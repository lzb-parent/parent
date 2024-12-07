package com.pro.common.modules.api.dependencies.model;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String phone;
    private String email;
    private String token;
    private String password;
}
