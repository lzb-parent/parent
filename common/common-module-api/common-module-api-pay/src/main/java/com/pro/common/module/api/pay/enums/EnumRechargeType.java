package com.pro.common.module.api.pay.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 充值类型
 */
@AllArgsConstructor
@Getter
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EnumRechargeType implements IEnum {

    RECHARGE_BACK("后台充值"),
    RECHARGE_ONLINE("在线充值"),
    RECHARGE_JACKPOT("充值2"),
    RECHARGE_REGISTER("注册赠送"),
    RECHARGE_PRESENT("充值奖励"),
    DEDUCT_HANDLE("手动扣款"),
    ;

    private String label;
    public static final Map<String, EnumRechargeType> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumRechargeType::name, o -> o));

}
