package com.yx.nas.model.base;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BaseMedia {
    //媒体标题
    public String title;
    //媒体封面图片
    public String coverPic;
    //媒体信息页
    public String uri;
    //媒体描述
    public String desc;
}
