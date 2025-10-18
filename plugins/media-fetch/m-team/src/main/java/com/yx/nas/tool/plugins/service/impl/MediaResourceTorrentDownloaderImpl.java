package com.yx.nas.tool.plugins.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yx.framework.spider.core.JsonParserEngine;
import com.yx.framework.spider.fetch.Fetcher;
import com.yx.framework.spider.model.Page;
import com.yx.framework.spider.model.SpiderRequest;
import com.yx.nas.api.MediaResourceTorrentDownloader;
import com.yx.nas.dto.ApiKeyMediaFetchConfig;
import com.yx.nas.dto.DownloadToolConfigView;
import com.yx.nas.dto.MediaTorrentRecordInput;
import com.yx.nas.entity.MediaTorrentRecord;
import com.yx.nas.enums.AuthTypeEnum;
import com.yx.nas.enums.TorrentStatusEnum;
import com.yx.nas.model.event.DownloadEvent;
import com.yx.nas.repository.DownloadToolConfigRepository;
import com.yx.nas.repository.MediaFetchConfigRepository;
import com.yx.nas.repository.MediaTorrentRecordRepository;
import com.yx.nas.tool.plugins.MediaFetchPluginConfig;
import com.yx.nas.tool.plugins.constant.FetchURLConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.babyfish.jimmer.sql.ast.mutation.SimpleSaveResult;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
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
@Slf4j
public class MediaResourceTorrentDownloaderImpl implements MediaResourceTorrentDownloader, ApplicationContextAware {

    private final Fetcher fetcher;
    private final JsonParserEngine parserEngine;
    private final MediaFetchConfigRepository mediaFetchConfigRepository;
    private final MediaTorrentRecordRepository mediaTorrentRecordRepository;
    private final DownloadToolConfigRepository downloadToolConfigRepository;

    private final MediaFetchPluginConfig mediaFetchPluginConfig;
    private ApplicationContext applicationContext;

    public MediaResourceTorrentDownloaderImpl(
            Fetcher fetcher,
            JsonParserEngine parserEngine,
            MediaFetchConfigRepository mediaFetchConfigRepository,
            MediaTorrentRecordRepository mediaTorrentRecordRepository,
            DownloadToolConfigRepository downloadToolConfigRepository,
            MediaFetchPluginConfig mediaFetchPluginConfig) {
        this.fetcher = fetcher;
        this.parserEngine = parserEngine;
        this.mediaFetchConfigRepository = mediaFetchConfigRepository;
        this.mediaTorrentRecordRepository = mediaTorrentRecordRepository;
        this.downloadToolConfigRepository = downloadToolConfigRepository;
        this.mediaFetchPluginConfig = mediaFetchPluginConfig;
    }

    @Override
    public boolean downloadTorrent(String resourceId) throws Exception {
        // 从系统配置获取API Key
        ApiKeyMediaFetchConfig apiKeyMediaFetchConfig = mediaFetchConfigRepository.findBySourceAndType(
                mediaFetchPluginConfig.name(),
                AuthTypeEnum.API_KEY.getCode()
        );
        if (apiKeyMediaFetchConfig == null) {
            throw new IllegalArgumentException("未发现M-Team配置，请先配置M-Team");
        }
        String url = apiKeyMediaFetchConfig.getUrl() + FetchURLConstant.DOWNLOAD_TORRENT;
        String apiKey = apiKeyMediaFetchConfig.getApiKey();
        Long mediaFetchConfigId = apiKeyMediaFetchConfig.getId();

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
            SimpleSaveResult<MediaTorrentRecord> saveResult = mediaTorrentRecordRepository.save(mediaTorrentRecordInput);
            Long torrentRecordId = saveResult.getModifiedEntity().id();

            //站点配置获取下载器配置类型以及id
            DownloadToolConfigView downloadToolConfig = downloadToolConfigRepository.findByTypeOrDefault(mediaFetchConfigId);

            if (downloadToolConfig != null) {
                if (Objects.isNull(applicationContext.getParent())) {
                    applicationContext.publishEvent(new DownloadEvent(
                                    "m-team",
                                    torrentRecordId,
                                    downloadToolConfig.getType(),
                                    downloadToolConfig.getId()
                            )
                    );
                } else {
                    applicationContext.getParent().publishEvent(new DownloadEvent(
                                    "m-team",
                                    torrentRecordId,
                                    downloadToolConfig.getType(),
                                    downloadToolConfig.getId()
                            )
                    );
                }
            } else {
                throw new IllegalArgumentException("M-Team未找到下载器配置");
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
