package com.pro.common.modules.api.dependencies.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "已登录信息")
public class LoginRequest {
    private String username;
    private String phone;
    private String email;
    @Schema(description = "登录token")
    private String token;
    private String password;
}
