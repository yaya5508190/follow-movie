package com.yx.nas.tool.plugins.model.vo;

import com.yx.nas.tool.plugins.model.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovieRankPageReqVo extends PageRequest {
    private String category;
}
