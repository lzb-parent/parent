package com.pro.common.module.service.admin.enums;

import com.pro.common.module.api.common.model.enums.EnumAuthRole;
import com.pro.common.module.service.admin.model.db.Admin;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
public enum EnumAdmin implements IEnumToDbEnum<Admin> {
    _1(
            1L,
            "admin"
            , "AAC1CB3291207102C7B06445D957A5F3"
            , true
            , EnumAuthRole._1001.name().substring(1)
            , null
    ),
    _2(
            2L,
            "developer"
            , "AAC1CB3291207102C7B06445D957A5F3"
            , true
            , EnumAuthRole._1001.name().substring(1)
            , null
    ),


    ;
    private final Long id;
    private final String username;
    private final String password;
    private final Boolean superFlag;
    private final String roleIds;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
