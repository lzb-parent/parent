package com.pro.common.module.api.usermoney.model.modelbase.intf;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 数额变化的基础类
 */
public interface IAmountEntity {
    Long getUserId();
    void setEntityKey(String key);
    @JsonIgnore
    String getEntityKey();
}
