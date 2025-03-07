package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.common.model.ICountry;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "国家")
@JTDTable(entityId = 309)
public class Country extends BaseConfigModel implements ICountry, IEnumToDbDbId, IOpenConfigClass {
    @Schema(description = "国家编号")
    private String code;
    @Schema(description = "图标")
    @JTDField(uiType = JTDConst.EnumFieldUiType.image)
    private String icon;
    @Schema(description = "国家名称")
    private String name;
    @Schema(description = "国家中文名称")
    private String cname;
    @Schema(description = "语言代码")
    private String langCode;
    @Schema(description = "手机号前缀")
    private String phoneCode;
    @Schema(description = "国家代码")
    private String countryCode;
    @Schema(description = "货币代码")
    private String currencyCode;
    @Schema(description = "货币符号")
    private String currencySymbol;
    @Schema(description = "兑换美元汇率")
    private BigDecimal exchangeRate;
    @Schema(description = "时区代码")
    private String zoneCode;
    @Schema(description = "管理端显示")
    @JTDField(defaultValue = "1")
    private Boolean showAdmin;
}
