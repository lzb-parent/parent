package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@ApiModel(value = "地址数据")
@NoArgsConstructor
@JTDTable(entityId = 328)
public class SysAddress extends BaseConfigModel implements IConfigClass {
    @ApiModelProperty("编号邮编")
    private Long code;
    @ApiModelProperty("父级编号邮编")
    private Long pcode;
    @ApiModelProperty("国家")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String country;// bn-孟加拉
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("邮编")
    private String postalCode;
    @ApiModelProperty("三方编号")
    private String outCode;
    @ApiModelProperty("层级")
    @JTDField(description = "1_国家_2_省份_3_市_4_县")
    private Integer level;

    public SysAddress(Long code, Long pcode, String country, String name, Integer level, String outCode) {
        this.code = code;
        this.pcode = pcode;
        this.country = country;
        this.name = name;
        this.level = level;
        this.outCode = outCode;
    }
}
