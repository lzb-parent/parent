package com.pro.common.module.api.usermoney.model.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 待结算订单状态
 */
@AllArgsConstructor
@Getter
public enum EnumUserMoneyWaitType implements IEnum {

    FINANCE_PROFIT("理财每日收益"),
    FINANCE_PROFIT_COMMISSION("理财每日收益佣金"),
    ;

    private String label;

}
