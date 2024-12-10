package com.pro.common.module.api.system.model.enums;

import com.pro.common.module.api.system.model.util.AuthDictUtil;

import java.math.BigDecimal;

/**
 * 字典枚举
 */
public interface IEnumAuthDict {
    String name();

    default String getValueCache() {
        return AuthDictUtil.getValueCache(this);
    }

    default Boolean getBoolean() {
        String value = getValueCache();
        return "1".equals(value);
    }
    default BigDecimal getBigDecimal() {
        String value = getValueCache();
        return null != value ? new BigDecimal(value) : BigDecimal.ZERO;
    }
    default Integer getInteger() {
        String value = getValueCache();
        return null != value ? Integer.parseInt(value) : 0;
    }
    default Long getLong() {
        String value = getValueCache();
        return null != value ? Long.parseLong(value) : 0L;
    }

    default String getValueDb() {
        return AuthDictUtil.getValue(this);
    }
    default <T> T getValueCacheOrDefault(T t) {
        return AuthDictUtil.getValueCacheOrDefault(this, t);
    }
}
