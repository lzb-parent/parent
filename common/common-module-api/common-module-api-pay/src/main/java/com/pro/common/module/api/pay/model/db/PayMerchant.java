package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.common.modules.api.dependencies.model.classes.ISimpleInfo;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付商户
 *
 * @author admin
 */
@Data

@EqualsAndHashCode(callSuper = true)
@Schema(description = "支付商户")
@JTDTable(sequences = {
        "UNIQUE KEY `code` (`code`)"
}, entityId = 322)
public class PayMerchant extends BaseConfigModel implements IAdminClass, ISimpleInfo {

    @Schema(description = "商户编号")
    private String code;
    @Schema(description = "商户名称")
    private String name;
//    @Schema(description = "商户账号")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String account;
//    @Schema(description = "商户密码")
//    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
//    private String password;
    @Schema(description = "商户号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String mchId;
    @Schema(description = "商户Key")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String mchKey;
    @Schema(description = "代付商户号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String payoutMchId;
    @Schema(description = "代付商户Key")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String payoutMchKey;
    @Schema(description = "商户公钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, mainLength = 2000)
    private String publicKey;
    @Schema(description = "平台公钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, mainLength = 2000)
    private String platPublicKey;
    @Schema(description = "代付商户公钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String payoutPublicKey;
    @Schema(description = "转账方式")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String transferType;
    @Schema(description = "api接口地址")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String apiUrl;
    @Schema(description = "提现接口地址")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String withdrawUrl;
    @Schema(description = "后台地址")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String htUrl;
//    @Schema(description = "启用")
//    private Boolean enabled;
    @Schema(description = "是否是线下支付")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null, disabled = true)
    private Boolean offlineFlag;

    @Schema(description = "白名单")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String whitelist;
    @Schema(description = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;

}
