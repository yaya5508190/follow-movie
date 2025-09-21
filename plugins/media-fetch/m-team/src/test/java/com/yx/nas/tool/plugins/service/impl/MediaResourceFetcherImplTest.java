package com.yx.nas.tool.plugins.service.impl;

import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

@SpringBootTest
class MediaResourceFetcherImplTest {
    @Resource
    private MediaResourceFetcher mediaResourceFetcher;

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
}
