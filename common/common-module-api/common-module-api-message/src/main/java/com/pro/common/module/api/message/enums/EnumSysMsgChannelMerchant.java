package com.pro.common.module.api.message.enums;

import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.module.api.message.enums.EnumSysMsgChannel.*;
import static com.pro.common.module.api.message.enums.EnumSysMsgChannelType.*;

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
public enum EnumSysMsgChannelMerchant implements IEnumToDbEnum<SysMsgChannelMerchant> {
    _1(SITE_MESSAGE, SITE_MESSAGE_DEFAULT, "", "", "", "", null, true,null, null),
    _2(EMAIL, EMAIL_GOOGLE, "", "xiaoz@gmail.com", "xxxxxxxxx", "", null, true, "{\"protocol\":\"smtp\", \"host\":\"smtp.gmail.com\", \"port\":\"587\"}",null),
    _3(PHONE_SMS, SMS_BUKA, "https://api.onbuka.com/v3", "xxxx", "xxxx", "", null, true, null,null),
    ;
    private EnumSysMsgChannelType channelType;
    private EnumSysMsgChannel channel;
    private String baseUrl;
    private String apiKey;
    private String apiPwd;
    private String remark;
    private Integer sort;
    private Boolean enabled;
    private String otherJson;

    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
