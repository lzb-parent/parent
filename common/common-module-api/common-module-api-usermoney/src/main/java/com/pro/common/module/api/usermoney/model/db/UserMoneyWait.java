package com.pro.common.module.api.usermoney.model.db;

import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityWithType;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 会员资金表
 *
 * @author admin
 */
@Data
@ApiModel(value = "", description = "资金待结算总计")
@NoArgsConstructor
//@AllArgsConstructor
@SuperBuilder
@JTDTable(
        sequences = {
                "UNIQUE KEY `uk_userId_type` (`user_id`,`amount_type`)",
        }, entityId = 347
)
public class UserMoneyWait extends AmountEntityWithType implements IUserRecordClass {
}
