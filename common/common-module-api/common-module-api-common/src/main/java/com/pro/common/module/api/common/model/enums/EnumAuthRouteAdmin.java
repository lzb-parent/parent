package com.pro.common.module.api.common.model.enums;

import com.pro.common.module.api.common.model.db.AuthRoute;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.module.api.common.model.enums.EnumAuthRouteType.*;
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
public enum EnumAuthRouteAdmin implements IEnumToDbEnum<AuthRoute> {
    dashboard(ADMIN, null, MENU, "面板", "/dashboard", "el-icon-home", null, null, null, null, null, 10100, null),
    count(ADMIN, null, MENU, "统计", "/count", "el-icon-s-data", "user,userMoneyRecord", null, null, null, null, 10200, null),
    catalog_user(ADMIN, null, CATALOG, "用户管理", null, "el-icon-user-solid", null, null, null, null, null, 16000, null),
    catalog_content(ADMIN, null, CATALOG, "站点文案管理", null, "el-icon-document", null, null, null, null, null, 180000, null),
    catalog_pay(ADMIN, null, CATALOG, "支付管理", null, "el-icon-s-goods", null, null, null, null, null, 190000, null),
    catalog_config(ADMIN, null, CATALOG, "系统设置", null, "el-icon-s-tools", null, null, null, null, null, 200000, null),
    user(ADMIN, catalog_user, MENU, "用户", "/user/user", null, null, null, null, null, null, 16100, null),
    user_QUERY(ADMIN, user, BUTTON, "查询", null, null, "user,userMoney,userAmountTotal,userLevelConfig", null, null, null, null, 16100, null),
    user_ALL(ADMIN, user, BUTTON, "管理", null, null, "#ALL#user,userMoney,userAmountTotal,userLevelConfig", null, null, null, null, 16101, null),
    userMoney(ADMIN, catalog_user, MENU, "用户余额", "/user/userMoney", null, null, null, null, null, null, 16200, null),
    userMoney_QUERY(ADMIN, userMoney, BUTTON, "查询", null, null, "userMoney", null, null, null, null, 16200, null),
    userMoney_ALL(ADMIN, userMoney, BUTTON, "管理", null, null, "#ALL#userMoney", null, null, null, null, 16201, null),
    userMoneyRecord(ADMIN, catalog_user, MENU, "用户余额变动", "/user/userMoneyRecord", null, null, null, null, null, null, 16300, null),
    userMoneyRecord_QUERY(ADMIN, userMoneyRecord, BUTTON, "查询", null, null, "userMoneyRecord", null, null, null, null, 16300, null),
    userMoneyRecord_ALL(ADMIN, userMoneyRecord, BUTTON, "管理", null, null, "#ALL#userMoneyRecord", null, null, null, null, 16301, null),
    UserAmountTotal(ADMIN, catalog_user, MENU, "用户数额总计", "/user/userAmountTotal", null, null, null, null, null, null, 16320, null),
    UserAmountTotal_QUERY(ADMIN, UserAmountTotal, BUTTON, "查询", null, null, "userAmountTotal", null, null, null, null, 16320, null),
    UserAmountTotal_ALL(ADMIN, UserAmountTotal, BUTTON, "管理", null, null, "#ALL#userAmountTotal", null, null, null, null, 16321, null),
    userRecharge(ADMIN, catalog_user, MENU, "用户充值", "/user/userRecharge", null, null, null, null, null, null, 16400, null),
    userRecharge_QUERY(ADMIN, userRecharge, BUTTON, "查询", null, null, "userRecharge", null, null, null, null, 16400, null),
    userRecharge_ALL(ADMIN, userRecharge, BUTTON, "管理", null, null, "#ALL#userRecharge", null, null, null, null, 16401, null),
    userBankCard(ADMIN, catalog_user, MENU, "用户银行卡", "/user/userBankCard", null, null, null, null, null, null, 16500, null),
    userBankCard_QUERY(ADMIN, userBankCard, BUTTON, "查询", null, null, "userBankCard", null, null, null, null, 16500, null),
    userBankCard_ALL(ADMIN, userBankCard, BUTTON, "管理", null, null, "#ALL#userBankCard", null, null, null, null, 16501, null),
    userWithdraw(ADMIN, catalog_user, MENU, "用户提现", "/user/userWithdraw", null, null, null, null, null, null, 16600, null),
    userWithdraw_QUERY(ADMIN, userWithdraw, BUTTON, "查询", null, null, "userWithdraw", null, null, null, null, 16600, null),
    userWithdraw_ALL(ADMIN, userWithdraw, BUTTON, "管理", null, null, "#ALL#userWithdraw,payBank", null, null, null, null, 16601, null),
    userTransfer(ADMIN, catalog_user, MENU, "用户代付", "/user/userTransfer", null, null, null, null, null, null, 16700, null),
    userTransfer_QUERY(ADMIN, userTransfer, BUTTON, "查询", null, null, "userTransfer,payBank", null, null, null, null, 16700, null),
    userTransfer_ALL(ADMIN, userTransfer, BUTTON, "管理", null, null, "#ALL#userTransfer", null, null, null, null, 16701, null),
    userAddress(ADMIN, catalog_user, MENU, "用户地址", "/user/userAddress", null, null, null, null, null, null, 16800, null),
    userAddress_QUERY(ADMIN, userAddress, BUTTON, "查询", null, null, "userAddress", null, null, null, null, 16800, null),
    userAddress_ALL(ADMIN, userAddress, BUTTON, "管理", null, null, "#ALL#userAddress", null, null, null, null, 16801, null),
    userLevelConfig(ADMIN, catalog_content, MENU, "用户等级配置", "/user/userLevelConfig", null, null, null, null, null, null, 16900, null),
    userLevelConfig_QUERY(ADMIN, userLevelConfig, BUTTON, "查询", null, null, "userLevelConfig", null, null, null, null, 16900, null),
    userLevelConfig_ALL(ADMIN, userLevelConfig, BUTTON, "管理", null, null, "#ALL#userLevelConfig", null, null, null, null, 16901, null),
    country(ADMIN, catalog_content, MENU, "国家语言", "/content/country", null, null, null, null, null, null, 180100, null),
    country_QUERY(ADMIN, country, BUTTON, "查询", null, null, "country", null, null, null, null, 180100, null),
    country_ALL(ADMIN, country, BUTTON, "管理", null, null, "#ALL#country", null, null, null, null, 180101, null),
    customer(ADMIN, catalog_content, MENU, "客服", "/content/customer", null, null, null, null, null, null, 180100, null),
    customer_QUERY(ADMIN, customer, BUTTON, "查询", null, null, "customer", null, null, null, null, 180100, null),
    customer_ALL(ADMIN, customer, BUTTON, "管理", null, null, "#ALL#customer", null, null, null, null, 180101, null),
    banner(ADMIN, catalog_content, MENU, "导航广告栏", "/content/banner", null, null, null, null, null, null, 180300, null),
    banner_QUERY(ADMIN, banner, BUTTON, "查询", null, null, "banner", null, null, null, null, 180300, null),
    banner_ALL(ADMIN, banner, BUTTON, "管理", null, null, "#ALL#banner", null, null, null, null, 180301, null),
    poster(ADMIN, catalog_content, MENU, "文章", "/content/poster", null, null, null, null, null, null, 180400, null),
    poster_QUERY(ADMIN, poster, BUTTON, "查询", null, null, "poster", null, null, null, null, 180400, null),
    poster_ALL(ADMIN, poster, BUTTON, "管理", null, null, "#ALL#poster", null, null, null, null, 180401, null),
    posterCategory(ADMIN, catalog_content, MENU, "文章分类", "/content/posterCategory", null, null, null, null, null, null, 180500, null),
    posterCategory_QUERY(ADMIN, posterCategory, BUTTON, "查询", null, null, "posterCategory", null, null, null, null, 180500, null),
    posterCategory_ALL(ADMIN, posterCategory, BUTTON, "管理", null, null, "#ALL#posterCategory", null, null, null, null, 180501, null),
    sysMsgChannelMerchant(ADMIN, catalog_content, MENU, "消息通道商户", "/content/sysMsgChannelMerchant", null, null, null, null, null, null, 180600, null),
    sysMsgChannelMerchant_QUERY(ADMIN, sysMsgChannelMerchant, BUTTON, "查询", null, null, "sysMsgChannelMerchant", null, null, null, null, 180600, null),
    sysMsgChannelMerchant_ALL(ADMIN, sysMsgChannelMerchant, BUTTON, "管理", null, null, "#ALL#sysMsgChannelMerchant", null, null, null, null, 180601, null),
    sysMsgChannelTemplate(ADMIN, catalog_content, MENU, "消息通道模板", "/content/sysMsgChannelTemplate", null, null, null, null, null, null, 180700, null),
    sysMsgChannelTemplate_QUERY(ADMIN, sysMsgChannelTemplate, BUTTON, "查询", null, null, "sysMsgChannelTemplate", null, null, null, null, 180700, null),
    sysMsgChannelTemplate_ALL(ADMIN, sysMsgChannelTemplate, BUTTON, "管理", null, null, "#ALL#sysMsgChannelTemplate", null, null, null, null, 180701, null),
    sysMsgRecord(ADMIN, catalog_content, MENU, "消息记录", "/content/sysMsgRecord", null, null, null, null, null, null, 180800, null),
    sysMsgRecord_QUERY(ADMIN, sysMsgRecord, BUTTON, "查询", null, null, "sysMsgRecord", null, null, null, null, 180800, null),
    sysMsgRecord_ALL(ADMIN, sysMsgRecord, BUTTON, "管理", null, null, "#ALL#sysMsgRecord", null, null, null, null, 180801, null),
    translation(ADMIN, catalog_content, MENU, "自定义翻译", "/content/translation", null, null, null, null, null, null, 180900, null),
    translation_QUERY(ADMIN, translation, BUTTON, "查询", null, null, "translation", null, null, null, null, 180900, null),
    translation_ALL(ADMIN, translation, BUTTON, "管理", null, null, "#ALL#translation", null, null, null, null, 180901, null),
    sysAddress(ADMIN, catalog_content, MENU, "地址", "/content/sysAddress", null, null, null, null, null, null, 181100, null),
    sysAddress_QUERY(ADMIN, sysAddress, BUTTON, "查询", null, null, "sysAddress", null, null, null, null, 181100, null),
    sysAddress_ALL(ADMIN, sysAddress, BUTTON, "管理", null, null, "#ALL#sysAddress", null, null, null, null, 181101, null),
    inputField(ADMIN, catalog_content, MENU, "定制属性", "/content/inputField", null, null, null, null, null, null, 181200, null),
    inputField_QUERY(ADMIN, inputField, BUTTON, "查询", null, null, "inputField", null, null, null, null, 181200, null),
    inputField_ALL(ADMIN, inputField, BUTTON, "管理", null, null, "#ALL#inputField", null, null, null, null, 181201, null),
    payChannel(ADMIN, catalog_pay, MENU, "充值渠道", "/pay/payChannel", null, null, null, null, null, null, 190100, null),
    payChannel_QUERY(ADMIN, payChannel, BUTTON, "查询", null, null, "payChannel", null, null, null, null, 190100, null),
    payChannel_ALL(ADMIN, payChannel, BUTTON, "管理", null, null, "#ALL#payChannel", null, null, null, null, 190101, null),
    payoutChannel(ADMIN, catalog_pay, MENU, "代付渠道", "/pay/payoutChannel", null, null, null, null, null, null, 190200, null),
    payoutChannel_QUERY(ADMIN, payoutChannel, BUTTON, "查询", null, null, "payoutChannel", null, null, null, null, 190200, null),
    payoutChannel_ALL(ADMIN, payoutChannel, BUTTON, "管理", null, null, "#ALL#payoutChannel", null, null, null, null, 190201, null),
    payMerchant(ADMIN, catalog_pay, MENU, "商户", "/pay/payMerchant", null, null, null, null, null, null, 190300, null),
    payMerchant_QUERY(ADMIN, payMerchant, BUTTON, "查询", null, null, "payMerchant", null, null, null, null, 190300, null),
    payMerchant_ALL(ADMIN, payMerchant, BUTTON, "管理", null, null, "#ALL#payMerchant", null, null, null, null, 190301, null),
    payBank(ADMIN, catalog_pay, MENU, "商户支持的银行", "/pay/payBank", null, null, null, null, null, null, 190400, null),
    payBank_QUERY(ADMIN, payBank, BUTTON, "查询", null, null, "payBank", null, null, null, null, 190400, null),
    payBank_ALL(ADMIN, payBank, BUTTON, "管理", null, null, "#ALL#payBank", null, null, null, null, 190401, null),
    payCardType(ADMIN, catalog_pay, MENU, "站内卡号类型", "/pay/payCardType", null, null, null, null, null, null, 190500, null),
    payCardType_QUERY(ADMIN, payCardType, BUTTON, "查询", null, null, "payCardType", null, null, null, null, 190500, null),
    payCardType_ALL(ADMIN, payCardType, BUTTON, "管理", null, null, "#ALL#payCardType", null, null, null, null, 190501, null),
    authDict(ADMIN, catalog_config, MENU, "字典", "/sys/authDict", null, null, null, null, null, null, 200100, null),
    authDict_QUERY(ADMIN, authDict, BUTTON, "查询", null, null, "authDict", null, null, null, null, 200100, null),
    authDict_ALL(ADMIN, authDict, BUTTON, "管理", null, null, "#ALL#authDict", null, null, null, null, 200101, null),
    agent(ADMIN, catalog_config, MENU, "代理", "/sys/agent", null, null, null, null, null, null, 200300, null),
    agent_QUERY(ADMIN, agent, BUTTON, "查询", null, null, "agent", null, null, null, null, 200300, null),
    agent_ALL(ADMIN, agent, BUTTON, "管理", null, null, "#ALL#agent", null, null, null, null, 200301, null),
    admin(ADMIN, catalog_config, MENU, "管理员", "/sys/admin", null, null, null, null, null, null, 200400, null),
    admin_QUERY(ADMIN, admin, BUTTON, "查询", null, null, "admin", null, null, null, null, 200400, null),
    admin_ALL(ADMIN, admin, BUTTON, "管理", null, null, "#ALL#admin", null, null, null, null, 200401, null),
    authRoute(ADMIN, catalog_config, MENU, "菜单", "/sys/authRoute", null, null, null, null, null, null, 200500, null),
    authRoute_QUERY(ADMIN, authRoute, BUTTON, "查询", null, null, "authRoute", null, null, null, null, 200500, null),
    authRoute_ALL(ADMIN, authRoute, BUTTON, "管理", null, null, "#ALL#authRoute", null, null, null, null, 200501, null),
    authRole(ADMIN, catalog_config, MENU, "角色", "/sys/authRole", null, null, null, null, null, null, 200600, null),
    authRole_QUERY(ADMIN, authRole, BUTTON, "查询", null, null, "authRole", null, null, null, null, 200600, null),
    authRole_ALL(ADMIN, authRole, BUTTON, "管理", null, null, "#ALL#authRole", null, null, null, null, 200601, null),

//    posterCode(ADMIN, catalog_content, MENU, "固有文章编号", "/content/posterCode", null, null, null, null, null, null, 180800, null),
//    authRoleRoute(ADMIN, catalog_config, MENU, "角色权限", "/authRoleRoute", null, null, null, null, null, null, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRouteAdmin pcode;
    private final EnumAuthRouteType type;
    private final String name;
    private final String componentPath;
    private final String icon;
    private String permissionPaths;
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
