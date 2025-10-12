package com.yx.nas.tool.plugins.service;

import com.yx.nas.tool.plugins.common.CommonApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = CommonApplication.class,
        properties = "plugin.standalone=true"
)
public class QBittorrentServiceTest {
    @Resource
    QBittorrentService qBittorrentService;

    @Test
    void downloadResourceTest(){
        qBittorrentService.downloadResource(17L,1L);
    }
}
