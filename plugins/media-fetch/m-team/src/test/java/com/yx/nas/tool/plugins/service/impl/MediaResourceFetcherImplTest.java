package com.yx.nas.tool.plugins.service.impl;

import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.api.MediaResourceTorrentDownloader;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import com.yx.nas.tool.plugins.MTeamMediaResourceApplication;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@SpringBootTest(
        classes =  MTeamMediaResourceApplication.class,
        properties = "plugin.standalone=true"
)
class MediaResourceFetcherImplTest {
    @Resource
    private MediaResourceFetcher mediaResourceFetcher;

    @Resource
    private MediaResourceTorrentDownloader mediaResourceTorrentDownloader;

    @Test
    void testSearch() throws Exception {
        MediaResourcePageReqVo reqVo = new MediaResourcePageReqVo();
        reqVo.setKeyword("龙珠");
        reqVo.setPageNum(1);
        reqVo.setPageSize(10);
        reqVo.setExtra(Map.ofEntries(
                Map.entry("categories", List.of("405"))
        ));
        PageResult<MediaResourceDto> mediaResourceDtos = mediaResourceFetcher.search(reqVo);
        Assert.isTrue(mediaResourceDtos.getRecords().size() == 10, "数量必须是10");

        for (MediaResourceDto dto : mediaResourceDtos.getRecords()) {
            Assert.isTrue(dto.getDesc().contains("龙珠"), "描述必须包含龙珠");
        }
    }

    //测试downloadTorrent
    @Test
    void testDownloadTorrent() throws Exception {
        String resourceId = "1047237";
        boolean result = mediaResourceTorrentDownloader.downloadTorrent(resourceId);
        Assert.isTrue(result, "下载种子失败");
    }
}
