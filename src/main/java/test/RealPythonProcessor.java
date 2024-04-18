package test;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

import java.util.List;

public class RealPythonProcessor implements PageProcessor {


    private Site site = Site
            .me()
            .setTimeOut(10000)
            .setSleepTime(3000)
            .setCharset("UTF-8")
            .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36\n" +
                    "\uFEFF\n" +
                    "weiboFeedList.js?ver…on=202403291024:159 \n" +
                    "[video.gifvideo]\n" +
                    "weibo:1 Third-party cookie will be blocked. Learn more in the Issues tab.\n" +
                    "\uFEFF\n" +
                    "\n");

    @Override
    public void process(Page page) {
        List<String> images=page.getHtml().xpath("//img/@src").all();
        page.putField("imageUrls", images);
        page.addTargetRequests(images);

    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {
        Spider.create(new RealPythonProcessor())
                .addUrl("https://realpython.com/")
                .addPipeline(new FilePipeline("D:\\webmagic\\"))
                .thread(5)
                .run();
    }
}
