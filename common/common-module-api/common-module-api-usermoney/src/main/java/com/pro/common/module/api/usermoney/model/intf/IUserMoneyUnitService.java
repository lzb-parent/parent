package com.pro.common.module.api.usermoney.model.intf;


import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.common.module.api.usermoney.model.db.UserMoneyRecord;
import com.pro.common.module.api.usermoney.model.dto.AmountEntityUnitDTO;
import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;

import java.util.List;
import java.util.Map;

public interface IUserMoneyUnitService<Entity extends UserMoney, Record extends UserMoneyRecord, DTO extends IAmountEntityRecord> {
    Map<String, Entity> listEntity(Long userId);

    List<Record> change(AmountEntityUnitDTO<DTO> dto);
    Record change(EnumAmountNegativeDeal negativeDeal, DTO record);
}
