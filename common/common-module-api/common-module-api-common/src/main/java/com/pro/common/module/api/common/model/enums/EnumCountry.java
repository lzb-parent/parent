package com.pro.common.module.api.common.model.enums;

import com.pro.common.module.api.common.model.db.Country;
import com.pro.framework.api.enums.IEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

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
public enum EnumCountry implements IEnumToDbEnum<Country>, IEnum {
    _1("CHN_1", null, "简体中文", "中国", "zh-CN", "简体中文", "简体中文", "86", "CHN", "CNY", "¥", BigDecimal.valueOf(1 / 6.8), "Asia/Shanghai", 100,false, null),
    _2("USA_1", null, "English", "美国", "en-US", "English", "英语", "1", "USA", "USD", "$", BigDecimal.valueOf(1), "America/New_York", 200,true, null),
    _3("CHN_2", null, "繁体中文", "中国台湾", "zh-TW", "繁体中文", "繁体中文", "", "", "", "", BigDecimal.valueOf(1 / 6.8), "Asia/Shanghai", 300,true, null),
    ;
    private String code;
    private String icon;
    private String name;
    private String cname;
    private String langCode;
    private String langName;
    private String langCnName;
    private String phoneCode;
    private String countryCode;
    private String currencyCode;
    private String currencySymbol;
    private BigDecimal exchangeRate;
    private String zoneCode;
    private Integer sort;
    private Boolean showAdmin;
    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;

    @Override
    public String getLabel() {
        return cname;
    }
}
