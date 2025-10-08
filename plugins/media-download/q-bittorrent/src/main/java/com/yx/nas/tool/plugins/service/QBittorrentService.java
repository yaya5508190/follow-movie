package com.yx.nas.tool.plugins.service;

import com.yx.nas.entity.DownloadToolInfo;
import com.yx.nas.repository.DownloadToolInfoRepository;
import com.yx.nas.tool.plugins.common.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
public class QBittorrentService {
    private final OkHttpClient okHttpClient;
    private final DownloadToolInfoRepository downloadToolInfoRepository;
    private final CommonService commonService;
    private String cookie; // 保存登录后的 cookie

    public QBittorrentService(OkHttpClient okHttpClient,
                              DownloadToolInfoRepository downloadToolInfoRepository,
                              CommonService commonService) {
        this.okHttpClient = okHttpClient;
        this.downloadToolInfoRepository = downloadToolInfoRepository;
        this.commonService = commonService;
    }

    /**
     * 登录 qBittorrent
     */
    private boolean login(DownloadToolInfo config) {
        try {
            log.info("尝试登录 qBittorrent: {}", config.url());

            // 构建表单数据
            RequestBody formBody = new FormBody.Builder()
                    .add("username", Objects.requireNonNull(config.username()))
                    .add("password", Objects.requireNonNull(config.password()))
                    .build();

            Request request = new Request.Builder()
                    .url(config.url() + "/api/v2/auth/login")
                    .post(formBody)
                    .build();

            try (Response response = okHttpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("qBittorrent 登录失败: HTTP {}", response.code());
                    return false;
                }

                // 获取 Set-Cookie header
                String setCookie = response.header("Set-Cookie");
                if (StringUtils.hasText(setCookie)) {
                    this.cookie = setCookie.split(";")[0];
                    log.info("qBittorrent 登录成功");
                    return true;
                } else {
                    log.error("qBittorrent 登录失败: 未获取到 cookie");
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("qBittorrent 登录失败", e);
            return false;
        }
    }

    public boolean downloadResource(Long mediaTorrentRecordId) {
        return commonService.resolveDownloadTask(mediaTorrentRecordId, this::createDownloadTask);
    }

    /**
     * 创建下载任务
     *
     * @param torrentPath 种子文件路径
     * @return 是否创建成功
     */
    private boolean createDownloadTask(String torrentPath) {
        try {
            //查询配置
            DownloadToolInfo config = queryDownloadToolInfo();
            if (config == null) {
                log.error("未配置下载工具");
                return false;
            }
            // 检查文件是否存在
            File torrentFile = new File(torrentPath);
            if (!torrentFile.exists()) {
                log.error("种子文件不存在: {}", torrentPath);
                return false;
            }

            // 如果没有 cookie 或 cookie 过期，先登录
            if (!StringUtils.hasText(cookie)) {
                if (!login(config)) {
                    return false;
                }
            }

            log.info("开始添加种子到 qBittorrent: {}", torrentPath);

            // 构建 multipart 请求
            String normalizedSavePath = config.savePath().replace('\\', '/');
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("torrents", torrentFile.getName(),
                            RequestBody.create(torrentFile, MediaType.parse("application/x-bittorrent")))
                    .addFormDataPart("savepath", normalizedSavePath)
                    .build();

            Request request = new Request.Builder()
                    .url(config.url() + "/api/v2/torrents/add")
                    .addHeader("Cookie", cookie)
                    .post(requestBody)
                    .build();

            // 调用 qBittorrent API 添加种子
            try (Response response = okHttpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                log.info("qBittorrent 响应: HTTP {}, Body: {}", response.code(), responseBody);

                // qBittorrent 成功添加会返回 "Ok."
                if (response.isSuccessful() && "Ok.".equals(responseBody)) {
                    log.info("种子添加成功: {}", torrentPath);
                    return true;
                } else if (response.code() == 403 || responseBody.contains("Forbidden")) {
                    log.info("Cookie 可能已过期，尝试重新登录");
                    if (login(config)) {
                        return createDownloadTask(torrentPath);
                    } else {
                        return false;
                    }
                } else {
                    log.error("种子添加失败: {}, 响应: {}", torrentPath, responseBody);
                    return false;
                }
            }
        } catch (IOException e) {
            log.error("创建下载任务失败: {}", torrentPath, e);
            return false;
        }
    }

    /**
     * 查询下载工具配置
     * TODO: 后续根据配置查询不同的下载工具,先写死
     */
    private DownloadToolInfo queryDownloadToolInfo() {
        return downloadToolInfoRepository.findByIdAndType(1L, 1);
    }
}
