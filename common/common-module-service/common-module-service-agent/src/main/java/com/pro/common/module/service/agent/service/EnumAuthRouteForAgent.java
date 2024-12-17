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
    dashboard(AGENT, null, MENU, "面板", "/dashboard", "el-icon-home", null, null, null, null, false, 10100, null),
    count(AGENT, null, MENU, "统计", "/count", "el-icon-s-data", "user,userMoneyRecord", null, null, null, false, 10200, null),
//    catalog_user(AGENT, null, CATALOG, "用户管理", null, "el-icon-user-solid", null, null, null, null, false, 16000, null),
    catalog_content(AGENT, null, CATALOG, "站点文案管理", null, "el-icon-document", null, null, null, null, false, 180000, null),
    catalog_pay(AGENT, null, CATALOG, "支付管理", null, "el-icon-s-goods", null, null, null, null, false, 190000, null),
    catalog_config(AGENT, null, CATALOG, "系统设置", null, "el-icon-s-tools", null, null, null, null, false, 200000, null),
//    user(AGENT, catalog_user, MENU, "用户", "/user/user", null, null, null, null, null, false, 16100, null),
//    user_QUERY(AGENT, user, BUTTON, "查询", null, null, "user,userMoney,userAmountTotal,userLevelConfig", null, null, null, false, 16100, null),
//    user_ALL(AGENT, user, BUTTON, "管理", null, null, "#ALL#user,userMoney,userAmountTotal,userLevelConfig", null, null, null, false, 16101, null),
//    userMoney(AGENT, catalog_user, MENU, "用户余额", "/user/userMoney", null, null, null, null, null, false, 16200, null),
//    userMoney_QUERY(AGENT, userMoney, BUTTON, "查询", null, null, "userMoney", null, null, null, false, 16200, null),
//    userMoney_ALL(AGENT, userMoney, BUTTON, "管理", null, null, "#ALL#userMoney", null, null, null, false, 16201, null),
//    userMoneyRecord(AGENT, catalog_user, MENU, "用户余额变动", "/user/userMoneyRecord", null, null, null, null, null, false, 16300, null),
//    userMoneyRecord_QUERY(AGENT, userMoneyRecord, BUTTON, "查询", null, null, "userMoneyRecord", null, null, null, false, 16300, null),
//    userMoneyRecord_ALL(AGENT, userMoneyRecord, BUTTON, "管理", null, null, "#ALL#userMoneyRecord", null, null, null, false, 16301, null),
//    UserAmountTotal(AGENT, catalog_user, MENU, "用户数额总计", "/user/userAmountTotal", null, null, null, null, null, false, 16320, null),
//    UserAmountTotal_QUERY(AGENT, UserAmountTotal, BUTTON, "查询", null, null, "userAmountTotal", null, null, null, false, 16320, null),
//    UserAmountTotal_ALL(AGENT, UserAmountTotal, BUTTON, "管理", null, null, "#ALL#userAmountTotal", null, null, null, false, 16321, null),
//    userRecharge(AGENT, catalog_user, MENU, "用户充值", "/user/userRecharge", null, null, null, null, null, false, 16400, null),
//    userRecharge_QUERY(AGENT, userRecharge, BUTTON, "查询", null, null, "userRecharge", null, null, null, false, 16400, null),
//    userRecharge_ALL(AGENT, userRecharge, BUTTON, "管理", null, null, "#ALL#userRecharge", null, null, null, false, 16401, null),
//    userBankCard(AGENT, catalog_user, MENU, "用户银行卡", "/user/userBankCard", null, null, null, null, null, false, 16500, null),
//    userBankCard_QUERY(AGENT, userBankCard, BUTTON, "查询", null, null, "userBankCard", null, null, null, false, 16500, null),
//    userBankCard_ALL(AGENT, userBankCard, BUTTON, "管理", null, null, "#ALL#userBankCard", null, null, null, false, 16501, null),
//    userWithdraw(AGENT, catalog_user, MENU, "用户提现", "/user/userWithdraw", null, null, null, null, null, false, 16600, null),
//    userWithdraw_QUERY(AGENT, userWithdraw, BUTTON, "查询", null, null, "userWithdraw", null, null, null, false, 16600, null),
//    userWithdraw_ALL(AGENT, userWithdraw, BUTTON, "管理", null, null, "#ALL#userWithdraw,payBank", null, null, null, false, 16601, null),
//    userTransfer(AGENT, catalog_user, MENU, "用户代付", "/user/userTransfer", null, null, null, null, null, false, 16700, null),
//    userTransfer_QUERY(AGENT, userTransfer, BUTTON, "查询", null, null, "userTransfer,payBank", null, null, null, false, 16700, null),
//    userTransfer_ALL(AGENT, userTransfer, BUTTON, "管理", null, null, "#ALL#userTransfer", null, null, null, false, 16701, null),
//    userAddress(AGENT, catalog_user, MENU, "用户地址", "/user/userAddress", null, null, null, null, null, false, 16800, null),
//    userAddress_QUERY(AGENT, userAddress, BUTTON, "查询", null, null, "userAddress", null, null, null, false, 16800, null),
//    userAddress_ALL(AGENT, userAddress, BUTTON, "管理", null, null, "#ALL#userAddress", null, null, null, false, 16801, null),
    userLevelConfig(AGENT, catalog_content, MENU, "用户等级配置", "/user/userLevelConfig", null, null, null, null, null, false, 16900, null),
    userLevelConfig_QUERY(AGENT, userLevelConfig, BUTTON, "查询", null, null, "userLevelConfig", null, null, null, false, 16900, null),
    userLevelConfig_ALL(AGENT, userLevelConfig, BUTTON, "管理", null, null, "#ALL#userLevelConfig", null, null, null, false, 16901, null),
    country(AGENT, catalog_content, MENU, "国家语言", "/content/country", null, null, null, null, null, false, 180100, null),
    country_QUERY(AGENT, country, BUTTON, "查询", null, null, "country", null, null, null, false, 180100, null),
    country_ALL(AGENT, country, BUTTON, "管理", null, null, "#ALL#country", null, null, null, false, 180101, null),
//    customer(AGENT, catalog_content, MENU, "客服", "/content/customer", null, null, null, null, null, false, 180100, null),
//    customer_QUERY(AGENT, customer, BUTTON, "查询", null, null, "customer", null, null, null, false, 180100, null),
//    customer_ALL(AGENT, customer, BUTTON, "管理", null, null, "#ALL#customer", null, null, null, false, 180101, null),
    banner(AGENT, catalog_content, MENU, "导航广告栏", "/content/banner", null, null, null, null, null, false, 180300, null),
    banner_QUERY(AGENT, banner, BUTTON, "查询", null, null, "banner", null, null, null, false, 180300, null),
    banner_ALL(AGENT, banner, BUTTON, "管理", null, null, "#ALL#banner", null, null, null, false, 180301, null),
    poster(AGENT, catalog_content, MENU, "文章", "/content/poster", null, null, null, null, null, false, 180400, null),
    poster_QUERY(AGENT, poster, BUTTON, "查询", null, null, "poster", null, null, null, false, 180400, null),
    poster_ALL(AGENT, poster, BUTTON, "管理", null, null, "#ALL#poster", null, null, null, false, 180401, null),
    posterCategory(AGENT, catalog_content, MENU, "文章分类", "/content/posterCategory", null, null, null, null, null, false, 180500, null),
    posterCategory_QUERY(AGENT, posterCategory, BUTTON, "查询", null, null, "posterCategory", null, null, null, false, 180500, null),
    posterCategory_ALL(AGENT, posterCategory, BUTTON, "管理", null, null, "#ALL#posterCategory", null, null, null, false, 180501, null),
    sysMsgChannelMerchant(AGENT, catalog_content, MENU, "消息通道商户", "/content/sysMsgChannelMerchant", null, null, null, null, null, false, 180600, null),
    sysMsgChannelMerchant_QUERY(AGENT, sysMsgChannelMerchant, BUTTON, "查询", null, null, "sysMsgChannelMerchant", null, null, null, false, 180600, null),
    sysMsgChannelMerchant_ALL(AGENT, sysMsgChannelMerchant, BUTTON, "管理", null, null, "#ALL#sysMsgChannelMerchant", null, null, null, false, 180601, null),
    sysMsgChannelTemplate(AGENT, catalog_content, MENU, "消息通道模板", "/content/sysMsgChannelTemplate", null, null, null, null, null, false, 180700, null),
    sysMsgChannelTemplate_QUERY(AGENT, sysMsgChannelTemplate, BUTTON, "查询", null, null, "sysMsgChannelTemplate", null, null, null, false, 180700, null),
    sysMsgChannelTemplate_ALL(AGENT, sysMsgChannelTemplate, BUTTON, "管理", null, null, "#ALL#sysMsgChannelTemplate", null, null, null, false, 180701, null),
//    sysMsgRecord(AGENT, catalog_content, MENU, "消息记录", "/content/sysMsgRecord", null, null, null, null, null, false, 180800, null),
//    sysMsgRecord_QUERY(AGENT, sysMsgRecord, BUTTON, "查询", null, null, "sysMsgRecord", null, null, null, false, 180800, null),
//    sysMsgRecord_ALL(AGENT, sysMsgRecord, BUTTON, "管理", null, null, "#ALL#sysMsgRecord", null, null, null, false, 180801, null),
    translation(AGENT, catalog_content, MENU, "自定义翻译", "/content/translation", null, null, null, null, null, false, 180900, null),
    translation_QUERY(AGENT, translation, BUTTON, "查询", null, null, "translation", null, null, null, false, 180900, null),
    translation_ALL(AGENT, translation, BUTTON, "管理", null, null, "#ALL#translation", null, null, null, false, 180901, null),
    sysAddress(AGENT, catalog_content, MENU, "地址", "/content/sysAddress", null, null, null, null, null, false, 181100, null),
    sysAddress_QUERY(AGENT, sysAddress, BUTTON, "查询", null, null, "sysAddress", null, null, null, false, 181100, null),
    sysAddress_ALL(AGENT, sysAddress, BUTTON, "管理", null, null, "#ALL#sysAddress", null, null, null, false, 181101, null),
    inputField(AGENT, catalog_content, MENU, "定制属性", "/content/inputField", null, null, null, null, null, false, 181200, null),
    inputField_QUERY(AGENT, inputField, BUTTON, "查询", null, null, "inputField", null, null, null, false, 181200, null),
    inputField_ALL(AGENT, inputField, BUTTON, "管理", null, null, "#ALL#inputField", null, null, null, false, 181201, null),
    payChannel(AGENT, catalog_pay, MENU, "充值渠道", "/pay/payChannel", null, null, null, null, null, false, 190100, null),
    payChannel_QUERY(AGENT, payChannel, BUTTON, "查询", null, null, "payChannel", null, null, null, false, 190100, null),
    payChannel_ALL(AGENT, payChannel, BUTTON, "管理", null, null, "#ALL#payChannel", null, null, null, false, 190101, null),
    payoutChannel(AGENT, catalog_pay, MENU, "代付渠道", "/pay/payoutChannel", null, null, null, null, null, false, 190200, null),
    payoutChannel_QUERY(AGENT, payoutChannel, BUTTON, "查询", null, null, "payoutChannel", null, null, null, false, 190200, null),
    payoutChannel_ALL(AGENT, payoutChannel, BUTTON, "管理", null, null, "#ALL#payoutChannel", null, null, null, false, 190201, null),
    payMerchant(AGENT, catalog_pay, MENU, "商户", "/pay/payMerchant", null, null, null, null, null, false, 190300, null),
    payMerchant_QUERY(AGENT, payMerchant, BUTTON, "查询", null, null, "payMerchant", null, null, null, false, 190300, null),
    payMerchant_ALL(AGENT, payMerchant, BUTTON, "管理", null, null, "#ALL#payMerchant", null, null, null, false, 190301, null),
    payBank(AGENT, catalog_pay, MENU, "商户支持的银行", "/pay/payBank", null, null, null, null, null, false, 190400, null),
    payBank_QUERY(AGENT, payBank, BUTTON, "查询", null, null, "payBank", null, null, null, false, 190400, null),
    payBank_ALL(AGENT, payBank, BUTTON, "管理", null, null, "#ALL#payBank", null, null, null, false, 190401, null),
    payCardType(AGENT, catalog_pay, MENU, "站内卡号类型", "/pay/payCardType", null, null, null, null, null, false, 190500, null),
    payCardType_QUERY(AGENT, payCardType, BUTTON, "查询", null, null, "payCardType", null, null, null, false, 190500, null),
    payCardType_ALL(AGENT, payCardType, BUTTON, "管理", null, null, "#ALL#payCardType", null, null, null, false, 190501, null),
    authDict(AGENT, catalog_config, MENU, "字典", "/sys/authDict", null, null, null, null, null, false, 200100, null),
    authDict_QUERY(AGENT, authDict, BUTTON, "查询", null, null, "authDict", null, null, null, false, 200100, null),
    authDict_ALL(AGENT, authDict, BUTTON, "管理", null, null, "#ALL#authDict", null, null, null, false, 200101, null),
//    agent(AGENT, catalog_config, MENU, "代理", "/sys/agent", null, null, null, null, null, false, 200300, null),
//    agent_QUERY(AGENT, agent, BUTTON, "查询", null, null, "agent", null, null, null, false, 200300, null),
//    agent_ALL(AGENT, agent, BUTTON, "管理", null, null, "#ALL#agent", null, null, null, false, 200301, null),
    admin(AGENT, catalog_config, MENU, "管理员", "/sys/admin", null, null, null, null, null, false, 200400, null),
    admin_QUERY(AGENT, admin, BUTTON, "查询", null, null, "admin", null, null, null, false, 200400, null),
    admin_ALL(AGENT, admin, BUTTON, "管理", null, null, "#ALL#admin", null, null, null, false, 200401, null),
    authRoute(AGENT, catalog_config, MENU, "菜单", "/sys/authRoute", null, null, null, null, null, false, 200500, null),
    authRoute_QUERY(AGENT, authRoute, BUTTON, "查询", null, null, "authRoute", null, null, null, false, 200500, null),
    authRoute_ALL(AGENT, authRoute, BUTTON, "管理", null, null, "#ALL#authRoute", null, null, null, false, 200501, null),
    authRole(AGENT, catalog_config, MENU, "角色", "/sys/authRole", null, null, null, null, null, false, 200600, null),
    authRole_QUERY(AGENT, authRole, BUTTON, "查询", null, null, "authRole", null, null, null, false, 200600, null),
    authRole_ALL(AGENT, authRole, BUTTON, "管理", null, null, "#ALL#authRole", null, null, null, false, 200601, null),

//    posterCode(AGENT, catalog_content, MENU, "固有文章编号", "/content/posterCode", null, null, null, null, null, false, 180800, null),
//    authRoleRoute(AGENT, catalog_config, MENU, "角色权限", "/authRoleRoute", null, null, null, null, null, null, null),

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
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;

    @Override
    public String getToDbCode() {
        return sysRole + CommonConst.Str.SPLIT + name();
    }
}
