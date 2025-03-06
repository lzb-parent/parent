package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.common.modules.api.dependencies.model.classes.ISimpleInfo;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付商户
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "支付商户")
@JTDTable(sequences = {
        "UNIQUE KEY `code` (`code`)"
}, entityId = 322)
public class PayMerchant extends BaseConfigModel implements IAdminClass, ISimpleInfo {

    @ApiModelProperty(value = "商户编号")
    private String code;
    @ApiModelProperty(value = "商户名称")
    private String name;
//    @ApiModelProperty(value = "商户账号")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String account;
//    @ApiModelProperty(value = "商户密码")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String password;
    @ApiModelProperty(value = "商户号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String mchId;
    @ApiModelProperty(value = "商户Key")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String mchKey;
    @ApiModelProperty(value = "代付商户号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String payoutMchId;
    @ApiModelProperty(value = "代付商户Key")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String payoutMchKey;
    @ApiModelProperty(value = "商户公钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, mainLength = 2000)
    private String publicKey;
    @ApiModelProperty(value = "平台公钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, mainLength = 2000)
    private String platPublicKey;
    @ApiModelProperty(value = "代付商户公钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String payoutPublicKey;
    @ApiModelProperty(value = "转账方式")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String transferType;
    @ApiModelProperty(value = "api接口地址")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String apiUrl;
    @ApiModelProperty(value = "提现接口地址")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String withdrawUrl;
    @ApiModelProperty(value = "后台地址")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String htUrl;
//    @ApiModelProperty(value = "启用")
//    private Boolean enabled;
    @ApiModelProperty(value = "是否是线下支付")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, disabled = true)
    private Boolean offlineFlag;

    @ApiModelProperty(value = "白名单")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String whitelist;
    @ApiModelProperty(value = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;

}
