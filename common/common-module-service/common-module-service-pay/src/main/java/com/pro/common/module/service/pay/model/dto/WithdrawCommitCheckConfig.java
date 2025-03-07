package com.pro.common.module.service.pay.model.dto;

import com.pro.common.modules.api.dependencies.pay.intf.IWithdrawCommitCheckConfig;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawCommitCheckConfig implements IWithdrawCommitCheckConfig {
    @Schema(description = "提现最低金额单次")
    private BigDecimal withdrawMoneyMin;
    @Schema(description = "提现最高金额单次")
    private BigDecimal withdrawMoneyMax;
    @Schema(description = "提现金额不满足的文章")
    @JTDField(defaultValue = "withdrawMoneyPoster")
    private String withdrawMoneyPoster;

    @Schema(description = "提现次数/天")
    @JTDField(defaultValue = "-1")
    private Integer withdrawTimes;
    @Schema(description = "免费提款次数/天")
    @JTDField(defaultValue = "-1")
    private Integer withdrawFreeTimes;
    @Schema(description = "免费提款次数/月")
    @JTDField(defaultValue = "-1")
    private Integer withdrawFreeMonthTimes;
    @Schema(description = "总免费提款次数")
    @JTDField(defaultValue = "-1")
    private Integer withdrawFreeTotalTimes;
    @Schema(description = "提现次数不满足的文章")
    @JTDField(defaultValue = "withdrawTimesPoster")
    private String withdrawTimesPoster;

    @Schema(description = "超次后提现手续费")
    private BigDecimal withdrawFee;
    @Schema(description = "超次后提现固定手续费")
    private BigDecimal withdrawFeeMoney;

}
