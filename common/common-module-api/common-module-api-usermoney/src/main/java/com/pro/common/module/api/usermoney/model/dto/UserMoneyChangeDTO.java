package com.pro.common.module.api.usermoney.model.dto;

import com.pro.common.module.api.usermoney.model.enums.IEnumTradeType;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;
import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.enums.EnumAmountUpDown;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UserMoneyChangeDTO extends BaseUserModel implements IAmountEntityRecord {
    @ApiModelProperty("用户Id")
    private Long userId;
    @ApiModelProperty("变动类型")
    @JTDField(defaultValue = FrameworkConst.Str.DEFAULT)
    private IEnumTradeType tradeType;
    @ApiModelProperty("数额类型")
    @JTDField(defaultValue = FrameworkConst.Str.DEFAULT)
    private String amountType = FrameworkConst.Str.DEFAULT;
    @ApiModelProperty("变化数额")//正数
    private BigDecimal amount;
    @ApiModelProperty("内部备注")
    private String remark;
    @ApiModelProperty("用户端备注")
    private String userRemark;
    @ApiModelProperty("相关订单编号")
    private String orderNo;
    @ApiModelProperty("相关订单Id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long orderId;

    public UserMoneyChangeDTO(Long userId, IEnumTradeType tradeType, BigDecimal amount, String remark, String userRemark, String orderNo, Long orderId) {
        this.userId = userId;
        this.tradeType = tradeType;
        this.amountType = FrameworkConst.Str.DEFAULT;
        this.amount = amount;
        this.remark = remark;
        this.userRemark = userRemark;
        this.orderNo = orderNo;
        this.orderId = orderId;
    }

    @Override
    public String getEntityKey() {
        return getAmountType() + FrameworkConst.Str.split_pound + getUserId();
    }

    @Override
    public EnumAmountUpDown getUpDown() {
        return getTradeType().getUpDown();
    }

    @Override
    public String getRecordType() {
        return getTradeType().name();
    }
}
