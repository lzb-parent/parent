package com.pro.common.module.api.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumRechargeState {
    UNPAID("未支付"),
    PAID("已支付"),
    FAILED("支付失败"),
    ;

    String label;

}
