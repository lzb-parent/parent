package com.pro.common.web.security.component;

import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.model.LoginInfo;
import com.pro.common.modules.service.dependencies.modelauth.base.LoginInfoService;
import com.pro.common.modules.service.dependencies.modelauth.base.TokenService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class TokenAuthResolver implements HandlerMethodArgumentResolver {

    private CommonProperties commonProperties;
    private LoginInfoService loginInfoService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ILoginInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        EnumApplication application = commonProperties.getApplication();
        EnumSysRole role = application.getRole();
        String token = request.getHeader(TokenService.tokenKey);
        ILoginInfo loginInfo = new LoginInfo(EnumSysRole.ANONYMOUS, -9999L, null, null);
        if (token == null) {

//            Enumeration<String> headerNames = request.getHeaderNames();
//            while (headerNames.hasMoreElements()) {
//                String headerName = headerNames.nextElement();
//                String headerValue = request.getHeader(headerName);
//                log.info("Header: {} = {}", headerName, headerValue);
//            }

            String origin = request.getHeader("Request-Origion");
            if ("Knife4j".equals(origin)) {
                return new LoginInfo(EnumSysRole.USER, 1L, null, null);
            }

        } else {
//            try {
            loginInfo = loginInfoService.getLoginInfoCache(role, token);
//            } catch (Exception e) {
//                log.error(e.getMessage(), e);
//            }
        }
//        Set<String> permissionPaths = null;
        switch (application) {
            case user:
                // 用户端 已经在 UserCommonService 中鉴权过,暂时不用角色权限鉴权
                break;
            default:
                this.checkAuthRolePermission(request, loginInfo);
                break;
        }
        return loginInfo;
    }

    private void checkAuthRolePermission(HttpServletRequest request, @Parameter(hidden = true) ILoginInfo loginInfo) {
        String requestURI = request.getRequestURI();
        List<String> requestURIs;
        if (requestURI.contains(",")) {
            String prefix = requestURI.substring(0, requestURI.lastIndexOf("/") + 1);
            // 一个接口查询多实体定制
            String entityNames = requestURI.substring(requestURI.lastIndexOf("/") + 1);
            requestURIs = Arrays.stream(entityNames.split(",")).map(p -> prefix + p).collect(Collectors.toList());
        } else {
            requestURIs = List.of(requestURI);
        }
        for (String uri : requestURIs) {
            if (commonProperties.getPublicPaths().stream().noneMatch(uri::contains)) {
                boolean hasPermission = (loginInfo.getPermissionPaths().contains(uri)) || (loginInfo.getPermissionPathsPrefix().stream().anyMatch(uri::startsWith));
                if (!hasPermission) {
                    throw new BusinessException(403, "无权限", uri);
                }
            }
        }
    }
}
