package com.pro.common.module.service.user.service;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.CATALOG;
import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.MENU;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.ADMIN;

@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.AuthRoute")
public enum EnumAuthRouteUser implements IEnumToDbEnum {
    user(ADMIN, null, CATALOG, "用户管理", null, "el-icon-user", null, null, null, null, null, 320000, null),
    userAddress(ADMIN, user, MENU, "用户地址", "/user/userAddress", null, null, null, null, null, null, 320100, null),
    userBankCard(ADMIN, user, MENU, "用户银行卡", "/user/userBankCard", null, null, null, null, null, null, 320200, null),
    // 可以继续添加其他模块...
    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteUser pcode;
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
