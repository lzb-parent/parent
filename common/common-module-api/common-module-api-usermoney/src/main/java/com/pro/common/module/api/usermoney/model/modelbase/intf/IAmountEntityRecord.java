package com.pro.common.module.api.usermoney.model.modelbase.intf;

import com.pro.framework.api.enums.EnumAmountUpDown;

import java.math.BigDecimal;

public interface IAmountEntityRecord {
    Long getUserId();

    String getEntityKey();

    EnumAmountUpDown getUpDown();

    BigDecimal getAmount();

    String getRecordType();
}
