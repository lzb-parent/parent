package com.pro.common.web.security;

import cn.hutool.core.util.ObjUtil;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.common.model.ICountry;
import com.pro.common.modules.api.dependencies.common.service.ICountryService;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.component.TokenAuthResolver;
import com.pro.common.web.security.component.MyLocalResolver;
import com.pro.common.web.security.service.TokenService;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.LogicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.util.List;

@Configuration
@DependsOn("jTDServiceImpl")
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private IAuthRoleService authRouteService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private TokenService tokenService;

    /**
     * 获取登录信息token
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new TokenAuthResolver(authRouteService, tokenService, commonProperties));
    }

    @Bean
    public LocaleResolver localeResolver(ICountryService countryService) {
//     todo 自定义翻译   TransConstant.initTransMap();
        return new MyLocalResolver(ObjUtil.defaultIfNull(LogicUtils.and(countryService.getActiveFirst(), ICountry::getLangCode), CommonConst.Str.LANG_EN));
    }

// 直接在 user/admin/agent
//    /**
//     * 实体统一接口入口(鉴权)
//     */
//    @Bean
//    public ICommonDataService<?> commonModelService() {
//        switch (commonProperties.getApplication().getRole()) {
//            case ADMIN:
//                return new AdminCommonService<>();
//            case AGENT:
//                return new AgentCommonService<>();
//            case USER:
//                return new UserCommonService<>();
//            default:
//                throw new BusinessException("commonModelService error| role = " + commonProperties.getApplication().getRole());
//        }
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 新建目录
        String savePath = commonProperties.getFiles().getSavePath();
        commonProperties.getFiles().getModules().values().forEach(fileModule -> {
            File saveFile = new File(savePath + File.separator + fileModule.getCode());
            if (!saveFile.exists()) {
                boolean mkdirs = saveFile.mkdirs();
                AssertUtil.isTrue(mkdirs,"目录创建失败:"+saveFile.getAbsolutePath());
            }
        });
        //静态文件
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + savePath + "/");
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("*");
//        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
