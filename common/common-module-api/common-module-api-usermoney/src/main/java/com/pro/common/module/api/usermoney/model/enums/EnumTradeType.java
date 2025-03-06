package com.pro.common.module.api.usermoney.model.enums;

import com.pro.framework.api.enums.EnumAmountUpDown;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 交易枚举类
 * 统一用trades()来给用户 充值/扣款
 */
@AllArgsConstructor
@Getter
public enum EnumTradeType implements IEnumTradeType {

    RECHARGE_BACK("101", EnumAmountUpDown.up, "后台充值"),
    RECHARGE_ONLINE("102", EnumAmountUpDown.up, "在线充值"),
    RECHARGE_REGISTER("106", EnumAmountUpDown.up, "注册赠送"),
    RECHARGE_JACKPOT("103", EnumAmountUpDown.up, "充值2"),
    RECHARGE_PRESENT("108", EnumAmountUpDown.up, "充值奖励"),
    RECHARGE_TG_COMMISSION("104", EnumAmountUpDown.up, "充值推广佣金"),
    REGISTER_TG_COMMISSION("105", EnumAmountUpDown.up, "注册推广佣金"),
    WITHDRAW("201", EnumAmountUpDown.down, "提现"),
    WITHDRAW_REJECT("202", EnumAmountUpDown.up, "提现驳回"),
//    COMMISSION_CONVERT("203", EnumAmountUpDown.up, "佣金转化"),
    DEDUCT_HANDLE("301", EnumAmountUpDown.down, "手动扣款"),
    DEDUCT_RECHARGE_MONEY("302", EnumAmountUpDown.down, "充值校正"),
    VIP_UPGRADE("601", EnumAmountUpDown.down, "升级VIP"),
    VIP_TG_COMMISSION("602", EnumAmountUpDown.up, "等级推广佣金"),
    USER_LEVEL_UPGRADE_AWARD("603", EnumAmountUpDown.up, "升级VIP奖励"),

    ;


    // BillNo/orderNo各种No最重要是售后和客服相关处理时用,为了方便分类分工作员管理建议,统一开头方便客服操作
    // 拼接在BillNo前面
    String billNoHead;
    EnumAmountUpDown upDown;
    String label;

    public static final List<EnumTradeType> TYPE_COMMISSION = Arrays.asList(
        EnumTradeType.RECHARGE_TG_COMMISSION,
        EnumTradeType.REGISTER_TG_COMMISSION
    );
    public static final Map<String, EnumTradeType> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumTradeType::name, o -> o));
}
