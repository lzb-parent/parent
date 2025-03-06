package com.pro.common.modules.api.dependencies.enums;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统角色
 */
@Getter
@AllArgsConstructor
public enum EnumSysRole implements IEnum {
    ANONYMOUS("匿名", 1, CommonConst.Bean.userService),
    USER("用户", 2, CommonConst.Bean.userService),
    AGENT("代理", 3, CommonConst.Bean.agentService),
    ADMIN("管理员", 4, CommonConst.Bean.adminService),
//    superAdmin("超级管理员", 5),
    ;

    final String label;
    final  Integer id;
    final String serviceBean;
//    public static final Map<Integer, EnumSysRole> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumSysRole::getId, o -> o));
}
