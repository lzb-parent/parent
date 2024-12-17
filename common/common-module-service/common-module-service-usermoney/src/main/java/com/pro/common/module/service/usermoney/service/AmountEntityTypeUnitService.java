package com.pro.common.module.service.usermoney.service;

import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityWithType;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;
import com.pro.framework.api.util.CollUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数额变化的基础类
 */
//@Service
@Slf4j
public abstract class AmountEntityTypeUnitService<Entity extends AmountEntityWithType, Record extends AmountEntityRecord, DTO extends IAmountEntityRecord> extends AmountEntityUnitService<Entity, Record, DTO> {
    public Map<String, Entity> listEntity(Long userId) {
        List<Entity> list = this.listEntity(Collections.singleton(userId), false);
        return CollUtils.listToMap(list, Entity::getAmountType);
    }
}
