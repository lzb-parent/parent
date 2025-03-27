package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.api.enums.IEnumToDbDbCode;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "固有文章编号枚举")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code` (`code`)",
}, entityId = 117)
public class PosterCode extends BaseModel implements IConfigClass, IEnumToDbDbCode {
    @ApiModelProperty(value = "固有文章编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String code;
    @ApiModelProperty(value = "中文名字")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String name;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    @ApiModelProperty(value = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @ApiModelProperty(value = "内部描述")
    private String remark;
}
