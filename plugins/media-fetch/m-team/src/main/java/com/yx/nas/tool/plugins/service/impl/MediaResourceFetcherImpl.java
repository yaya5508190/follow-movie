package com.yx.nas.tool.plugins.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.core.JsonParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import com.yx.nas.tool.plugins.model.vo.MTeamMediaResourceReqVo;
import com.yx.nas.tool.plugins.service.helper.MediaResourceFetcherHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MediaResourceFetcherImpl implements MediaResourceFetcher {
    private final Fetcher fetcher;
    private final JsonParserEngine parserEngine;

    public MediaResourceFetcherImpl(Fetcher fetcher, JsonParserEngine parserEngine) {
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
    }

    @Override
    public String siteName() {
        return "M-Team";
    }

    @Override
    public PageResult<MediaResourceDto> search(MediaResourcePageReqVo reqVo) throws Exception {
        String url = "https://api.m-team.cc/api/torrent/search";

        MTeamMediaResourceReqVo mTeamMediaResourceReqVo = MediaResourceFetcherHelper.buildMTeamMediaResourceReqVo(reqVo);
        Page fetchPage = fetcher.fetch(SpiderRequest.post(url, Map.ofEntries(
                Map.entry("Accept", "*/*"),
                Map.entry("Content-Type", "application/json")
        ), mTeamMediaResourceReqVo));

        String context = Optional.ofNullable(fetchPage.contentType()).orElse("").toLowerCase();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(new String(fetchPage.body(), StandardCharsets.UTF_8));
        Map<String, Object> parseData = parserEngine.parse(node, url, context);

        PageResult<MediaResourceDto> pageResult = new PageResult<>();
        pageResult.setPageNum(reqVo.getPageNum());
        pageResult.setPageSize(reqVo.getPageSize());
        pageResult.setTotal((Integer) parseData.get("total"));
        pageResult.setRecords((List<MediaResourceDto>) parseData.get("data"));

        return pageResult;
    }
}
