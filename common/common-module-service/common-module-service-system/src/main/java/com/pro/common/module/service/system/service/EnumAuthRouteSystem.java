package com.pro.common.module.service.system.service;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.*;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.ADMIN;

@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.AuthRoute")
public enum EnumAuthRouteSystem implements IEnumToDbEnum {
    catalog_config(ADMIN, null, CATALOG, "系统设置", null, "el-icon-s-tools", null, null, null, null, null, 200000, null),
    authDict(ADMIN, catalog_config, MENU, "字典", "/sys/authDict", null, null, null, null, null, null, 200100, null),
    authDict_QUERY(ADMIN, authDict, BUTTON, "查询", null, null, "authDict", null, null, null, null, 200100, null),
    authDict_ALL(ADMIN, authDict, BUTTON, "管理", null, null, "#ALL#authDict", null, null, null, null, 200101, null),
    COMMON_TABLE_QUERY(ADMIN, catalog_config, URI, "通用表单访问权限", null, null, "/commonTable/info/**", null, null, null, null, 200200, null),
    ;
    // 可以继续添加其他模块...

    private final EnumSysRole sysRole;
    private final EnumAuthRouteSystem pcode;
    private final EnumAuthRouteType type;
    private final String name;
    private final String componentPath;
    private final String icon;
    private final String permissionPaths;
    private final String pic;
    private final String url;
    private final String remark;
    private final Boolean enabled;
    private final Integer sort;
    private final String forceChangeTime;

    @Override
    public String getToDbCode() {
        return sysRole + CommonConst.Str.SPLIT + name();
    }
}
