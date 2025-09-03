package com.yx.nas.tool.plugins.model.base;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class BaseMedia {
    //媒体标题
    public String title;
    //媒体封面图片
    public String coverPic;
    //媒体信息页
    public String uri;
    //媒体评分
    public Double rating;
    //媒体描述
    public String desc;
    //上映日期
    public LocalDate releaseDate;
}
