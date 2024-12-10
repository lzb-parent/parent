package com.pro.common.module.api.usermoney.controller;

import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.common.module.api.usermoney.model.db.UserMoneyRecord;
import com.pro.common.module.api.usermoney.model.dto.UserMoneyChangeDTO;
import com.pro.common.module.api.usermoney.service.UserMoneyUnitService;

public abstract class UserMoneyController<Entity extends UserMoney, Record extends UserMoneyRecord, DTO extends UserMoneyChangeDTO, UnitService extends UserMoneyUnitService<Entity, Record, DTO>>
        extends AmountEntityBaseController<Entity, Record, DTO, UnitService> {
}
