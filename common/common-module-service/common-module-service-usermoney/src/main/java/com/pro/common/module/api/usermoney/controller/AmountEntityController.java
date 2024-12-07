package com.pro.common.module.api.usermoney.controller;

import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecordBaseDTO;
import com.pro.common.module.api.usermoney.service.AmountEntityRecordService;
import com.pro.common.module.api.usermoney.service.AmountEntityService;
import com.pro.common.module.api.usermoney.service.AmountEntityUnitServiceImpl;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

//@Api(tags = "数额变化")
//@RestController
//@RequestMapping("/amountEntity")
@Getter
public abstract class AmountEntityController extends AmountEntityBaseController<AmountEntity, AmountEntityRecord, AmountEntityRecordBaseDTO, AmountEntityUnitServiceImpl>{
    @Autowired
    private AmountEntityUnitServiceImpl amountEntityUnitService;
    @Autowired
    private AmountEntityService amountEntityService;
    @Autowired
    private AmountEntityRecordService amountEntityRecordService;
}
