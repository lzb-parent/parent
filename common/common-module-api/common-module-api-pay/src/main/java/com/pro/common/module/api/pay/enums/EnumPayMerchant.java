package com.pro.common.module.api.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumPayMerchant {
    SYSTEMPAY("线下支付"),
    QEPAYNEW("qe new"),
    ;

    final String label;

}
