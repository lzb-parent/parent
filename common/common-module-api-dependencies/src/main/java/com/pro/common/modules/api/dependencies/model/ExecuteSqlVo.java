package com.pro.common.modules.api.dependencies.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ExecuteSqlVo {
    @ApiModelProperty("sql语句")
    private String sql;
}
