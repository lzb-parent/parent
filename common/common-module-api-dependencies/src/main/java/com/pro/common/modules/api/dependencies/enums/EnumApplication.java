package com.pro.common.modules.api.dependencies.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 不同系统端口
 */
@Getter
@AllArgsConstructor
public enum EnumApplication implements IEnum {
    user("用户端", EnumSysRole.USER),
    agent("代理端", EnumSysRole.AGENT),
    admin("后台管理端",  EnumSysRole.ADMIN),
    pay("支付端",  EnumSysRole.ADMIN),
    ;

    final String label;
    final EnumSysRole role;
//    public static final Map<Integer, EnumSystem> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumSystem::name, o -> o));
}
