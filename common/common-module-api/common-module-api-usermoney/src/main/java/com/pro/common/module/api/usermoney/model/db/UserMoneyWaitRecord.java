package com.pro.common.module.api.usermoney.model.db;

import com.pro.common.module.api.usermoney.model.enums.EnumUserMoneyWaitState;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * 会员资金表
 *
 * @author admin
 */
@Data
@ApiModel(description = "资金待结算记录")
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JTDTable(
        entityId = 348
)
public class UserMoneyWaitRecord extends AmountEntityRecord implements IUserRecordClass {
    @ApiModelProperty("状态")
    private EnumUserMoneyWaitState state;
    @ApiModelProperty("状态更变时间")
    private LocalDateTime stateTime;
    @ApiModelProperty("下次状态更变时间")
    private LocalDateTime nextStateTime;
}
