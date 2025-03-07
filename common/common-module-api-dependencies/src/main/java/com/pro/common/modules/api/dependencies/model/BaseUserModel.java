package com.pro.common.modules.api.dependencies.model;

import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseUserModel extends BaseModel implements IUserClass {
    @Schema(description = "用户Id")
//    @JTDField(group = "基础信息", disabled = true)
    private Long userId;
    @Schema(description = "用户名")
//    @JTDField(group = "基础信息", disabled = true)
    private String username;
    @Schema(description = "内部")
//    @JTDField(group = "基础信息", disabled = true)
    private Boolean isDemo;
}
