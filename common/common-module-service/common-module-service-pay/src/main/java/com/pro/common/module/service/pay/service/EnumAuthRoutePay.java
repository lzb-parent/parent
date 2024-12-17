package com.pro.common.module.service.pay.service;

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
public enum EnumAuthRoutePay implements IEnumToDbEnum {
    catalog_user(ADMIN, null, CATALOG, "用户管理", null, "el-icon-user-solid", null, null, null, null, null, 16000, null),
    catalog_pay(ADMIN, null, CATALOG, "支付管理", null, "el-icon-s-goods", null, null, null, null, null, 190000, null),
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

//    posterCode(ADMIN, catalog_content, MENU, "固有文章编号", "/content/posterCode", null, null, null, null, null, null, 180800, null),
//    authRoleRoute(ADMIN, catalog_config, MENU, "角色权限", "/authRoleRoute", null, null, null, null, null, null, null),

    ;
    private final EnumSysRole sysRole;
    private final EnumAuthRoutePay pcode;
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
