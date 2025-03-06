package com.pro.common.module.service.message.util;

import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EnumSmsCodeType {

    YUN_PIAN("云片短信", EnumSysMsgChannel.SMS_YUN_PIAN),
    NODE("节点短信", EnumSysMsgChannel.SMS_NODE),
    XIAO_CHUANG("小船出海短信", EnumSysMsgChannel.SMS_BUKA),
    EMAIL("谷歌邮件", EnumSysMsgChannel.EMAIL_GOOGLE),
    EMAIL_SUBMAIL("SUBMAIL邮件", EnumSysMsgChannel.Email_Submail),
//    EMAIL_SENDINBLUE("SENDINBLUE邮件", EnumSysMsgChannel.Email_Sendinblue),
//    EMAIL_AWS("AWS邮件", EnumSysMsgChannel.Email_Aws),
    none("无", null),
    ;

    String label;
    EnumSysMsgChannel channel;
    public static final Map<String, EnumSmsCodeType> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumSmsCodeType::name, o -> o));

}
