package com.pro.common.web.security.component;

import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.model.LoginInfo;
import com.pro.common.web.security.service.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class TokenAuthResolver implements HandlerMethodArgumentResolver {

    private IAuthRoleService authRouteService;
    private TokenService tokenService;
    private CommonProperties commonProperties;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return ILoginInfo.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        EnumApplication application = commonProperties.getApplication();
        EnumSysRole role = application.getRole();
        Long loginId = tokenService.getIdByToken(request.getHeader(TokenService.tokenKey));
        Set<String> permissionPaths = null;
        switch (application) {
            case user:
                // 用户端 已经在 UserCommonService 中鉴权过,暂时不用角色权限鉴权
                break;
            default:
                permissionPaths = this.checkAuthRolePermission(request, role, loginId);
                break;
        }
        //        ThreadLocalUtil.setLoginInfo(loginInfo);
        return new LoginInfo(role, loginId, permissionPaths);
    }

    private Set<String> checkAuthRolePermission(HttpServletRequest request, EnumSysRole role, Long loginId) {
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
        Set<String> permissionPaths = null;
        Set<String> permissionPathsPrefix = null;
        for (String uri : requestURIs) {
            if (commonProperties.getPublicPaths().stream().noneMatch(uri::contains)) {
                if (null != loginId && permissionPaths == null) {
                    permissionPaths = authRouteService.getPathCache(role, loginId);
                    permissionPathsPrefix = permissionPaths.stream().filter(p -> p.endsWith("*")).map(u->u.replaceAll("\\*+$", "")).collect(Collectors.toSet());
                    permissionPaths = permissionPaths.stream().filter(p -> !p.endsWith("*")).collect(Collectors.toSet());
                }
                boolean hasPermission = (null != permissionPaths && permissionPaths.contains(uri)) || (null != permissionPathsPrefix && permissionPathsPrefix.stream().anyMatch(uri::startsWith));
                if (!hasPermission) {
                    throw new BusinessException(403, "无权限", uri);
                }
            }
        }
        return permissionPaths;
    }
}
