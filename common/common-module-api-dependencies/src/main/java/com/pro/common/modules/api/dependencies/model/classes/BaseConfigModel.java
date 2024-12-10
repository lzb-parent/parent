package com.pro.common.modules.api.dependencies.model.classes;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public abstract class BaseConfigModel extends BaseModel implements IConfigClass {
//    @ApiModelProperty(value = "主键id")
//    protected Long id;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
    @ApiModelProperty(value = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @ApiModelProperty(value = "描述")
    private String remark;
}
