package com.pro.common.web.controller.user.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TeamRequest {

    private String start;
    private String end;

    @Schema(description = "用户树查询的层级")//null表示无限级
    private Integer level;
}
