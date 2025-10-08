package com.yx.nas.tool.plugins.common.service;

import com.yx.nas.entity.MediaTorrentRecord;
import com.yx.nas.enums.TorrentStatusEnum;
import com.yx.nas.repository.MediaTorrentRecordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.function.Function;

@Service
@Slf4j
public class CommonService {

    public final MediaTorrentRecordRepository mediaTorrentRecordRepository;
    private final WebClient webClient;

    public CommonService(MediaTorrentRecordRepository mediaTorrentRecordRepository, WebClient webClient) {
        this.mediaTorrentRecordRepository = mediaTorrentRecordRepository;
        this.webClient = webClient;
    }

    /**
     * 通用下载种子的服务，具体下载逻辑传入lambda实现
     *
     * @param torrentRecordId 资源id
     * @param downloadLogic   下载逻辑,传入的string为种子文件本地临时路径
     */
    public void resolveDownloadTask(Long torrentRecordId, Function<String, Boolean> downloadLogic) {
        MediaTorrentRecord torrentRecord = mediaTorrentRecordRepository.findById(torrentRecordId);
        if (torrentRecord == null) {
            log.error("种子记录不存在，id: {}", torrentRecordId);
            return;
        }
        //如果状态是待下载，则开始下载
        if (TorrentStatusEnum.PENDING.getCode() == torrentRecord.torrentStatus()) {
            log.info("开始下载种子，id: {}", torrentRecordId);
            //通过url下载种子到临时目录
            String torrentUrl = torrentRecord.torrentUrl();
            Path tempFile = null;
            try {
                tempFile = Files.createTempFile("torrent-" + UUID.randomUUID(), ".torrent");
                log.debug("创建临时文件: {}", tempFile.toAbsolutePath());

                // 同步下载文件（WebClient 会自动跟随 302 重定向）
                byte[] bytes = webClient.get()
                        .uri(new URI(torrentUrl))
                        .retrieve()
                        .bodyToMono(byte[].class)
                        .block();

                if (bytes == null || bytes.length == 0) {
                    log.error("下载种子文件为空, url: {}", torrentUrl);
                    return;
                }

                // 写入文件
                Files.write(tempFile, bytes);
                log.info("种子下载到临时文件: {}, 大小: {} bytes", tempFile.toAbsolutePath(), bytes.length);

                // 执行下载逻辑
                boolean downloadSuccess = downloadLogic.apply(tempFile.toAbsolutePath().toString());
                if (downloadSuccess) {
                    log.info("种子下载成功，id: {}", torrentRecordId);
                } else {
                    log.error("种子下载失败，id: {}", torrentRecordId);
                }

            } catch (IOException | URISyntaxException e) {
                log.error("下载种子文件失败, url: {}", torrentUrl, e);
            } finally {
                // 在下载逻辑执行完成后删除临时文件
                if (tempFile != null) {
                    try {
                        boolean deleted = Files.deleteIfExists(tempFile);
                        if (deleted) {
                            log.debug("已删除临时种子文件: {}", tempFile.toAbsolutePath());
                        }
                    } catch (IOException e) {
                        log.warn("删除临时种子文件失败: {}", tempFile.toAbsolutePath(), e);
                    }
                }
            }
        } else {
            log.info("种子记录状态不是待下载，跳过，id: {}, status: {}", torrentRecordId, torrentRecord.torrentStatus());
        }
    }
}
