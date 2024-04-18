package com.lhclike.controller;

import com.lhclike.ItemService.ItemService;
import com.lhclike.pojo.Item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;


import java.util.ArrayList;
import java.util.List;

@Component
public class JobProcessor implements PageProcessor {

    @Autowired
    private MyDataPipeline myDataPipeline;

    @Autowired
    private ItemService itemService;



    //private String url ="https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&enc=utf-8&wq=%E6%89%8B%E6%9C%BA&pvid=8858151673f941e9b1a4d2c7214b2b52";
    private String url ="https://realpython.com/";
    @Override
    public void process(Page page) {

       /* // 设置 WebDriver 路径
        System.setProperty("webdriver.chrome.driver", "F:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");



        ChromeOptions options = new ChromeOptions();
        // 使用Selenium获取动态加载的内容
        //解决 403 出错问题
        options.addArguments("--remote-allow-origins=*");

        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");

        WebDriver driver = new ChromeDriver(options);



        driver.get(page.getUrl().toString());
        // 等待页面加载完成，这里可以使用显式等待或隐式等待
*/

//        // 将driver页面源码传给WebMagic处理
//            Html html = new Html(driver.getPageSource());


        Html html=page.getHtml();

        List<String> images=page.getHtml().xpath("//div[@class='embed-responsive embed-responsive-16by9']").all();

        List<Item> itemList = new ArrayList<>();
        for (String src : images) {
            Html srcHtml = new Html(src);
            Item item = new Item();
            item.setUrl(page.getUrl().toString());
            item.setPic(srcHtml.xpath("//img/@src").get());
            item.setTitle(srcHtml.xpath("//img/@alt").get());
            itemList.add(item);
            // 将每个图像和标题作为单独的 Item 对象存储

        }
        page.putField("itemInfo" , itemList);
    }
//        // 设置 WebDriver 路径
//        System.setProperty("webdriver.chrome.driver", "F:\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
//
//        ChromeOptions options = new ChromeOptions();
//        // 使用Selenium获取动态加载的内容
//        //解决 403 出错问题
//        options.addArguments("--remote-allow-origins=*");
//        WebDriver driver = new ChromeDriver(options);
//        options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36");
//        driver.get(page.getUrl().toString());
//        // 等待页面加载完成，这里可以使用显式等待或隐式等待
//        try {
//            Thread.sleep(10000); // 示例，实际应用中可用更好的等待方法
//        // 将driver页面源码传给WebMagic处理
//        Html html = new Html(driver.getPageSource());
       /* List<String> images=page.getHtml().xpath("//img/@src").all();
        page.putField("imageUrls", images);
        page.addTargetRequests(images);*/

    private Site site = Site.me()
            .setCharset("UTF-8")//设置编码
            .setTimeOut(10 * 1000)//设置超时时间
            .setRetrySleepTime(3000)//设置重试的间隔时间
            .setRetryTimes(3)//设置重试的次数
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36")//设置请求头
            ;
    @Override
    public Site getSite() {
        return site;
    }


    /**
     * 定时执行
     *
     * initialDelay：启动项目延迟执行
     * fixedDelay：每隔5s执行一次
     */
    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    public void process() {
        Spider.create(new JobProcessor())
                // 初始访问url地址
                .addUrl(url)
                .thread(6)
                .addPipeline(this.myDataPipeline)
                .run();
    }
}
