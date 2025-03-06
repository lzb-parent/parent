package com.pro.common.modules.api.dependencies.model;

import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserModel extends BaseModel implements IUserClass {
    @ApiModelProperty("用户Id")
//    @JTDField(group = "基础信息", disabled = true)
    private Long userId;
    @ApiModelProperty("用户名")
//    @JTDField(group = "基础信息", disabled = true)
    private String username;
    @ApiModelProperty("内部")
//    @JTDField(group = "基础信息", disabled = true)
    private Boolean isDemo;
}
