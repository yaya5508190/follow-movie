package com.yx.nas.tool.plugins.controller;

import com.yx.nas.tool.plugins.model.common.PageRequest;
import com.yx.nas.tool.plugins.model.common.PageResult;
import com.yx.nas.tool.plugins.model.movie.Movie;
import com.yx.nas.tool.plugins.service.impl.DouBanMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/movieRank")
public class MovieRankController {
    @Autowired
    private DouBanMovieService doubanMovieService;

    @GetMapping("/")
    public ResponseEntity<PageResult<Movie>> list(PageRequest page) throws Exception {
        return ResponseEntity.ok(doubanMovieService.fetchMoviePageList(page));
    }
}
