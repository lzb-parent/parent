package com.pro.common.web.security;

import com.pro.common.module.api.system.model.intf.IAuthDictService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.service.TokenService;
import com.pro.framework.api.cache.ICacheManagerCenter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;


@Configuration
@DependsOn("jTDServiceImpl")
public class WebTokenConfig {
    /**
     * 获取登录信息token
     */
    @Bean
    public TokenService tokenService(CommonProperties commonProperties, ICacheManagerCenter cacheManagerCenter, IAuthDictService authDictService) {
        return new TokenService(commonProperties, cacheManagerCenter, authDictService);
    }

}
