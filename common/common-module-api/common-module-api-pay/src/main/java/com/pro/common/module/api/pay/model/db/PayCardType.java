package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.common.modules.api.dependencies.pay.intf.IWithdrawCommitCheckConfig;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 支付渠道
 *
 * @author admin
 */
@Data
@ApiModel(description = "站内卡号类型")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code` (`code`)"
}, entityId = 111, module = "pay")
public class PayCardType extends BaseModel implements IConfigClass, IWithdrawCommitCheckConfig, IEnumToDbDbId {
    // 基础信息
    @ApiModelProperty(value = "编号")
    @JTDField(defaultValue = "BANK") // 为了保证其他简单平台无需对 CardType 进行额外管理
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "LOGO")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String logo;
    @ApiModelProperty(value = "LOGO旁名称")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String logoName;
    @ApiModelProperty(value = "背景图")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String background;
    @ApiModelProperty(value = "排序")
    @JTDField(defaultValue = PayCardType.DEFAULT_SORT)
    private Integer sort;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    @ApiModelProperty(value = "支付金额可选项")
    @JTDField(description = "若多个,英文逗号分隔")
    private String amountOptions;
    @ApiModelProperty(value = "主题颜色", notes = "例如 blue 或 green")
    private String theme;

    //充值
    @ApiModelProperty(value = "充值_总开关")
    @JTDField(group = "recharge", defaultValue = "1")
    private Boolean rechargeFlag;
    @ApiModelProperty(value = "充值_默认金额")
    @JTDField(group = "recharge")
    private String rechargeDefaultAmount;
    @ApiModelProperty(value = "充值_描述文章")
    @JTDField(group = "recharge")
    private String rechargePosters;
    @ApiModelProperty(value = "充值_线下提交处描述多文章")
    @JTDField(group = "recharge", mainLength = 400)
    private String rechargeOfflineFormPosters;


    //银行卡管理
    @ApiModelProperty(value = "银行卡_总开关")
    @JTDField(group = "withdraw", defaultValue = "1")
    private Boolean userBankCardFlag;
    @ApiModelProperty(value = "银行卡_用户端是否显示")
    @JTDField(group = "withdraw", defaultValue = "1")
    private Boolean userBankCardUserFlag;
    @ApiModelProperty(value = "银行卡_开启最低余额")
    @JTDField(group = "withdraw")
    private BigDecimal bankCardEnableMinBalance;
    @ApiModelProperty(value = "银行卡_描述文章")
    @JTDField(group = "withdraw")
    private String userBankCardPosters;

    //提现
    @ApiModelProperty(value = "提款_描述文章")
    @JTDField(group = "withdraw")
    private String withdrawPosters;
    @ApiModelProperty(value = "提现最低金额单次")
    @JTDField(defaultValue = "-1", group = "withdraw")
    private BigDecimal withdrawMoneyMin;
    @ApiModelProperty(value = "提现最高金额单次")
    @JTDField(defaultValue = "-1", group = "withdraw")
    private BigDecimal withdrawMoneyMax;
    @ApiModelProperty(value = "提现金额不满足的文章")
    @JTDField(defaultValue = "withdrawMoneyPoster", group = "withdraw")
    private String withdrawMoneyPoster;
    @ApiModelProperty(value = "提现次数每天")
    @JTDField(defaultValue = "-1", group = "withdraw")
    private Integer withdrawTimes;
    @ApiModelProperty(value = "免费提款次数每天")
    @JTDField(defaultValue = "-1", group = "withdraw")
    private Integer withdrawFreeTimes;
    @ApiModelProperty(value = "免费提款次数每月")
    @JTDField(defaultValue = "-1", group = "withdraw")
    private Integer withdrawFreeMonthTimes;
    @ApiModelProperty(value = "总免费提款次数")
    @JTDField(defaultValue = "-1", group = "withdraw")
    private Integer withdrawFreeTotalTimes;
    @ApiModelProperty(value = "提现次数不满足的文章")
    @JTDField(defaultValue = "withdrawTimesPoster", group = "withdraw")
    private String withdrawTimesPoster;
    @ApiModelProperty(value = "超次后提现手续费")
    @JTDField(group = "withdraw")
    private BigDecimal withdrawFee;
    @ApiModelProperty(value = "超次后提现固定手续费")
    @JTDField(group = "withdraw")
    private BigDecimal withdrawFeeMoney;

    public static final String DEFAULT_SORT = "1000";
}
