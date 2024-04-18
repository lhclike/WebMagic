package test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

import java.util.List;

//实现Pageprocessor接口
public class ZhihuPageProcessor implements PageProcessor {
//用户代理，http头信息需要的数据类型，字符集，设置编码格式
//    private Site site = Site.me().setCycleRetryTimes(5).setRetryTimes(5).setSleepTime(500).setTimeOut(3 * 60 * 1000)
//            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:124.0) Gecko/20100101 Firefox/124.0")
//            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
//            .addHeader("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
//            .setCharset("UTF-8");
            private Site site = Site.me()
                .setTimeOut(10000)  // 设置连接超时时间和读取超时时间为10000毫秒（10秒）
        .setRetryTimes(3)   // 设置重试次数为3次
        .setSleepTime(1000); // 设置重试的间隔时间为1000毫秒（1秒）
//设置了爬取文章的阈值
    private static final int voteNum = 1000;


    @Override
    public void process(Page page) {
        //获取a标签的href属性，并且加入到url缓存队列中
        List<String> relativeUrl =page.getHtml().links().regex("https://zhihu\\.com/[\\w\\-]+/[\\w\\-]+").all();
        page.addTargetRequests(relativeUrl);
        relativeUrl = page.getHtml().xpath("//div[@id='zh-question-related-questions']//a[@class='question_link']/@href").all();
        page.addTargetRequests(relativeUrl);
        List<String> answers =  page.getHtml().xpath("//div[@id='zh-question-answer-wrap']/div").all();
        boolean exist = false;
        for(String answer:answers){
            String vote = new Html(answer).xpath("//div[@class='zm-votebar']//span[@class='count']/text()").toString();
            if(Integer.valueOf(vote) >= voteNum){
                page.putField("vote",vote);
                page.putField("content",new Html(answer).xpath("//div[@class='zm-editable-content']"));
                page.putField("userid", new Html(answer).xpath("//a[@class='author-link']/@href"));
                exist = true;
            }
        }
        if(!exist){
            page.setSkip(true);
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "7890");
        System.setProperty("https.proxyHost", "127.0.0.1");
        System.setProperty("https.proxyPort", "7890");
        Spider.create(new ZhihuPageProcessor()).
                addUrl("https://www.zhihu.com/search?q=java&type=content").
                addPipeline(new FilePipeline("D:\\webmagic\\")).
                thread(5).
                run();
    }
}