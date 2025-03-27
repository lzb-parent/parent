package com.pro.common.module.service.common.service;

import com.pro.common.module.api.common.model.db.AuthRole;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.ADMIN;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.AGENT;

/**
 * 路由配置 基础
 * userMoneyWait
 * userMoneyWaitRecord
 * posterCode
 * authRoleRoute
 * sysMultiClassRelation
 * 暂时不做路由
 */
@Getter
@AllArgsConstructor
public enum EnumAuthRole implements IEnumToDbEnum<AuthRole> {
    _1001(ADMIN,true,"superAdmin","",true,null),
    _2001(AGENT,true,"superAgent","",true,null),
    ;
    private EnumSysRole sysRole;
    private Boolean superFlag;
    private String name;
    private String routeCodes;
    private Boolean enabled;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
