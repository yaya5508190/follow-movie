package com.yx.nas.tool.plugins.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.core.JsonParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.dto.ApiKeyMediaFetchConfig;
import com.yx.nas.enums.AuthTypeEnum;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import com.yx.nas.repository.MediaFetchConfigRepository;
import com.yx.nas.tool.plugins.MediaFetchPluginConfig;
import com.yx.nas.tool.plugins.constant.FetchURLConstant;
import com.yx.nas.tool.plugins.model.vo.MTeamMediaResourceReqVo;
import com.yx.nas.tool.plugins.service.helper.MediaResourceFetcherHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MediaResourceFetcherImpl implements MediaResourceFetcher {
    private final Fetcher fetcher;
    private final JsonParserEngine parserEngine;
    private final MediaFetchConfigRepository mediaFetchConfigRepository;
    private final MediaFetchPluginConfig mediaFetchPluginConfig;

    public MediaResourceFetcherImpl(Fetcher fetcher,
                                    JsonParserEngine parserEngine,
                                    MediaFetchConfigRepository mediaFetchConfigRepository,
                                    MediaFetchPluginConfig mediaFetchPluginConfig
    ) {
        this.mediaFetchConfigRepository = mediaFetchConfigRepository;
        this.mediaFetchPluginConfig = mediaFetchPluginConfig;
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
    }

    @Override
    public String siteName() {
        return "M-Team";
    }

    @Override
    @SuppressWarnings("unchecked")
    public PageResult<MediaResourceDto> search(MediaResourcePageReqVo reqVo) throws Exception {
        // 从系统配置获取API Key
        ApiKeyMediaFetchConfig apiKeyMediaFetchConfig = mediaFetchConfigRepository.findBySourceAndType(
                mediaFetchPluginConfig.name(),
                AuthTypeEnum.API_KEY.getCode()
        );
        if (apiKeyMediaFetchConfig == null) {
            throw new IllegalArgumentException("未发现M-Team配置，请先配置M-Team");
        }

        // 构建请求URL
        String url = apiKeyMediaFetchConfig.getUrl() + FetchURLConstant.SEARCH_TORRENT;
        String apiKey = apiKeyMediaFetchConfig.getApiKey();
        if (StringUtils.isNotBlank(apiKey)) {
            MTeamMediaResourceReqVo mTeamMediaResourceReqVo = MediaResourceFetcherHelper.buildMTeamMediaResourceReqVo(reqVo);
            Page fetchPage = fetcher.fetch(SpiderRequest.post(url, Map.ofEntries(
                    Map.entry("Accept", "*/*"),
                    Map.entry("x-api-key", apiKey),
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
        } else {
            throw new IllegalArgumentException("请先配置M-Team API Key");
        }
    }
}
