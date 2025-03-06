package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "自定义翻译")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code_lang` (`code`,`lang`)"
}, entityId = 339)
public class Translation extends BaseModel implements IConfigClass {
    @ApiModelProperty(value = "翻译固定原文")
    private String code;
    @ApiModelProperty(value = "语言代码")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @ApiModelProperty(value = "翻译内容")
    @JTDField(mainLength = 1000)
    private String content;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    //    @ApiModelProperty(value = "是否服务端")
//    private Boolean isService;
    @ApiModelProperty(value = "排序")
    private Integer sort;
}
