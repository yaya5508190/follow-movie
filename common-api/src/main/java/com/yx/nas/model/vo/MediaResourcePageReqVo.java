package com.yx.nas.model.vo;

import com.yx.nas.model.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class MediaResourcePageReqVo extends PageRequest {
    /**
     * 查询关键词
     */
    private String keyword;

    /**
     * 额外参数
     * 由于不同站点可能需要不同的参数，所以这里使用 Map 来存储额外参数
     * 例如类别、年份、语言等
     */
    private Map<String, Object> extra;
}
