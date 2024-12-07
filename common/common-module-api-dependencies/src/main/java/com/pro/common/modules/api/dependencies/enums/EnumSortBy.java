package com.pro.common.modules.api.dependencies.enums;


import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 排序类型
 */
@AllArgsConstructor
@Getter
public enum EnumSortBy implements IEnum {
    random("随机"),
    sort("按排序设置"),
    ;
    final String label;
}
