package com.pro.common.module.api.common.model.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章容器样式类型
 */
@AllArgsConstructor
@Getter
public enum EnumPosterContainerType implements IEnum {

    _default("透明底无边框"),
    card("白底灰框卡片"),
    ;

    private String label;

}
