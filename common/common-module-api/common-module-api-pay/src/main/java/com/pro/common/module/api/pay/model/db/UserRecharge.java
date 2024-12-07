package com.pro.common.module.api.pay.model.db;

import com.pro.common.module.api.pay.enums.EnumRechargeState;
import com.pro.common.module.api.pay.enums.EnumRechargeType;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserOrderClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值记录
 */
@Data
@JTDTable(
        sequences = {
                "UNIQUE KEY `uk_no` (`no`)",
                "KEY `idx_type` (`type`)",
                "KEY `idx_userId` (`user_id`)",
        }, entityId = 350
)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecharge extends BaseUserModel implements IUserOrderClass {
    @ApiModelProperty(value = "单号")
    @JTDField(group = "基础信息")
    private String no;
    @ApiModelProperty(value = "账户类型")
    @JTDField(group = "基础信息")
    private String amountType;
    @ApiModelProperty(value = "类型")
    @JTDField(group = "基础信息")//, uiType = JTDConst.EnumFieldUiType.radio
    private EnumRechargeType type;

    @ApiModelProperty(value = "状态")
    @JTDField(group = "基础信息")
    private EnumRechargeState state;
    @ApiModelProperty(value = "后台备注")
    @JTDField(uiType = JTDConst.EnumFieldUiType.textarea, group = "基础信息")
    private String remark;
    @ApiModelProperty("用户端备注")
    @JTDField(uiType = JTDConst.EnumFieldUiType.textarea, group = "基础信息")
    private String userRemark;

    @ApiModelProperty(value = "货币符号")
    @JTDField(group = "资金信息")
    private String coinCode;
    @ApiModelProperty(value = "申请金额")
    @JTDField(group = "资金信息")
    private BigDecimal applyMoney;
    @ApiModelProperty(value = "账变前余额")
    @JTDField(group = "资金信息")
    private BigDecimal beforeMoney;
    @ApiModelProperty(value = "账变金额")
    @JTDField(group = "基础信息")
    private BigDecimal money;
    @ApiModelProperty(value = "账变后余额")
    @JTDField(group = "资金信息")
    private BigDecimal afterMoney;
    @ApiModelProperty(value = "支付时间")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, group = "资金信息")
    private LocalDateTime payTime;

    @ApiModelProperty(value = "付款凭证")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image, group = "提交资料")
    private String pic;
    @ApiModelProperty(value = "付款银行名称")
    @JTDField(group = "提交资料")
    private String bankName;
    @ApiModelProperty(value = "付款人")
    @JTDField(group = "提交资料")
    private String bankUsername;
    @ApiModelProperty(value = "付款账号")
    @JTDField(group = "提交资料")
    private String bankAccount;
    @ApiModelProperty(value = "三方支付单号")
    @JTDField(group = "提交资料")
    private String merchantNo;
    @ApiModelProperty(value = "手机号")
    @JTDField(group = "提交资料")
    private String phone;



    @ApiModelProperty(value = "支付渠道")
    @JTDField(group = "通道信息", notNull = JTDConst.EnumFieldNullType.can_null, entityClass = PayChannel.class, entityClassKey = "id")
    private Long channelId;
    @ApiModelProperty(value = "支付方式")
    @JTDField(group = "通道信息")
    private String payType;
    @ApiModelProperty(value = "渠道代收手续费百分比")
    @JTDField(group = "通道信息")
    private BigDecimal channelFeeRate;
    @ApiModelProperty(value = "渠道代收手续费")
    @JTDField(group = "通道信息")
    private BigDecimal channelFee;
    @ApiModelProperty(value = "汇率")//：1平台币=*通道币
    @JTDField(group = "通道信息", defaultValue = "1")
    private BigDecimal exchangeRate;
    @ApiModelProperty(value = "商户编号")
    @JTDField(group = "通道信息", entityClass = PayMerchant.class)
    private String merchantCode;
    @ApiModelProperty(value = "商户名称")
    @JTDField(group = "通道信息")
    private String merchantName;


//    @ApiModelProperty(value = "优惠券码")
//    private String couponNo;

//    @ApiModelProperty(value = "第几单")
//    private Integer times;

    //    @ApiModelProperty("错误信息")
//    private String rejectReason;
//    @ApiModelProperty(value = "扣款金额")
//    private BigDecimal deductMoney;
    // 签名
    private transient String sign;

}
