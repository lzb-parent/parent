package com.pro.common.module.api.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTransferState {
    SUBMITTED("已提交"),
    SUCCESS("代付成功"),
    FAILED("代付失败"),
    ;

    String label;
}
