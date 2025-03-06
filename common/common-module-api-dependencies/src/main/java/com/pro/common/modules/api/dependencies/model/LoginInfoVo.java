package com.pro.common.modules.api.dependencies.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class LoginInfoVo implements Serializable {
//    private static final long serialVersionUID = 1L;

    private Long id;
    private String username;
    private Boolean isDemo;

    public LoginInfoVo() {}
}
