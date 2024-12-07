package com.pro.common.module.api.system;


import com.pro.common.module.api.system.service.AuthDictService;
import com.pro.common.module.api.system.model.util.AuthDictUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
public class AuthDictConfig {
    @Autowired
    private AuthDictService authDictService;
    @PostConstruct
    public void init() {
        AuthDictUtil.init(authDictService);
    }
}
