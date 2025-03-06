package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumTableAuthDataType implements IEnum {
    AUTH("添加权限"),
    UN_AUTH("删除权限"),
    ;
    String label;
}
