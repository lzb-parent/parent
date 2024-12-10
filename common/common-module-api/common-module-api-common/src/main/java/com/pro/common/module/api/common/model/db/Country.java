package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.common.model.ICountry;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "国家")
@JTDTable(entityId = 309)
public class Country extends BaseConfigModel implements ICountry, IEnumToDbDbId {
    @ApiModelProperty(value = "国家编号")
    private String code;
    @ApiModelProperty(value = "图标")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String icon;
    @ApiModelProperty(value = "国家名称")
    private String name;
    @ApiModelProperty(value = "国家中文名称")
    private String cname;
    @ApiModelProperty(value = "语言代码")
    private String langCode;
    @ApiModelProperty(value = "手机号前缀")
    private String phoneCode;
    @ApiModelProperty(value = "国家代码")
    private String countryCode;
    @ApiModelProperty(value = "货币代码")
    private String currencyCode;
    @ApiModelProperty(value = "货币符号")
    private String currencySymbol;
    @ApiModelProperty(value = "兑换美元汇率")
    private BigDecimal exchangeRate;
    @ApiModelProperty(value = "时区代码")
    private String zoneCode;
    @ApiModelProperty(value = "管理端显示")
    @JTDField(defaultValue = "1") // todo 项目中后期时改为 0
    private Boolean showAdmin;
}
