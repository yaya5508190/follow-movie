package com.yx.nas.api;

import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MovieRankDto;
import com.yx.nas.model.vo.MovieRankPageReqVo;

public interface MovieRankFetcher {
    /**
     * 获取分页电影推荐列表
     * @param page
     * @return 返回电影列表
     */
    PageResult<MovieRankDto> fetchMoviePageList(MovieRankPageReqVo page)  throws Exception;
}
