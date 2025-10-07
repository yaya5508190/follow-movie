package com.yx.nas.tool.plugins.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.core.JsonParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.nas.api.MediaResourceTorrentDownloader;
import com.yx.nas.dto.MediaTorrentRecordInput;
import com.yx.nas.enums.AuthTypeEnum;
import com.yx.nas.enums.TorrentStatusEnum;
import com.yx.nas.model.event.DownloadEvent;
import com.yx.nas.repository.MediaFetchAuthRepository;
import com.yx.nas.repository.MediaTorrentRecordRepository;
import com.yx.nas.tool.plugins.MediaFetchPluginConfig;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 种子下载器实现类
 * </p>
 *
 * @author yx
 * @date 2025-10-07
 */
@Service
public class MediaResourceTorrentDownloaderImpl implements MediaResourceTorrentDownloader, ApplicationContextAware {

    private final Fetcher fetcher;
    private final JsonParserEngine parserEngine;
    private final MediaFetchAuthRepository mediaFetchAuthRepository;
    private final MediaTorrentRecordRepository mediaTorrentRecordRepository;
    private final MediaFetchPluginConfig mediaFetchPluginConfig;
    private ApplicationContext applicationContext;

    public MediaResourceTorrentDownloaderImpl(
            Fetcher fetcher,
            JsonParserEngine parserEngine,
            MediaFetchAuthRepository mediaFetchAuthRepository,
            MediaTorrentRecordRepository mediaTorrentRecordRepository,
            MediaFetchPluginConfig mediaFetchPluginConfig) {
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
        this.mediaFetchAuthRepository = mediaFetchAuthRepository;
        this.mediaTorrentRecordRepository = mediaTorrentRecordRepository;
        this.mediaFetchPluginConfig = mediaFetchPluginConfig;
    }

    @Override
    public boolean downloadTorrent(String resourceId) throws Exception {
        String url = "https://api.m-team.cc/api/torrent/genDlToken";
        // 从系统配置获取API Key
        String apiKey = mediaFetchAuthRepository.findBySourceAndType(
                mediaFetchPluginConfig.name(),
                AuthTypeEnum.API_KEY.getCode()
        ).toEntity().apiKey();

        if (StringUtils.isNotBlank(apiKey)) {
            Page fetchPage = fetcher.fetch(SpiderRequest.post(url, Map.ofEntries(
                    Map.entry("Accept", "*/*"),
                    Map.entry("x-api-key", apiKey),
                    Map.entry("Content-Type", "application/x-www-form-urlencoded")
            ), Map.ofEntries(
                    Map.entry("id", resourceId)
            )));

            String context = Optional.ofNullable(fetchPage.contentType()).orElse("").toLowerCase();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(new String(fetchPage.body(), StandardCharsets.UTF_8));
            Map<String, Object> parseData = parserEngine.parse(node, url, context);

            MediaTorrentRecordInput mediaTorrentRecordInput = new MediaTorrentRecordInput();
            mediaTorrentRecordInput.setResourceId(resourceId);
            mediaTorrentRecordInput.setFetcherSource(mediaFetchPluginConfig.name());
            mediaTorrentRecordInput.setTorrentStatus(TorrentStatusEnum.PENDING.getCode());
            mediaTorrentRecordInput.setTorrentUrl(String.valueOf(parseData.get("torrentUrl")));
            mediaTorrentRecordRepository.save(mediaTorrentRecordInput);

            if (Objects.isNull(applicationContext.getParent())) {
                applicationContext.publishEvent(new DownloadEvent("m-team", "q-bittorrent"));
            } else {
                applicationContext.getParent().publishEvent(new DownloadEvent("m-team", "q-bittorrent"));
            }
            return true;
        }

        return false;
    }

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
