package com.yx.nas.tool.plugins.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.core.JsonParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MovieRankDto;
import com.yx.nas.model.vo.MovieRankPageReqVo;
import com.yx.nas.api.MovieRankFetcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class DouBanMovieRankFetcher implements MovieRankFetcher {

    private final Fetcher fetcher;
    private final JsonParserEngine parserEngine;
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

    public DouBanMovieRankFetcher(Fetcher fetcher, JsonParserEngine parserEngine) {
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
    }

    @Override
    public PageResult<MovieRankDto> fetchMoviePageList(MovieRankPageReqVo movieRankPageReqVo) throws Exception {
        String url = "https://m.douban.com/rexxar/api/v2/subject/recent_hot/movie?start=" + movieRankPageReqVo.getStartNum() + "&limit=" + movieRankPageReqVo.getPageSize() + "&category=" + movieRankPageReqVo.getCategory() + "&type=全部";
        Page fetchPage = fetcher.fetch(SpiderRequest.get(url, headers));
        String context = Optional.ofNullable(fetchPage.contentType()).orElse("").toLowerCase();

        Document doc = Jsoup.parse(new String(fetchPage.body(), StandardCharsets.UTF_8), fetchPage.request().url());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(doc.body().text());

        Map<String,Object> parseData = parserEngine.parse(node, url, context);
        PageResult<MovieRankDto> pageResult = new PageResult<>();
        pageResult.setRecords((List<MovieRankDto>) parseData.get("data"));
        pageResult.setTotal((Integer) parseData.get("total"));
        pageResult.setPageNum(movieRankPageReqVo.getPageNum());
        pageResult.setPageSize(movieRankPageReqVo.getPageSize());
        return pageResult;
    }
}
