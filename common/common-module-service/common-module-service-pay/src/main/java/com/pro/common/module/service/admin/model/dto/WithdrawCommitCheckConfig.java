package com.pro.common.module.service.admin.model.dto;

import com.pro.common.modules.api.dependencies.pay.intf.IWithdrawCommitCheckConfig;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WithdrawCommitCheckConfig implements IWithdrawCommitCheckConfig {
    @ApiModelProperty(value = "提现最低金额单次")
    private BigDecimal withdrawMoneyMin;
    @ApiModelProperty(value = "提现最高金额单次")
    private BigDecimal withdrawMoneyMax;
    @ApiModelProperty(value = "提现金额不满足的文章")
    @JTDField(defaultValue = "withdrawMoneyPoster")
    private String withdrawMoneyPoster;

    @ApiModelProperty(value = "提现次数/天")
    @JTDField(defaultValue = "-1")
    private Integer withdrawTimes;
    @ApiModelProperty(value = "免费提款次数/天")
    @JTDField(defaultValue = "-1")
    private Integer withdrawFreeTimes;
    @ApiModelProperty(value = "免费提款次数/月")
    @JTDField(defaultValue = "-1")
    private Integer withdrawFreeMonthTimes;
    @ApiModelProperty(value = "总免费提款次数")
    @JTDField(defaultValue = "-1")
    private Integer withdrawFreeTotalTimes;
    @ApiModelProperty(value = "提现次数不满足的文章")
    @JTDField(defaultValue = "withdrawTimesPoster")
    private String withdrawTimesPoster;

    @ApiModelProperty(value = "超次后提现手续费")
    private BigDecimal withdrawFee;
    @ApiModelProperty(value = "超次后提现固定手续费")
    private BigDecimal withdrawFeeMoney;

}
