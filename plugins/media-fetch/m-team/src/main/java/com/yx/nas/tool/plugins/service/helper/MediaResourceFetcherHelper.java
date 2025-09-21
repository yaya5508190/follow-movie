package com.yx.nas.tool.plugins.service.helper;

import com.yx.nas.model.vo.MediaResourcePageReqVo;
import com.yx.nas.tool.plugins.model.vo.MTeamMediaResourceReqVo;

import java.util.List;

public class MediaResourceFetcherHelper {
    public static MTeamMediaResourceReqVo buildMTeamMediaResourceReqVo(MediaResourcePageReqVo reqVo) {
        MTeamMediaResourceReqVo mTeamMediaResourceReqVo = new MTeamMediaResourceReqVo();
        mTeamMediaResourceReqVo.setKeyword(reqVo.getKeyword());
        mTeamMediaResourceReqVo.setPageNumber(reqVo.getPageNum());
        mTeamMediaResourceReqVo.setPageSize(reqVo.getPageSize());
        mTeamMediaResourceReqVo.setCategories((List<String>) reqVo.getExtra().get("categories"));
        return mTeamMediaResourceReqVo;
    }
}
