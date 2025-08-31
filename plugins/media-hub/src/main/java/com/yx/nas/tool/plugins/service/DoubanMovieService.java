package com.yx.nas.tool.plugins.service;

import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.Result;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.framework.spider.spi.PageParser;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DoubanMovieService {

    private final Fetcher fetcher;
    private final List<PageParser<?>> parsers;
    private final Map<String, String> headers = Map.ofEntries(
            Map.entry("accept", "application/json, text/plain, */*"),
            Map.entry("accept-language", "zh-CN,zh;q=0.9"),
            Map.entry("cache-control", "no-cache"),
            Map.entry("origin", "https://movie.douban.com"),
            Map.entry("pragma", "no-cache"),
            Map.entry("priority", "u=1, i"),
            Map.entry("referer", "https://movie.douban.com/explore?support_type=movie&is_all=false&category=%E7%83%AD%E9%97%A8&type=%E5%85%A8%E9%83%A8"),
            Map.entry("sec-ch-ua", "\"Not;A=Brand\";v=\"99\", \"Google Chrome\";v=\"139\", \"Chromium\";v=\"139\""),
            Map.entry("sec-ch-ua-mobile", "?0"),
            Map.entry("sec-ch-ua-platform", "\"Windows\""),
            Map.entry("sec-fetch-dest", "empty"),
            Map.entry("sec-fetch-mode", "cors"),
            Map.entry("sec-fetch-site", "same-site"),
            Map.entry("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36")
    );

    public DoubanMovieService(Fetcher fetcher, List<PageParser<?>> parsers) {
        this.fetcher = fetcher;
        this.parsers = parsers;
    }

    public Map<String, Object> getMovieList() throws Exception {
        String url = "https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie?start=0&limit=60&category=热门&type=全部";
        Page page = fetcher.fetch(SpiderRequest.get(url,headers));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();
        return parse(page, url, context);
    }

    private Map<String, Object> parse(Page page, String url, String context) throws Exception {
        List<Map<String, Object>> parsed = new ArrayList<>();
        for (PageParser<?> parser : parsers) {
            if (!parser.supports(url, context)) continue;
            @SuppressWarnings("unchecked")
            List<Result<Object>> results = (List<Result<Object>>) (List<?>) parser.parse(page);
            for (Result<Object> r : results) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("url", r.request().url());
                item.put("data", r.data());
                parsed.add(item);
            }
        }

        Map<String, Object> resp = new LinkedHashMap<>();
        resp.put("requestUrl", url);
        resp.put("status", page.status());
        resp.put("contentType", context);
        resp.put("parsed", parsed);
        return resp;
    }
}
