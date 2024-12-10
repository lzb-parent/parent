package com.pro.common.modules.api.dependencies.service;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;

import java.util.Set;

public interface IAuthRoleService {
    void clearCache(EnumSysRole role, Long loginId);

    Set<String> getPathCache(EnumSysRole role, Long loginId);

    //    @Cacheable(value = CommonConst.CacheKey.AuthRoutePaths, key = "#role+'_'+#loginId")
    Set<String> getRouteCodes(EnumSysRole role, Long loginId);
}
