package com.pro.common.module.api.usermoney.model.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待结算订单状态
 */
@AllArgsConstructor
@Getter
public enum EnumUserMoneyWaitState implements IEnum {

    WAIT("待结算"),
    SUCCESS("已结算"),
    CANCEL("已取消"),
    ;

    private String label;

}
