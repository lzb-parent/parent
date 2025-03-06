package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 代付渠道
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "代付渠道")
@JTDTable(entityId = 323)
public class PayoutChannel extends BaseModel implements IConfigClass {

    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty("卡号类型编号")
    @JTDField(defaultValue = "BANK", entityClass = PayCardType.class)
    private String cardType;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;

    @ApiModelProperty(value = "货币符号")
    private String coinCode;
    @ApiModelProperty(value = "汇率")
    @JTDField(defaultValue = "1", description = "1平台币等于多少通道币")
    private BigDecimal exchangeRate;


    @ApiModelProperty(value = "商户编号")
    @JTDField(group = "三方相关配置", entityClass = PayMerchant.class)
    private String merchantCode;
    @ApiModelProperty(value = "支付方式")
    @JTDField(group = "三方相关配置")
    private String payType;
    @ApiModelProperty(value = "最小支付金额")
    @JTDField(group = "三方相关配置")
    private BigDecimal minAmount;
    @ApiModelProperty(value = "最大支付金额")
    @JTDField(group = "三方相关配置", defaultValue = "10000000")
    private BigDecimal maxAmount;
    @ApiModelProperty(value = "手续费率")
    @JTDField(group = "三方相关配置")
    private BigDecimal feeRate;
    @ApiModelProperty(value = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;
}
