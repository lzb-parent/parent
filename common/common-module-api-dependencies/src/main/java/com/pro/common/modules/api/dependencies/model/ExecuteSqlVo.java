package com.pro.common.modules.api.dependencies.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ExecuteSqlVo {
    @Schema(description = "sql语句")
    private String sql;
}
