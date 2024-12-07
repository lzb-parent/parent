package com.pro.common.module.api.usermoney.model.db;

import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户数额总计 (一个用户只能有一个UserAmountTotal,一个用户可能有多个userMoney)
 */
//@ApiModel("用户数额总计")
@Data
public class UserAmountTotal extends BaseUserModel implements IUserRecordClass, IEnumToDbDbId {
//    @ApiModelProperty("更新版本")
//    private Integer version;
    @ApiModelProperty("今日佣金金额")
    private BigDecimal todayCommissionMoney;
    @ApiModelProperty("总佣金金额")
    private BigDecimal commissionMoney;

    @ApiModelProperty(value = "今日充值金额")
    private BigDecimal todayRechargeMoney;
    @ApiModelProperty(value = "累计充值金额")
    private BigDecimal totalRechargeMoney;
    @ApiModelProperty(value = "累计充值次数")
    private Integer totalRechargeTimes;

    @ApiModelProperty(value = "今日提款金额")
    private BigDecimal todayTkMoney;
    @ApiModelProperty(value = "累计提款金额")
    private BigDecimal totalTkMoney;
    @ApiModelProperty(value = "今日提款次数")
    private Integer todayTkTimes;
    @ApiModelProperty(value = "累计提款次数")
    private Integer totalTkTimes;
}
