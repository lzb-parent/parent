package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
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
@ApiModel(description = "固有导航广告栏")
@JTDTable(sequences = {
        "UNIQUE KEY `uk_code` (`code`)",
}, entityId = 117)
public class BannerType extends BaseModel implements IConfigClass, IEnumToDbDbId {
    @ApiModelProperty(value = "固有导航广告栏编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String code;
    @ApiModelProperty(value = "名称")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String name;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    @ApiModelProperty(value = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @ApiModelProperty(value = "内部描述")
    @JTDField(uiType = JTDConst.EnumFieldUiType.textarea)
    private String remark;
}
