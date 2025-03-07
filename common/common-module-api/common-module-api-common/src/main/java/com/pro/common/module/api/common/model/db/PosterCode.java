package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.api.enums.IEnumToDbDbCode;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "固有文章编号枚举")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code` (`code`)",
}, entityId = 117)
public class PosterCode extends BaseModel implements IOpenConfigClass, IEnumToDbDbCode {
    @Schema(description = "固有文章编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String code;
    @Schema(description = "中文名字")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String name;
    @Schema(description = "启用")
    private Boolean enabled;
    @Schema(description = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @Schema(description = "内部描述")
    private String remark;
}
