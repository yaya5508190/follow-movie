package com.yx.nas.api;

import com.yx.nas.model.dto.MovieRankDto;

import java.util.List;

/**
 * 媒体信息获取接口
 * 包括媒体查询列表，媒体种子下载等功能
 */
public interface MediaResourceFetcher {
    /**
     * 注册站点名称
     *
     * @return 站点名称
     */
    String siteName();

    /**
     * 媒体搜索
     *
     * @param keyword  关键词
     * @param page     页码
     * @param pageSize 每页数量
     * @return 媒体列表
     */
    List<MovieRankDto> search(String keyword, int page, int pageSize);
}
