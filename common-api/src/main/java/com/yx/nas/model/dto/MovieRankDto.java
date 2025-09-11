package com.yx.nas.model.dto;

import com.yx.nas.model.base.BaseMedia;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class MovieRankDto extends BaseMedia {
    //媒体评分
    public Double rating;

    //上映日期
    public LocalDate releaseDate;
}
