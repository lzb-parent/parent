package com.pro.common.web.controller.user.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class TeamRequest {

    private String start;
    private String end;

    @ApiModelProperty(value = "用户树查询的层级")//null表示无限级
    private Integer level;
}
