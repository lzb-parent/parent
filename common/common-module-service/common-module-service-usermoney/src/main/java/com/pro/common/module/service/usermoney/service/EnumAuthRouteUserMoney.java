package com.pro.common.module.service.usermoney.service;

import com.pro.common.module.api.usermoney.model.db.UserAmountTotal;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.*;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.ADMIN;

/**
 * 路由配置 基础
 * userMoneyWait
 * userMoneyWaitRecord
 * posterCode
 * authRoleRoute
 * sysMultiClassRelation
 * 暂时不做路由
 */
@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.AuthRoute")
public enum EnumAuthRouteUserMoney implements IEnumToDbEnum {
    catalog_user(ADMIN, null, CATALOG, "用户管理", null, "el-icon-user-solid", null, null, null, null, null, 16000, true, null),
    userMoney(ADMIN, catalog_user, MENU, "用户余额", "/user/userMoney", null, null, null, null, null, null, 16200, true, null),
    userMoney_QUERY(ADMIN, userMoney, BUTTON, "查询", null, null, "userMoney", null, null, null, null, 16200, true, null),
    userMoney_ALL(ADMIN, userMoney, BUTTON, "管理", null, null, "#ALL#userMoney", null, null, null, null, 16201, true, null),
    userMoneyRecord(ADMIN, catalog_user, MENU, "用户余额变动", "/user/userMoneyRecord", null, null, null, null, null, null, 16300, true, null),
    userMoneyRecord_QUERY(ADMIN, userMoneyRecord, BUTTON, "查询", null, null, "userMoneyRecord", null, null, null, null, 16300, true, null),
    userMoneyRecord_ALL(ADMIN, userMoneyRecord, BUTTON, "管理", null, null, "#ALL#userMoneyRecord", null, null, null, null, 16301, true, null),
    UserAmountTotal(ADMIN, catalog_user, MENU, "用户数额总计", "/user/userAmountTotal", null, null, null, null, null, null, 16320, true, null),
    UserAmountTotal_QUERY(ADMIN, UserAmountTotal, BUTTON, "查询", null, null, "userAmountTotal", null, null, null, null, 16320, true, null),
    UserAmountTotal_ALL(ADMIN, UserAmountTotal, BUTTON, "管理", null, null, "#ALL#userAmountTotal", null, null, null, null, 16321, true, null),

//    posterCode(ADMIN, catalog_content, MENU, "固有文章编号", "/content/posterCode", null, null, null, null, null, null, 180800, null),
//    authRoleRoute(ADMIN, catalog_config, MENU, "角色权限", "/authRoleRoute", null, null, null, null, null, null, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteUserMoney pcode;
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
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;

    @Override
    public String getToDbCode() {
        return sysRole + CommonConst.Str.SPLIT + name();
    }
}
