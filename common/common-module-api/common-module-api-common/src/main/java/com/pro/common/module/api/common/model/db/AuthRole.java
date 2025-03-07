package com.pro.common.module.api.common.model.db;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色")
public class AuthRole extends BaseConfigModel implements IEnumToDbDbId {
    @Schema(description = "系统角色")
    private EnumSysRole sysRole;
    @Schema(description = "超级管理员")
    private Boolean superFlag;
    //    @Schema(description = "类型")
//    private String type;
    @Schema(description = "角色名称")
    private String name;
    @Schema(description = "关联的菜单路由")
    @JTDField(entityClass = AuthRoleRoute.class, javaTypeEnumClassMultiple = true, type = JTDConst.EnumFieldType.text)
    private String routeCodes;
    @Schema(description = "启用")
    private Boolean enabled;
}
