package com.pro.common.modules.api.dependencies.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "登录token")
public class TokenRequest {
    @Schema(description = "登录token", requiredMode = Schema.RequiredMode.REQUIRED)
    private String token;
}
