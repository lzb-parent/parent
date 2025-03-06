package com.pro.common.module.api.usermoney.model.enums;

import com.pro.common.module.api.usermoney.model.db.UserAmountTotal;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
public enum EnumUserAmountTotal implements IEnumToDbEnum<UserAmountTotal> {
    // 密码123456
    _1(1L,"13800000000", true,null,null,null,null,null,null,null,null,null,null),
    ;
    private Long userId;
    private String username;
    private Boolean isDemo;
    private BigDecimal todayCommissionMoney;
    private BigDecimal commissionMoney;
    private BigDecimal todayRechargeMoney;
    private BigDecimal totalRechargeMoney;
    private Integer totalRechargeTimes;
    private BigDecimal todayTkMoney;
    private BigDecimal totalTkMoney;
    private Integer todayTkTimes;
    private Integer totalTkTimes;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
