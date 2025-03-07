package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "自定义翻译")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code_lang` (`code`,`lang`)"
}, entityId = 339)
public class Translation extends BaseModel implements IOpenConfigClass {
    @Schema(description = "翻译固定原文")
    private String code;
    @Schema(description = "语言代码")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode")
    private String lang;
    @Schema(description = "翻译内容")
    @JTDField(mainLength = 1000)
    private String content;
    @Schema(description = "启用")
    private Boolean enabled;
    //    @Schema(description = "是否服务端")
//    private Boolean isService;
    @Schema(description = "排序")
    private Integer sort;
}
