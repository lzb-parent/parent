package com.pro.common.module.api.message.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 短信发送状态
 */
@AllArgsConstructor
@Getter
public enum EnumSysMsgState implements IEnum {

    SUBMITTED("已提交"),
    HAS_BEEN_SENT("已发送"),
    FAILED_TO_SEND("发送失败"),
    USED("已使用"),
    EXPIRED("已过期")

    ;
    String label;
}
