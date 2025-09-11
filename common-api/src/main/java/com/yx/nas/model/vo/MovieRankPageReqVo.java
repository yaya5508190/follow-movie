package com.yx.nas.model.vo;

import com.yx.nas.model.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovieRankPageReqVo extends PageRequest {
    private String category;
}
