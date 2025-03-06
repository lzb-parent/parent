package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.LoginInfo;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LoginInfoService {
    @Autowired
    private IAuthRoleService authRouteService;
    @Autowired
    private TokenService tokenService;

//    @CacheEvict(value = CommonConst.CacheKey.LoginInfo, key = "#role+'_'+#loginId")
//    public void clearCache(EnumSysRole role, String token) {
//
//    }

    @Cacheable(value = CommonConst.CacheKey.LoginInfo, key = "#token")
    public ILoginInfo getLoginInfoCache(EnumSysRole role, String token) {
        Long loginId = tokenService.getIdByToken(token);
        Set<String> permissionPaths = null;
        Set<String> permissionPathsPrefix = null;
        if (null != loginId) {
            permissionPaths = authRouteService.getPath(role, loginId);
            permissionPathsPrefix = permissionPaths.stream().filter(p -> p.endsWith("*")).map(u -> u.replaceAll("\\*+$", "")).collect(Collectors.toSet());
            permissionPaths = permissionPaths.stream().filter(p -> !p.endsWith("*")).collect(Collectors.toSet());
        }
        return new LoginInfo(role, loginId, permissionPaths, permissionPathsPrefix);
    }
}
