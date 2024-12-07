package com.pro.common.module.api.message.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 短信发送类型
 */
@AllArgsConstructor
@Getter
public enum EnumSysMsgBusinessCode implements IEnum {



    REGISTER_CODE("注册验证码"),
    FORGET_PWD_CODE("忘记密码验证码"),
    FORGET_TK_PWD_CODE("忘记提款密码验证码"),

    REGISTER_SUCCESS("注册成功"),
    RECHARGE_SUCCESS("充值成功"),
    WITHDRAW_SUCCESS("提现成功"),
    WITHDRAW_FAIL("提现失败"),
    FINANCE_BUY_SUCCESS("理财购买成功"),
    FINANCE_END_SUCCESS("理财项目到期"),
    OTHERS("营销类通知消息"),
    ;
    String label;
    public static final Map<String, EnumSysMsgBusinessCode> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumSysMsgBusinessCode::name, o -> o));
}
