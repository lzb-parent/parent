package com.pro.common.web.security;

import cn.hutool.core.util.ObjUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.common.model.ICountry;
import com.pro.common.modules.api.dependencies.common.service.ICountryService;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.component.CustomTokenResolver;
import com.pro.common.web.security.component.MyLocalResolver;
import com.pro.common.web.security.component.xss.JsonHtmlXssDeserializer;
import com.pro.common.web.security.service.TokenService;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.LogicUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
     * 解决 ID_WORKER 生成主键太长导致 js 精度丢失（JackJson 处理方式）
     */
    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder, @Value("${spring.application.name}") String springApplicationName) {
        ObjectMapper objectMapper = builder.createXmlMapper(false).build();
        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行系列化
        // Include.Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为 空（""） 或者为 NULL 都不序列化，则返回的json是没有这个字段的。这样对移动端会更省流量
        // Include.NON_NULL 属性为NULL 不序列化,就是为null的字段不参加序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 解决,页面通过@RequestBody传多余的属性时,报错不存在属性问题
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 解决,JavaScript 无法处理 Java 的长整型 Long 导致精度丢失，具体表现为主键最后两位永远为 0，解决思路： Long 转为 String 返回
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
        simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
        // 时间转换
        simpleModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        simpleModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
//          simpleModule.addDeserializer(Enum.class, new EnumDeserializer());

        if (EnumApplication.user.equals(commonProperties.getApplication())) {
            // 富文本无法插入
            simpleModule.addDeserializer(String.class, new JsonHtmlXssDeserializer(String.class));
        }
        objectMapper.registerModule(simpleModule);

        // 初始化json工具
        JSONUtils.init(objectMapper);

        return objectMapper;
    }

    /**
     * 获取登录信息token
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new CustomTokenResolver(authRouteService, tokenService, commonProperties));
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
        //静态文件
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + commonProperties.getFiles().getSavePath() + "/");
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
