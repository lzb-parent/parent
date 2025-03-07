package com.pro.common.web.security.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "表信息对象")
public class JTDTableInfoRequest {
    private String urlTemplate =  "/common/jtd/{option}/{entityName}";
}
