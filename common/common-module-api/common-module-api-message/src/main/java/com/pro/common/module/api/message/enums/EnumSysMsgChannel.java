package com.pro.common.module.api.message.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum EnumSysMsgChannel {

    SMS_YUN_PIAN("短信_云片", "sysMsgChannelServiceSmsYunPian", ""),
    SMS_NODE("短信_节点", "sysMsgChannelServiceSmsJieDian", ""),
    SMS_XIAO_CHUANG("短信_小船", "sysMsgChannelServiceSmsXiaoChuan", "http://boatsms.com/"),
    SMS_BUKA("短信_不卡", "sysMsgChannelServiceSmsBuKa", "https://my.onbuka.com/home/home"),
    SMS_OTP("短信_otp", "sysMsgChannelServiceSmsOtp", "http://190.92.241.43:9356/sms_cloud"),

    EMAIL_GOOGLE("邮件_谷歌", "sysMsgChannelServiceEmailGoogle", "https://google.com/"),
//    Email_Aws("邮件_Aws", "sysMsgChannelServiceEmailAws", ""),
//    Email_Sendinblue("邮件_Sendinblue", "sysMsgChannelServiceEmailSendinblue", ""),
    Email_Submail("邮件_Submail", "sysMsgChannelServiceEmailSubmail", ""),

    SITE_MESSAGE_DEFAULT("站内信", "sysMsgChannelServiceSiteMessage", "https://my.onbuka.com/home/home"),
    ;
    String label;
    String sendBeanName;
    String officialSiteInfos;
    public static Map<String, EnumSysMsgChannel> map = Arrays.stream(EnumSysMsgChannel.values()).collect(Collectors.toMap(Enum::name, o -> o));
}
