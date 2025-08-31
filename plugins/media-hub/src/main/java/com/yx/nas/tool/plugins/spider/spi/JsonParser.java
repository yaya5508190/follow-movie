package com.yx.nas.tool.plugins.spider.spi;

import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.Result;
import com.yx.framework.spider.spi.PageParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
public class JsonParser implements PageParser<String> {
    @Override
    public boolean supports(String url, String ct) { return ct != null && ct.contains("application/json"); }


    @Override
    public List<Result<String>> parse(Page page) {
        Document doc = Jsoup.parse(new String(page.body(), StandardCharsets.UTF_8), page.request().url());
        return List.of(new Result<>(page.request(), doc.body().text()));
    }
}
