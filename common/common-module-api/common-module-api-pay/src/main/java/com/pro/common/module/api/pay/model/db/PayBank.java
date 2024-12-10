package com.pro.common.module.api.pay.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付银行卡
 */
@Data
@ApiModel(description = "商户支持的银行")
@JTDTable(entityId = 319,
        sequences = {
                "UNIQUE KEY `merchantCode_code` (`merchant_code`,`code`,`name`)",
        })
public class PayBank extends BaseModel implements IConfigClass {

    @ApiModelProperty(value = "商户编码")
    @JTDField(entityClass = PayMerchant.class)
    private String merchantCode;
    @ApiModelProperty(value = "银行卡编号")
    private String code;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "排序")
    private Integer sort;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    @ApiModelProperty(value = "管理端显示")
    @JTDField(defaultValue = "1") // todo 项目中后期时改为 0
    private Boolean showAdmin;
}
