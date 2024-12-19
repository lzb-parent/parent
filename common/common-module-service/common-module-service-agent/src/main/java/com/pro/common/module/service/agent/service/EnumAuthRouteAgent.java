package com.pro.common.module.service.agent.service;

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
public enum EnumAuthRouteAgent implements IEnumToDbEnum {
    catalog_config(ADMIN, null, CATALOG, "系统设置", null, "el-icon-s-tools", null, null, null, null, null, 200000, true, null),
    customer(ADMIN, catalog_config, MENU, "客服", "/content/customer", null, null, null, null, null, null, 180100, true, null),
    customer_QUERY(ADMIN, customer, BUTTON, "查询", null, null, "customer", null, null, null, null, 180100, true, null),
    customer_ALL(ADMIN, customer, BUTTON, "管理", null, null, "#ALL#customer", null, null, null, null, 180101, true, null),
    agent(ADMIN, catalog_config, MENU, "代理", "/sys/agent", null, null, null, null, null, null, 200300, true, null),
    agent_QUERY(ADMIN, agent, BUTTON, "查询", null, null, "agent", null, null, null, null, 200300, true, null),
    agent_ALL(ADMIN, agent, BUTTON, "管理", null, null, "#ALL#agent", null, null, null, null, 200301, true, null),

//    posterCode(ADMIN, catalog_content, MENU, "固有文章编号", "/content/posterCode", null, null, null, null, null, null, 180800, null),
//    authRoleRoute(ADMIN, catalog_config, MENU, "角色权限", "/authRoleRoute", null, null, null, null, null, null, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteAgent pcode;
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
