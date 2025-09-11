package com.yx.nas.model.common;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    /**
     * 数据列表
     */
    private List<T> records;
    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 每页大小
     */
    private Integer pageSize;


    /**
     * 总记录数
     */
    private Integer total;

}
