package com.pro.common.module.api.pay.model.db;

import com.pro.common.module.api.pay.enums.EnumTransferState;
import com.pro.common.module.api.pay.enums.EnumWithdrawStatus;
import com.pro.common.modules.api.dependencies.model.classes.IUserOrderClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 转账记录
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserTransfer对象", description = "代付记录")
@NoArgsConstructor
@AllArgsConstructor
//@SuperBuilder
@JTDTable(
        sequences = {
                "UNIQUE KEY `uk_no` (`no`)",
                "KEY `idx_userId` (`user_id`)",
        }, entityId = 351
)
public class UserTransfer extends UserWithdraw implements IUserOrderClass {
    //    @ApiModelProperty(value = "商户Id")
//    private String merchantId;
//    @ApiModelProperty(value = "商户名称")
//    private String merchantName;
    @ApiModelProperty(value = "商户编号")
    @JTDField(group = "代付信息", entityClass = PayMerchant.class, uiType = JTDConst.EnumFieldUiType.hide)
    private String merchantCode;
    @ApiModelProperty(value = "是否是线下支付")
    @JTDField(group = "代付信息", notNull = JTDConst.EnumFieldNullType.can_null, disabled = true)
    private Boolean offlineFlag;

    @ApiModelProperty(value = "提款订单Id")
    @JTDField(group = "代付信息")
    private Long userWithdrawId;
    @ApiModelProperty(value = "代付渠道")
    @JTDField(group = "代付信息", entityClass = PayoutChannel.class, entityClassKey = "id")
    private Long payoutChannelId;
    @ApiModelProperty(value = "代付渠道名称")
    @JTDField(group = "代付信息", entityClass = PayoutChannel.class, entityClassKey = "id", entityClassTargetProp = "name")
    private String payoutChannelName;
    @ApiModelProperty(value = "代付银行")
    @JTDField(group = "代付信息", entityClass = PayBank.class)
    private String bankCode;
    @ApiModelProperty(value = "代付银行")
    @JTDField(group = "代付信息", entityClass = PayBank.class, entityClassTargetProp = "name")
    private String bankName;
    @ApiModelProperty(value = "代付三方返回单号")
    @JTDField(group = "代付信息", disabled = true)
    private String payNo;
    @ApiModelProperty(value = "代付状态")
    @JTDField(group = "代付信息", disabled = true)
    private EnumTransferState state;
    @ApiModelProperty(value = "代付失败信息")
    @JTDField(group = "代付信息", disabled = true)
    private String errorMessage;
    @ApiModelProperty(value = "内部描述")
    @JTDField(uiType = JTDConst.EnumFieldUiType.textarea)
    private String remark;

//    @ApiModelProperty("提现状态")
//    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.none)
//    private EnumWithdrawStatus status;
}
