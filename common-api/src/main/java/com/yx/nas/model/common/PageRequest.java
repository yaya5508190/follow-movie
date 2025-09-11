package com.yx.nas.model.common;

import lombok.Data;

@Data
public class PageRequest {
    /**
     * 页码（从 1 开始）
     */
    private Integer pageNum = 1;

    /**
     * 每页大小
     */
    private Integer pageSize = 10;

    /**
     * 记录总数
     */
    private Integer total;

    /**
     * 计算开始Num
     *
     * @return 返回快开始Num
     */
    public Integer getStartNum() {
        return (pageNum - 1) * pageSize;
    }

    /**
     * 计算结束Num
     *
     * @return 返回结束Num
     */
    public Integer getEndNum() {
        return total != null ? Math.min((pageNum * pageSize), total) : (pageNum * pageSize);
    }
}
