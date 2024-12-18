package com.pro.common.module.api.usermoney.model.modelbase.intf;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import com.pro.framework.api.model.IModel;

/**
 * 数额变化的基础类
 */
public interface IAmountEntity extends IUserClass, IModel {
    Long getUserId();
    void setEntityKey(String key);
    @JsonIgnore
    String getEntityKey();
}
