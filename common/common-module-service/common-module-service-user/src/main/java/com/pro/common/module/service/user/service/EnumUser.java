package com.pro.common.module.service.user.service;

import com.pro.common.module.api.user.model.db.User;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
public enum EnumUser implements IEnumToDbEnum<User> {
    // 密码123456
    _1(1L,"13800000000", "AAC1CB3291207102C7B06445D957A5F3", "", true, true, "13800000000", 1L, "888888", 1L, null),
    ;
    private final Long id;
    private final String username;
    private final String password;
    private final String tkPassword;
    private final Boolean enabled;
    private final Boolean isDemo;
    private final String nickName;
    private final Long levelId;
    private final String inviteCode;
    private final Long agentId;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
