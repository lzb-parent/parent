package com.pro.common.module.service.agent.service;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumToDbEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.modules.api.dependencies.enums.EnumAuthRouteType.*;
import static com.pro.common.modules.api.dependencies.enums.EnumSysRole.AGENT;

/**
 * agent 相对 admin需要定制的(隐藏的菜单)
 */
@Getter
@AllArgsConstructor
@EnumToDbEnum(entityClass = "com.pro.common.module.api.common.model.db.AuthRoute")
public enum EnumAuthRouteForAgent implements IEnumToDbEnum {
    dashboard(AGENT, null, MENU, "面板", "/dashboard", "el-icon-home", null, null, null, null, false, 10100, true, null),
    count(AGENT, null, MENU, "统计", "/count", "el-icon-s-data", "user,userMoneyRecord", null, null, null, false, 10200, true, null),
    catalog_content(AGENT, null, CATALOG, "站点文案管理", null, "el-icon-document", null, null, null, null, false, 180000, true, null),
    catalog_pay(AGENT, null, CATALOG, "支付管理", null, "el-icon-s-goods", null, null, null, null, false, 190000, true, null),
    catalog_config(AGENT, null, CATALOG, "系统设置", null, "el-icon-s-tools", null, null, null, null, false, 200000, true, null),
    userLevelConfig(AGENT, catalog_content, MENU, "用户等级配置", "/user/userLevelConfig", null, null, null, null, null, false, 16900, true, null),
    userLevelConfig_QUERY(AGENT, userLevelConfig, BUTTON, "查询", null, null, "userLevelConfig", null, null, null, false, 16900, true, null),
    userLevelConfig_ALL(AGENT, userLevelConfig, BUTTON, "管理", null, null, "#ALL#userLevelConfig", null, null, null, false, 16901, true, null),
    country(AGENT, catalog_content, MENU, "国家语言", "/content/country", null, null, null, null, null, false, 180100, true, null),
    country_QUERY(AGENT, country, BUTTON, "查询", null, null, "country", null, null, null, false, 180100, true, null),
    country_ALL(AGENT, country, BUTTON, "管理", null, null, "#ALL#country", null, null, null, false, 180101, true, null),
    banner(AGENT, catalog_content, MENU, "导航广告栏", "/content/banner", null, null, null, null, null, false, 180300, true, null),
    banner_QUERY(AGENT, banner, BUTTON, "查询", null, null, "banner", null, null, null, false, 180300, true, null),
    banner_ALL(AGENT, banner, BUTTON, "管理", null, null, "#ALL#banner", null, null, null, false, 180301, true, null),
    poster(AGENT, catalog_content, MENU, "文章", "/content/poster", null, null, null, null, null, false, 180400, true, null),
    poster_QUERY(AGENT, poster, BUTTON, "查询", null, null, "poster", null, null, null, false, 180400, true, null),
    poster_ALL(AGENT, poster, BUTTON, "管理", null, null, "#ALL#poster", null, null, null, false, 180401, true, null),
    posterCategory(AGENT, catalog_content, MENU, "文章分类", "/content/posterCategory", null, null, null, null, null, false, 180500, true, null),
    posterCategory_QUERY(AGENT, posterCategory, BUTTON, "查询", null, null, "posterCategory", null, null, null, false, 180500, true, null),
    posterCategory_ALL(AGENT, posterCategory, BUTTON, "管理", null, null, "#ALL#posterCategory", null, null, null, false, 180501, true, null),
    sysMsgChannelMerchant(AGENT, catalog_content, MENU, "消息通道商户", "/content/sysMsgChannelMerchant", null, null, null, null, null, false, 180600, true, null),
    sysMsgChannelMerchant_QUERY(AGENT, sysMsgChannelMerchant, BUTTON, "查询", null, null, "sysMsgChannelMerchant", null, null, null, false, 180600, true, null),
    sysMsgChannelMerchant_ALL(AGENT, sysMsgChannelMerchant, BUTTON, "管理", null, null, "#ALL#sysMsgChannelMerchant", null, null, null, false, 180601, true, null),
    sysMsgChannelTemplate(AGENT, catalog_content, MENU, "消息通道模板", "/content/sysMsgChannelTemplate", null, null, null, null, null, false, 180700, true, null),
    sysMsgChannelTemplate_QUERY(AGENT, sysMsgChannelTemplate, BUTTON, "查询", null, null, "sysMsgChannelTemplate", null, null, null, false, 180700, true, null),
    sysMsgChannelTemplate_ALL(AGENT, sysMsgChannelTemplate, BUTTON, "管理", null, null, "#ALL#sysMsgChannelTemplate", null, null, null, false, 180701, true, null),
    translation(AGENT, catalog_content, MENU, "自定义翻译", "/content/translation", null, null, null, null, null, false, 180900, true, null),
    translation_QUERY(AGENT, translation, BUTTON, "查询", null, null, "translation", null, null, null, false, 180900, true, null),
    translation_ALL(AGENT, translation, BUTTON, "管理", null, null, "#ALL#translation", null, null, null, false, 180901, true, null),
    sysAddress(AGENT, catalog_content, MENU, "地址", "/content/sysAddress", null, null, null, null, null, false, 181100, true, null),
    sysAddress_QUERY(AGENT, sysAddress, BUTTON, "查询", null, null, "sysAddress", null, null, null, false, 181100, true, null),
    sysAddress_ALL(AGENT, sysAddress, BUTTON, "管理", null, null, "#ALL#sysAddress", null, null, null, false, 181101, true, null),
    inputField(AGENT, catalog_content, MENU, "定制属性", "/content/inputField", null, null, null, null, null, false, 181200, true, null),
    inputField_QUERY(AGENT, inputField, BUTTON, "查询", null, null, "inputField", null, null, null, false, 181200, true, null),
    inputField_ALL(AGENT, inputField, BUTTON, "管理", null, null, "#ALL#inputField", null, null, null, false, 181201, true, null),
    payChannel(AGENT, catalog_pay, MENU, "充值渠道", "/pay/payChannel", null, null, null, null, null, false, 190100, true, null),
    payChannel_QUERY(AGENT, payChannel, BUTTON, "查询", null, null, "payChannel", null, null, null, false, 190100, true, null),
    payChannel_ALL(AGENT, payChannel, BUTTON, "管理", null, null, "#ALL#payChannel", null, null, null, false, 190101, true, null),
    payoutChannel(AGENT, catalog_pay, MENU, "代付渠道", "/pay/payoutChannel", null, null, null, null, null, false, 190200, true, null),
    payoutChannel_QUERY(AGENT, payoutChannel, BUTTON, "查询", null, null, "payoutChannel", null, null, null, false, 190200, true, null),
    payoutChannel_ALL(AGENT, payoutChannel, BUTTON, "管理", null, null, "#ALL#payoutChannel", null, null, null, false, 190201, true, null),
    payMerchant(AGENT, catalog_pay, MENU, "商户", "/pay/payMerchant", null, null, null, null, null, false, 190300, true, null),
    payMerchant_QUERY(AGENT, payMerchant, BUTTON, "查询", null, null, "payMerchant", null, null, null, false, 190300, true, null),
    payMerchant_ALL(AGENT, payMerchant, BUTTON, "管理", null, null, "#ALL#payMerchant", null, null, null, false, 190301, true, null),
    payBank(AGENT, catalog_pay, MENU, "商户支持的银行", "/pay/payBank", null, null, null, null, null, false, 190400, true, null),
    payBank_QUERY(AGENT, payBank, BUTTON, "查询", null, null, "payBank", null, null, null, false, 190400, true, null),
    payBank_ALL(AGENT, payBank, BUTTON, "管理", null, null, "#ALL#payBank", null, null, null, false, 190401, true, null),
    payCardType(AGENT, catalog_pay, MENU, "站内卡号类型", "/pay/payCardType", null, null, null, null, null, false, 190500, true, null),
    payCardType_QUERY(AGENT, payCardType, BUTTON, "查询", null, null, "payCardType", null, null, null, false, 190500, true, null),
    payCardType_ALL(AGENT, payCardType, BUTTON, "管理", null, null, "#ALL#payCardType", null, null, null, false, 190501, true, null),
    authDict(AGENT, catalog_config, MENU, "字典", "/sys/authDict", null, null, null, null, null, false, 200100, true, null),
    authDict_QUERY(AGENT, authDict, BUTTON, "查询", null, null, "authDict", null, null, null, false, 200100, true, null),
    authDict_ALL(AGENT, authDict, BUTTON, "管理", null, null, "#ALL#authDict", null, null, null, false, 200101, true, null),
    admin(AGENT, catalog_config, MENU, "管理员", "/sys/admin", null, null, null, null, null, false, 200400, true, null),
    admin_QUERY(AGENT, admin, BUTTON, "查询", null, null, "admin", null, null, null, false, 200400, true, null),
    admin_ALL(AGENT, admin, BUTTON, "管理", null, null, "#ALL#admin", null, null, null, false, 200401, true, null),
    authRoute(AGENT, catalog_config, MENU, "菜单", "/sys/authRoute", null, null, null, null, null, false, 200500, true, null),
    authRoute_QUERY(AGENT, authRoute, BUTTON, "查询", null, null, "authRoute", null, null, null, false, 200500, true, null),
    authRoute_ALL(AGENT, authRoute, BUTTON, "管理", null, null, "#ALL#authRoute", null, null, null, false, 200501, true, null),
    authRole(AGENT, catalog_config, MENU, "角色", "/sys/authRole", null, null, null, null, null, false, 200600, true, null),
    authRole_QUERY(AGENT, authRole, BUTTON, "查询", null, null, "authRole", null, null, null, false, 200600, true, null),
    authRole_ALL(AGENT, authRole, BUTTON, "管理", null, null, "#ALL#authRole", null, null, null, false, 200601, true, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteForAgent pcode;
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
