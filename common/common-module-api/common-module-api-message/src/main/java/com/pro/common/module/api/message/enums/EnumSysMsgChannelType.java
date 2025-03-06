package com.pro.common.module.api.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EnumSysMsgChannelType {

    SITE_MESSAGE("站内"),
    EMAIL("邮件"),
    PHONE_SMS("短信"),
//    XXX("xxx三方通知,比如telegram", null),
    ;
    String label;
    public static Map<String, EnumSysMsgChannelType> MAP = Arrays.stream(EnumSysMsgChannelType.values()).collect(Collectors.toMap(Enum::name, o -> o));
}
