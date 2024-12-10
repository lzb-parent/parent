package com.pro.common.module.api.user.enums;


import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户名来源方式
 */
@AllArgsConstructor
@Getter
public enum EnumRegisterUsernameFrom implements IEnum {
    INPUT("取自用户名输入框"),
    PHONE("取自手机号"),
    EMAIL("取自邮箱"),
    ;
    final String label;
    public static final Map<String, EnumRegisterUsernameFrom> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumRegisterUsernameFrom::name, o -> o));

}
