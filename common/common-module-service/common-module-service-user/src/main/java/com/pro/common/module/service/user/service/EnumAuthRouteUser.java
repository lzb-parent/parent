package com.pro.common.module.service.user.service;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.CATALOG;
import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.*;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.ADMIN;

@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.AuthRoute")
public enum EnumAuthRouteUser implements IEnumToDbEnum {
    catalog_user(ADMIN, null, CATALOG, "用户管理", null, "el-icon-user-solid", null, null, null, null, null, 16000, true, null),
    user(ADMIN, catalog_user, MENU, "用户", "/user/user", null, null, null, null, null, null, 16100, true, null),
    user_QUERY(ADMIN, user, BUTTON, "查询", null, null, "user,userMoney,userAmountTotal,userLevelConfig", null, null, null, null, 16100, true, null),
    user_ALL(ADMIN, user, BUTTON, "管理", null, null, "#ALL#user,userMoney,userAmountTotal,userLevelConfig", null, null, null, null, 16101, true, null),
    userAddress(ADMIN, catalog_user, MENU, "用户地址", "/user/userAddress", null, null, null, null, null, null, 16800, true, null),
    userAddress_QUERY(ADMIN, userAddress, BUTTON, "查询", null, null, "userAddress", null, null, null, null, 16800, true, null),
    userAddress_ALL(ADMIN, userAddress, BUTTON, "管理", null, null, "#ALL#userAddress", null, null, null, null, 16801, true, null),
    userBankCard(ADMIN, catalog_user, MENU, "用户银行卡", "/user/userBankCard", null, null, null, null, null, null, 16500, true, null),
    userBankCard_QUERY(ADMIN, userBankCard, BUTTON, "查询", null, null, "userBankCard", null, null, null, null, 16500, true, null),
    userBankCard_ALL(ADMIN, userBankCard, BUTTON, "管理", null, null, "#ALL#userBankCard", null, null, null, null, 16501, true, null),
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
    private final Boolean showFlag;
    
    private final String forceChangeTime;

    @Override
    public String getToDbCode() {
        return sysRole + CommonConst.Str.SPLIT + name();
    }
}
