package com.pro.common.module.api.pay.enums;

import com.pro.common.module.api.pay.model.db.PayCardType;
import com.pro.framework.api.enums.IEnum;
import com.pro.framework.api.enums.IEnumToDbEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字典配置 定制
 */
@Getter
@AllArgsConstructor
public enum EnumPayCardType implements IEnumToDbEnum<PayCardType>, IEnum {
    _1("BANK", "银行卡", null),
    ;
    private final String code;
    private final String name;

    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;

    @Override
    public String getLabel() {
        return name;
    }
}
