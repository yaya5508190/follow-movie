package com.yx.nas.tool.plugins.controller;

import com.yx.nas.api.MediaResourceFetcher;
import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/media-resource")
public class MTeamMediaResourceFetcherController {

    private final MediaResourceFetcher mediaResourceFetcher;

    public MTeamMediaResourceFetcherController(MediaResourceFetcher mediaResourceFetcher) {
        this.mediaResourceFetcher = mediaResourceFetcher;
    }

    @PostMapping("/search")
    public PageResult<MediaResourceDto> search(@RequestBody MediaResourcePageReqVo reqVo) throws Exception {
        return mediaResourceFetcher.search(reqVo);
    }
}
