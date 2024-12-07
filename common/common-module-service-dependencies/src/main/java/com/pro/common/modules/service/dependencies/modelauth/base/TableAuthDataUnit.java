package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumMethodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.function.Function;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TableAuthDataUnit<ENTITY> {
    public static final String AUTH_ALL = "$$AUTH_ALL$$";
    public static final String AUTH_NULL = "$$AUTH_NULL$$";
    // 生效条件
    private Function<ENTITY, Boolean> condition;
    private EnumTableAuthDataType type;
    private EnumMethodType method;
    private EnumSysRole role;
    private String propName;
//    private Method setMethod;
//    private MultiFunction<ENTITY, Object> getFun;
}
