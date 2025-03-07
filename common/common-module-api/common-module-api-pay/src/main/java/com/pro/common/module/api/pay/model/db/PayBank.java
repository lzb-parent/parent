package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 支付银行卡
 */
@Data
@Schema(description = "商户支持的银行")
@JTDTable(entityId = 319,
        sequences = {
                "UNIQUE KEY `merchantCode_code` (`merchant_code`,`code`,`name`)",
        })
public class PayBank extends BaseModel implements IOpenConfigClass {

    @Schema(description = "商户编码")
    @JTDField(entityClass = PayMerchant.class)
    private String merchantCode;
    @Schema(description = "银行卡编号")
    private String code;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "启用")
    private Boolean enabled;
    @Schema(description = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;
}
