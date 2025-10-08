package com.yx.nas.tool.plugins.common.service;

import com.yx.nas.entity.MediaTorrentRecord;
import com.yx.nas.entity.MediaTorrentRecordDraft;
import com.yx.nas.enums.TorrentStatusEnum;
import com.yx.nas.repository.MediaTorrentRecordRepository;
import com.yx.nas.tool.plugins.common.CommonApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Integration test for CommonService using the Spring context.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(
        classes = CommonApplication.class,
        properties = "plugin.standalone=true"
)// Roll back database changes after each test.
class CommonServiceTest {

    @Autowired
    private CommonService commonService;

    @Autowired
    private MediaTorrentRecordRepository mediaTorrentRecordRepository;

    @Test
    void testResolveDownloadTaskSuccess() {
        commonService.resolveDownloadTask(13L, this::testDownload);
    }

    public boolean testDownload(String torrentPath) {
        return true;
    }
}