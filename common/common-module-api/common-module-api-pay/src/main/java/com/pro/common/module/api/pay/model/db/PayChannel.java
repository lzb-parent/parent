package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * 支付渠道
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "支付渠道")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_merchantCode_name_payType` (`merchant_code`,`name`,`pay_type`)"
}, entityId = 321)
public class PayChannel extends BaseModel implements IConfigClass {
    @ApiModelProperty(value = "渠道名称")
    @JTDField(group = "基础信息")
    private String name;
    @ApiModelProperty(value = "图标")
    @JTDField(group = "基础信息")
    private String icon;
    @ApiModelProperty(value = "描述文章")
    @JTDField(group = "基础信息")
    private String posterCode;
    @ApiModelProperty(value = "跳转地址")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null)
    private String url;
    @ApiModelProperty(value = "货币符号")
    @JTDField(group = "基础信息", defaultValue = "default")
    private String coinCode;
    @ApiModelProperty(value = "货币汇率")//：1平台币=*通道币
    @JTDField(group = "基础信息", defaultValue = "1")
    private BigDecimal exchangeRate;
    @ApiModelProperty(value = "排序")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null)
    private Integer sort;
    @ApiModelProperty(value = "启用")
    @JTDField(group = "基础信息")
    private Boolean enabled;
    @ApiModelProperty(value = "站内卡号类型")
    @JTDField(group = "基础信息", entityClass = PayCardType.class)
    private String cardType;

    @ApiModelProperty(value = "商户编号")
    @JTDField(group = "三方相关配置", entityClass = PayMerchant.class)
    private String merchantCode;
    @ApiModelProperty(value = "三方支付编号")
    @JTDField(group = "三方相关配置")
    private String payType;
    @ApiModelProperty(value = "三方支付卡类型")
    @JTDField(group = "三方相关配置")
    private String bankCode;
    @ApiModelProperty(value = "最小支付金额")
    @JTDField(group = "三方相关配置", defaultValue = "10")
    private BigDecimal minAmount;
    @ApiModelProperty(value = "最大支付金额")
    @JTDField(group = "三方相关配置", defaultValue = "10000000")
    private BigDecimal maxAmount;
    @ApiModelProperty(value = "只提交整数")
    @JTDField(group = "三方相关配置")
    private Boolean onlyInteger;
    @ApiModelProperty(value = "代收手续费百分比")
    @JTDField(group = "三方相关配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private BigDecimal feeRate;

    //    @ApiModelProperty(value = "默认显示支付金额")
//    private BigDecimal defaultAmount;
//    @ApiModelProperty(value = "支付金额可选项，多个用英文,隔开")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String amountOptions;
//    @ApiModelProperty(value = "是否在手机端显示")
//    private Boolean showInMobile;
//    @ApiModelProperty(value = "是否在PC端显示")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private Boolean showInPc;


    @ApiModelProperty(value = "开户行")
    @JTDField(group = "线下充值配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private String bankName;
    @ApiModelProperty(value = "开户姓名")
    @JTDField(group = "线下充值配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private String bankUsername;
    @ApiModelProperty(value = "银行账号")
    @JTDField(group = "线下充值配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private String bankAccount;
    @ApiModelProperty(value = "钱包二维码")
    @JTDField(group = "线下充值配置", uiType = JTDConst.EnumFieldUiType.image)
    private String qrcodePic;
    @ApiModelProperty(value = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;
//    @ApiModelProperty(value = "说明")
//    @JTDField(mainLength = 2000, notNull = JTDConst.EnumFieldNullType.can_null)
//    private String explanation;

    //    @ApiModelProperty(value = "是否需要上传交易截图")
//    private Boolean inputPic;
//    @ApiModelProperty(value = "是否需要输入交易单号")
//    private Boolean inputMerchantNo;
//    @ApiModelProperty(value = "是否需要输入付款账号")
//    private Boolean inputBankAccount;
//    @ApiModelProperty(value = "是否需要输入付款银行")
//    private Boolean inputBankName;
//    @JTDField(javaTypeEnumClass = EnumUserRechargeProp.class, javaTypeEnumClassMultiple = true, uiType = JTDConst.EnumFieldUiType.select)
//    private String inputFields;


//    @ApiModelProperty(value = "商户名称")
//    private transient String merchantName;
//    private transient Integer curPage;
//    private transient Integer pageSize;

}
