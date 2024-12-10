package com.pro.common.module.api.pay.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumWithdrawStatus implements IEnum {

    CHECKING(1, "待审核", "blue"),
    TRANSFERRING(2, "转账中", "yellow"),
    PASS(3, "已确认", "green"),
    REJECT(4, "已驳回", "gray"),
    ;

    private Integer status;
    private String label;
    private String color;
}
