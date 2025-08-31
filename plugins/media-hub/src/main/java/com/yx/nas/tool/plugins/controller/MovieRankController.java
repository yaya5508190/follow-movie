package com.yx.nas.tool.plugins.controller;

import com.yx.nas.tool.plugins.service.DoubanMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/movieRank")
public class MovieRankController {
    @Autowired
    private DoubanMovieService doubanMovieService;

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> list() throws Exception {
        return ResponseEntity.ok(doubanMovieService.getMovieList());
    }
}
