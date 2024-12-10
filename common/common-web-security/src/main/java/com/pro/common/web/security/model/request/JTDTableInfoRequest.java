package com.pro.common.web.security.model.request;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(description = "表信息对象")
public class JTDTableInfoRequest {
    private String urlTemplate =  "/common/jtd/{option}/{entityName}";
}
