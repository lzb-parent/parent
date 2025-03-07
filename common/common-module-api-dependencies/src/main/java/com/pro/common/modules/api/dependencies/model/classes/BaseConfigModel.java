package com.pro.common.modules.api.dependencies.model.classes;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
//@EqualsAndHashCode(callSuper = true)
public abstract class BaseConfigModel extends BaseModel {
//    @Schema(description = "主键id")
//    protected Long id;
    @Schema(description = "启用")// 功能关闭
    private Boolean enabled;
    @Schema(description = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @Schema(description = "描述")
    private String remark;
}
