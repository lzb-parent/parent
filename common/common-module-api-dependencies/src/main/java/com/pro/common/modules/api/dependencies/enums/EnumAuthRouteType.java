package com.pro.common.modules.api.dependencies.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型
 */
@Getter
@AllArgsConstructor
public enum EnumAuthRouteType implements IEnum {

    CATALOG(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮"),
    URI(4, "接口"),

    ;

    final Integer type;
    final String label;
}
