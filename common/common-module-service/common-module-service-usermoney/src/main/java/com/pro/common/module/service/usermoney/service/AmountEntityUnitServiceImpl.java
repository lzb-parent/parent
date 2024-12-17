package com.pro.common.module.service.usermoney.service;

import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecordBaseDTO;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 数额变化的基础类
 */
//@Service
@Getter
public abstract class AmountEntityUnitServiceImpl extends AmountEntityUnitService<AmountEntity, AmountEntityRecord, AmountEntityRecordBaseDTO> {
    @Autowired
    private AmountEntityService amountEntityService;
    @Autowired
    private AmountEntityRecordService amountEntityRecordService;
}
