package com.pro.common.module.api.usermoney.model.modelbase;

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
import java.util.List;

/**
 * 数额变化的基础类
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AmountEntityRecord extends BaseUserModel implements IAmountEntityRecord {
    @ApiModelProperty("变化前数额")
    @JTDField(uiType = JTDConst.EnumFieldUiType.hide)
    private BigDecimal amountBefore;
    @ApiModelProperty("变化后数额")
    private BigDecimal amountAfter;


    @ApiModelProperty("数额类型")
    @JTDField(defaultValue = FrameworkConst.Str.DEFAULT,uiType = JTDConst.EnumFieldUiType.hide)
    private String amountType;

    @ApiModelProperty("增减")
    private EnumAmountUpDown upDown;

    @ApiModelProperty("变化数额")//正数
    private BigDecimal amount;

    // 例如 LotteryBuyOrder
    @ApiModelProperty("变动类型")
    private String recordType;
    @ApiModelProperty("相关订单编号")
    private String orderNo;
    @ApiModelProperty("相关订单Id")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long orderId;
    @ApiModelProperty("订单来源用户名")
    private String orderUsername;


    @ApiModelProperty("内部备注")
    private String remark;
    @ApiModelProperty("用户端备注")
    private String userRemark;


    @Override
    public String getEntityKey() {
        return getAmountType() + FrameworkConst.Str.split_pound + getUserId();
    }


    @ApiModelProperty("过滤订单Ids")
    transient private List<Long> orderIds;
}

