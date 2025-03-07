package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "支付渠道")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_merchantCode_name_payType` (`merchant_code`,`name`,`pay_type`)"
}, entityId = 321)
public class PayChannel extends BaseModel implements IOpenConfigClass {
    @Schema(description = "渠道名称")
    @JTDField(group = "基础信息")
    private String name;
    @Schema(description = "图标")
    @JTDField(group = "基础信息")
    private String icon;
    @Schema(description = "描述文章")
    @JTDField(group = "基础信息")
    private String posterCode;
    @Schema(description = "跳转地址")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null)
    private String url;
    @Schema(description = "货币符号")
    @JTDField(group = "基础信息", defaultValue = "default")
    private String coinCode;
    @Schema(description = "货币汇率")//：1平台币=*通道币
    @JTDField(group = "基础信息", defaultValue = "1")
    private BigDecimal exchangeRate;
    @Schema(description = "排序")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null)
    private Integer sort;
    @Schema(description = "启用")
    @JTDField(group = "基础信息")
    private Boolean enabled;
    @Schema(description = "站内卡号类型")
    @JTDField(group = "基础信息", entityClass = PayCardType.class)
    private String cardType;

    @Schema(description = "商户编号")
    @JTDField(group = "三方相关配置", entityClass = PayMerchant.class)
    private String merchantCode;
    @Schema(description = "三方支付编号")
    @JTDField(group = "三方相关配置")
    private String payType;
    @Schema(description = "三方支付卡类型")
    @JTDField(group = "三方相关配置")
    private String bankCode;
    @Schema(description = "最小支付金额")
    @JTDField(group = "三方相关配置", defaultValue = "10")
    private BigDecimal minAmount;
    @Schema(description = "最大支付金额")
    @JTDField(group = "三方相关配置", defaultValue = "10000000")
    private BigDecimal maxAmount;
    @Schema(description = "只提交整数")
    @JTDField(group = "三方相关配置")
    private Boolean onlyInteger;
    @Schema(description = "代收手续费百分比")
    @JTDField(group = "三方相关配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private BigDecimal feeRate;

    //    @Schema(description = "默认显示支付金额")
//    private BigDecimal defaultAmount;
//    @Schema(description = "支付金额可选项，多个用英文,隔开")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String amountOptions;
//    @Schema(description = "是否在手机端显示")
//    private Boolean showInMobile;
//    @Schema(description = "是否在PC端显示")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private Boolean showInPc;


    @Schema(description = "开户行")
    @JTDField(group = "线下充值配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private String bankName;
    @Schema(description = "开户姓名")
    @JTDField(group = "线下充值配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private String bankUsername;
    @Schema(description = "银行账号")
    @JTDField(group = "线下充值配置", notNull = JTDConst.EnumFieldNullType.can_null)
    private String bankAccount;
    @Schema(description = "钱包二维码")
    @JTDField(group = "线下充值配置", uiType = JTDConst.EnumFieldUiType.image)
    private String qrcodePic;
    @Schema(description = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;
//    @Schema(description = "说明")
//    @JTDField(mainLength = 2000, notNull = JTDConst.EnumFieldNullType.can_null)
//    private String explanation;

    //    @Schema(description = "是否需要上传交易截图")
//    private Boolean inputPic;
//    @Schema(description = "是否需要输入交易单号")
//    private Boolean inputMerchantNo;
//    @Schema(description = "是否需要输入付款账号")
//    private Boolean inputBankAccount;
//    @Schema(description = "是否需要输入付款银行")
//    private Boolean inputBankName;
//    @JTDField(javaTypeEnumClass = EnumUserRechargeProp.class, javaTypeEnumClassMultiple = true, uiType = JTDConst.EnumFieldUiType.select)
//    private String inputFields;


//    @Schema(description = "商户名称")
//    private transient String merchantName;
//    private transient Integer curPage;
//    private transient Integer pageSize;

}
