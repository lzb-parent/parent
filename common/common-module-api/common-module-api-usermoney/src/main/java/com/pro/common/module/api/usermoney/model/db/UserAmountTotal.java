package com.pro.common.module.api.usermoney.model.db;

import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 用户数额总计 (一个用户只能有一个UserAmountTotal,一个用户可能有多个userMoney)
 */
//@Schema("用户数额总计")
@Data
public class UserAmountTotal extends BaseUserModel implements IUserRecordClass, IEnumToDbDbId {
//    @Schema(description = "更新版本")
//    private Integer version;
    @Schema(description = "今日佣金金额")
    private BigDecimal todayCommissionMoney;
    @Schema(description = "总佣金金额")
    private BigDecimal commissionMoney;

    @Schema(description = "今日充值金额")
    private BigDecimal todayRechargeMoney;
    @Schema(description = "累计充值金额")
    private BigDecimal totalRechargeMoney;
    @Schema(description = "累计充值次数")
    private Integer totalRechargeTimes;

    @Schema(description = "今日提款金额")
    private BigDecimal todayTkMoney;
    @Schema(description = "累计提款金额")
    private BigDecimal totalTkMoney;
    @Schema(description = "今日提款次数")
    private Integer todayTkTimes;
    @Schema(description = "累计提款次数")
    private Integer totalTkTimes;
}
