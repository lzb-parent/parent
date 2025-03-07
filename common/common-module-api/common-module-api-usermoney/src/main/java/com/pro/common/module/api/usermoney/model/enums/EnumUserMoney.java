package com.pro.common.module.api.usermoney.model.enums;

import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.annotation.JTDField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

/**
 * 路由配置 基础
 */
@Getter
@AllArgsConstructor
public enum EnumUserMoney implements IEnumToDbEnum<UserMoney> {
    // 密码123456
    _1(1L,"13800000000", true,new BigDecimal(10000), null),
    ;
    private Long userId;
    private String username;
    private Boolean isDemo;
    private BigDecimal amount;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
