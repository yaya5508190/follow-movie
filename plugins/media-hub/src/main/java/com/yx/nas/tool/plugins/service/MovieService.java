package com.yx.nas.tool.plugins.service;

import com.yx.nas.tool.plugins.model.common.PageRequest;
import com.yx.nas.tool.plugins.model.common.PageResult;
import com.yx.nas.tool.plugins.model.movie.Movie;

public interface MovieService {
    /**
     * 获取分页电影推荐列表
     * @param page
     * @return 返回电影列表
     */
    PageResult<Movie> fetchMoviePageList(PageRequest page)  throws Exception;
}
