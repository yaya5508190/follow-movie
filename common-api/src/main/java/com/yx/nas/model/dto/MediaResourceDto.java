package com.yx.nas.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class MediaResourceDto {
    private String id; // 资源ID
    private String name; // 资源名称
    private String desc; // 描述
    private List<String> images; // 资源图片
    private String discount; // 资源折扣
    private LocalDateTime discountEndTime; // 资源折扣结束时间
    private List<String> labels; // 资源标签
    private Long size; // 资源大小
    private Integer seeders; // 做种数
    private Integer leechers; // 下载数
    private String imdb; // IMDB地址
    private Double imdbRating; // IMDB评分
    private String douban; // 豆瓣地址
    private Double doubanRating; // 豆瓣评分
    private LocalDateTime createdDate; // 资源创建时间
}
