package com.yx.nas.tool.plugins.controller;

import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MovieRankDto;
import com.yx.nas.model.vo.MovieRankPageReqVo;
import com.yx.nas.tool.plugins.service.impl.DouBanMovieRankFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/movieRank")
public class MovieRankController {
    @Autowired
    private DouBanMovieRankFetcher doubanMovieService;

    @GetMapping("/")
    public ResponseEntity<PageResult<MovieRankDto>> list(MovieRankPageReqVo movieRankPageReqVo) throws Exception {
        return ResponseEntity.ok(doubanMovieService.fetchMoviePageList(movieRankPageReqVo));
    }
}
