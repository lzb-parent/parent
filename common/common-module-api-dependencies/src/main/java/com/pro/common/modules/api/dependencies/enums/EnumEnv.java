package com.pro.common.modules.api.dependencies.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统环境
 */
@Getter
@AllArgsConstructor
public enum EnumEnv implements IEnum {
    dev("本地"),
    test("测试"),
    prod("线上"),
    ;

    String label;
//    public static final Map<String, EnumEnv> MAP = Arrays.stream(values()).collect(Collectors.toMap(EnumEnv::name, o -> o));
}
