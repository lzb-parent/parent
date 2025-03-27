package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.api.model.IdModel;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色_路由表
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色_路由表")
public class AuthRoleRoute extends BaseModel implements IConfigClass {
    @ApiModelProperty(value = "角色id")
    private Long roleId;
    @ApiModelProperty(value = "路由id")
    private Long routeId;
    @ApiModelProperty(value = "排序")
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
