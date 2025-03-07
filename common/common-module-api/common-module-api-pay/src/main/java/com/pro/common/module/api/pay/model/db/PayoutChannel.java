package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "代付渠道")
@JTDTable(entityId = 323)
public class PayoutChannel extends BaseModel implements IOpenConfigClass {

    @Schema(description = "名称")
    private String name;
    @Schema(description = "卡号类型编号")
    @JTDField(defaultValue = "BANK", entityClass = PayCardType.class)
    private String cardType;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "启用")
    private Boolean enabled;

    @Schema(description = "货币符号")
    private String coinCode;
    @Schema(description = "汇率")
    @JTDField(defaultValue = "1", description = "1平台币等于多少通道币")
    private BigDecimal exchangeRate;


    @Schema(description = "商户编号")
    @JTDField(group = "三方相关配置", entityClass = PayMerchant.class)
    private String merchantCode;
    @Schema(description = "支付方式")
    @JTDField(group = "三方相关配置")
    private String payType;
    @Schema(description = "最小支付金额")
    @JTDField(group = "三方相关配置")
    private BigDecimal minAmount;
    @Schema(description = "最大支付金额")
    @JTDField(group = "三方相关配置", defaultValue = "10000000")
    private BigDecimal maxAmount;
    @Schema(description = "手续费率")
    @JTDField(group = "三方相关配置")
    private BigDecimal feeRate;
    @Schema(description = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;
}
