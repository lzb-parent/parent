package com.pro.common.module.api.usermoney.model.modelbase.intf;

import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import com.pro.framework.api.enums.EnumAmountUpDown;
import com.pro.framework.api.model.IModel;

import java.math.BigDecimal;

public interface IAmountEntityRecord extends IUserClass, IModel {
    Long getUserId();

    String getEntityKey();

    EnumAmountUpDown getUpDown();

    BigDecimal getAmount();

    String getRecordType();
}
