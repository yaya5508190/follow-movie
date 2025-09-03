package com.yx.nas.tool.plugins.service.impl;

import com.yx.framework.spider.core.ParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.nas.tool.plugins.model.common.PageRequest;
import com.yx.nas.tool.plugins.model.common.PageResult;
import com.yx.nas.tool.plugins.model.movie.Movie;
import com.yx.nas.tool.plugins.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DouBanMovieService implements MovieService {

    private final Fetcher fetcher;
    private final ParserEngine parserEngine;
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

    public DouBanMovieService(Fetcher fetcher, ParserEngine parserEngine) {
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
    }

    public Map<String, Object> getMovieList() throws Exception {
        String url = "https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie?start=0&limit=60&category=热门&type=全部";
        Page page = fetcher.fetch(SpiderRequest.get(url, headers));
        String context = Optional.ofNullable(page.contentType()).orElse("").toLowerCase();
        return parserEngine.parse(page, url, context);
    }

    @Override
    public PageResult<Movie> fetchMoviePageList(PageRequest page) throws Exception {
        String url = "https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie?start=" + page.getStartNum() + "&limit=" + page.getPageSize() + "&category=热门&type=全部";
        Page fetchPage = fetcher.fetch(SpiderRequest.get(url, headers));
        String context = Optional.ofNullable(fetchPage.contentType()).orElse("").toLowerCase();
        Map<String,Object> parseData = parserEngine.parse(fetchPage, url, context);
        PageResult<Movie> pageResult = new PageResult<>();
        pageResult.setRecords((List<Movie>) parseData.get("data"));
        pageResult.setTotal((Integer) parseData.get("total"));
        pageResult.setPageNum(page.getPageNum());
        pageResult.setPageSize(page.getPageSize());
        return pageResult;
    }
}
