package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色_路由表
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色_路由表")
public class AuthRoleRoute extends BaseModel implements IOpenConfigClass {
    @Schema(description = "角色id")
    private Long roleId;
    @Schema(description = "路由id")
    private Long routeId;
    @Schema(description = "排序")
    @JTDField(defaultValue = "100")
    private Integer sort;
    @Override
    public Boolean getEnabled() {
        return true;
    }
//
//    @Override
//    public Integer getSort() {
//        return 0;
//    }
}
