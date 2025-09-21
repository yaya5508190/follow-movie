package com.yx.nas.api;

import com.yx.nas.model.common.PageResult;
import com.yx.nas.model.dto.MediaResourceDto;
import com.yx.nas.model.dto.MovieRankDto;
import com.yx.nas.model.vo.MediaResourcePageReqVo;

import java.util.List;
import java.util.Map;

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
     * @param reqVo 查询参数
     * @return 媒体列表
     */
    PageResult<MediaResourceDto> search(MediaResourcePageReqVo reqVo) throws Exception;
}
