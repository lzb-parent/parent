package com.pro.common.module.api.usermoney.model.enums;

import com.pro.framework.api.enums.EnumAmountUpDown;
import com.pro.framework.api.enums.IEnum;

/**
 * 字典接口
 * 可用来界面展示
 */
public interface IEnumTradeType extends IEnum {

    String getBillNoHead();
    EnumAmountUpDown getUpDown();
}
