package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "角色")
public class AuthRole extends BaseConfigModel implements IEnumToDbDbId {
    @ApiModelProperty(value = "系统角色")
    private EnumSysRole sysRole;
    @ApiModelProperty(value = "超级管理员")
    private Boolean superFlag;
    //    @ApiModelProperty(value = "类型")
//    private String type;
    @ApiModelProperty(value = "角色名称")
    private String name;
    @ApiModelProperty(value = "关联的菜单路由")
    @JTDField(entityClass = AuthRoleRoute.class, javaTypeEnumClassMultiple = true, type = JTDConst.EnumFieldType.text)
    private String routeCodes;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
}
