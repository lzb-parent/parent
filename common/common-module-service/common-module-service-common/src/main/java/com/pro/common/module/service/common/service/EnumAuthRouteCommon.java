package com.pro.common.module.service.common.service;

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
public enum EnumAuthRouteCommon implements IEnumToDbEnum {
    dashboard(ADMIN, null, MENU, "面板", "/dashboard", "el-icon-home", null, null, null, null, null, 10100, true, null),
    count(ADMIN, null, MENU, "统计", "/count", "el-icon-s-data", "user,userMoneyRecord", null, null, null, null, 10200, true, null),
    catalog_content(ADMIN, null, CATALOG, "站点文案管理", null, "el-icon-document", null, null, null, null, null, 180000, true, null),
    catalog_config(ADMIN, null, CATALOG, "系统设置", null, "el-icon-s-tools", null, null, null, null, null, 200000, true, null),
    country(ADMIN, catalog_content, MENU, "国家语言", "/content/country", null, null, null, null, null, null, 180100, true, null),
    country_QUERY(ADMIN, country, BUTTON, "查询", null, null, "country", null, null, null, null, 180100, true, null),
    country_ALL(ADMIN, country, BUTTON, "管理", null, null, "#ALL#country", null, null, null, null, 180101, true, null),
    banner(ADMIN, catalog_content, MENU, "导航广告栏", "/content/banner", null, null, null, null, null, null, 180300, true, null),
    banner_QUERY(ADMIN, banner, BUTTON, "查询", null, null, "banner", null, null, null, null, 180300, true, null),
    banner_ALL(ADMIN, banner, BUTTON, "管理", null, null, "#ALL#banner", null, null, null, null, 180301, true, null),
    poster(ADMIN, catalog_content, MENU, "文章", "/content/poster", null, null, null, null, null, null, 180400, true, null),
    poster_QUERY(ADMIN, poster, BUTTON, "查询", null, null, "poster", null, null, null, null, 180400, true, null),
    poster_ALL(ADMIN, poster, BUTTON, "管理", null, null, "#ALL#poster", null, null, null, null, 180401, true, null),
    posterCategory(ADMIN, catalog_content, MENU, "文章分类", "/content/posterCategory", null, null, null, null, null, null, 180500, true, null),
    posterCategory_QUERY(ADMIN, posterCategory, BUTTON, "查询", null, null, "posterCategory", null, null, null, null, 180500, true, null),
    posterCategory_ALL(ADMIN, posterCategory, BUTTON, "管理", null, null, "#ALL#posterCategory", null, null, null, null, 180501, true, null),
    translation(ADMIN, catalog_content, MENU, "自定义翻译", "/content/translation", null, null, null, null, null, null, 180900, true, null),
    translation_QUERY(ADMIN, translation, BUTTON, "查询", null, null, "translation", null, null, null, null, 180900, true, null),
    translation_ALL(ADMIN, translation, BUTTON, "管理", null, null, "#ALL#translation", null, null, null, null, 180901, true, null),
    sysAddress(ADMIN, catalog_content, MENU, "地址", "/content/sysAddress", null, null, null, null, null, null, 181100, true, null),
    sysAddress_QUERY(ADMIN, sysAddress, BUTTON, "查询", null, null, "sysAddress", null, null, null, null, 181100, true, null),
    sysAddress_ALL(ADMIN, sysAddress, BUTTON, "管理", null, null, "#ALL#sysAddress", null, null, null, null, 181101, true, null),
    inputField(ADMIN, catalog_content, MENU, "定制属性", "/content/inputField", null, null, null, null, null, null, 181200, true, null),
    inputField_QUERY(ADMIN, inputField, BUTTON, "查询", null, null, "inputField", null, null, null, null, 181200, true, null),
    inputField_ALL(ADMIN, inputField, BUTTON, "管理", null, null, "#ALL#inputField", null, null, null, null, 181201, true, null),
    authRoute(ADMIN, catalog_config, MENU, "菜单", "/sys/authRoute", null, null, null, null, null, null, 200500, true, null),
    authRoute_QUERY(ADMIN, authRoute, BUTTON, "查询", null, null, "authRoute", null, null, null, null, 200500, true, null),
    authRoute_ALL(ADMIN, authRoute, BUTTON, "管理", null, null, "#ALL#authRoute", null, null, null, null, 200501, true, null),
    authRole(ADMIN, catalog_config, MENU, "角色", "/sys/authRole", null, null, null, null, null, null, 200600, true, null),
    authRole_QUERY(ADMIN, authRole, BUTTON, "查询", null, null, "authRole", null, null, null, null, 200600, true, null),
    authRole_ALL(ADMIN, authRole, BUTTON, "管理", null, null, "#ALL#authRole", null, null, null, null, 200601, true, null),

//    posterCode(ADMIN, catalog_content, MENU, "固有文章编号", "/content/posterCode", null, null, null, null, null, null, 180800, null),
//    authRoleRoute(ADMIN, catalog_config, MENU, "角色权限", "/authRoleRoute", null, null, null, null, null, null, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteCommon pcode;
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
