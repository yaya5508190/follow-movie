package com.yx.nas.tool.plugins.model.vo;

import com.yx.nas.model.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@Data
public class MTeamMediaResourceReqVo {
    private String mode = "normal";
    private Integer visible = 1;
    private String keyword;
    private List<String> categories = new ArrayList<>();
    private Integer pageNumber;
    private String sortDirection = "DESC";
    private String sortField = "SEEDERS";
    private Integer pageSize;
}
