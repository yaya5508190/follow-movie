package com.yx.nas.tool.plugins.common.service;

import com.yx.nas.repository.MediaTorrentRecordRepository;
import com.yx.nas.tool.plugins.common.CommonApplication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


/**
 * Integration test for CommonService using the Spring context.
 */
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
        commonService.resolveDownloadTask(17L, this::testDownload);
    }

    public boolean testDownload(String torrentPath) {
        return true;
    }
}