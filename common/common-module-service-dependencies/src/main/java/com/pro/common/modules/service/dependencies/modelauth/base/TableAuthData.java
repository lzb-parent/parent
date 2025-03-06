package com.pro.common.modules.service.dependencies.modelauth.base;

import cn.hutool.core.util.ObjUtil;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.framework.api.enums.EnumMethodType;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.util.inner.FunSerializable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class TableAuthData<ENTITY extends IModel> {

    // 生效条件
    private Function<ENTITY, Boolean> condition;
    // 授权/取消授权
    private EnumTableAuthDataType type;
    // 增/删/改/查
    private List<EnumMethodType> methods;
    // anonymous/user/agent/admin
    private List<EnumSysRole> roles;
    // 属性
    private List<FunSerializable<ENTITY, Object>> props;

    public TableAuthData(
            Function<ENTITY, Boolean> condition,
            EnumTableAuthDataType type,
            List<EnumMethodType> methods,
            List<EnumSysRole> roles,
            List<FunSerializable<ENTITY, Object>> props
    ) {
        this.condition = null == condition ? (e) -> true : condition;
        this.type = null == type ? EnumTableAuthDataType.AUTH : type;
        this.methods = null == methods ? METHOD_ALL : methods;
        this.roles = null == roles ? ROLE_ALL : roles;
        this.props = props;
    }

    public static final List<EnumMethodType> METHOD_ALL = Arrays.stream(EnumMethodType.values()).collect(Collectors.toList());
    public static final List<EnumSysRole> ROLE_ALL = Arrays.stream(EnumSysRole.values()).collect(Collectors.toList());
    public static final List<FunSerializable<?, Object>> AUTH_PROP_ALL = new ArrayList<>(0);
    public static final List<FunSerializable<?, Object>> AUTH_PROP_NULL = new ArrayList<>(0);

    /**
     * 隐藏属性 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> unAuth(
            List<FunSerializable<ENTITY, Object>> props
    ) {
        return new TableAuthData<>(null, EnumTableAuthDataType.UN_AUTH, null, List.of(
                EnumSysRole.ANONYMOUS,
                EnumSysRole.USER,
                EnumSysRole.AGENT
        ), props);
    }

//    /**
//     * 关闭所有访问
//     */
//    public static <ENTITY extends IModel> TableAuthData<ENTITY> unAuthAll(Function<ENTITY, Boolean> condition) {
//        return new TableAuthData<>(condition, EnumTableAuthDataType.AUTH, null, null, (List) AUTH_PROP_NULL);
//    }

    /**
     * 关闭所有访问
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> unAuthAll() {
        return new TableAuthData<>(null, EnumTableAuthDataType.AUTH, null, null, (List) AUTH_PROP_NULL);
    }

    /**
     * 授权更新 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> all() {
        return new TableAuthData<>(null, null, null, null, (List) AUTH_PROP_ALL);
    }

    /**
     * 授权更新 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> query(
            Function<ENTITY, Boolean> condition,
            List<EnumSysRole> roles,
            List<FunSerializable<ENTITY, Object>> props
    ) {
        return new TableAuthData<>(condition, null, Collections.singletonList(EnumMethodType.QUERY), roles, ObjUtil.defaultIfNull(props,(List)TableAuthData.AUTH_PROP_ALL));
    }
    /**
     * 授权更新 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> update(
            Function<ENTITY, Boolean> condition,
            List<FunSerializable<ENTITY, Object>> props
    ) {
        return new TableAuthData<>(condition, null, Collections.singletonList(EnumMethodType.UPDATE), null, props);
    }

    /**
     * 授权更新 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> update(
            Function<ENTITY, Boolean> condition,
            List<EnumSysRole> roles,
            List<FunSerializable<ENTITY, Object>> props
    ) {
        return new TableAuthData<>(condition, null, Collections.singletonList(EnumMethodType.UPDATE), null, props);
    }

    /**
     * 授权删除 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> delete(EnumSysRole... roles) {
        return new TableAuthData<>(null, null, null, Arrays.asList(roles), Arrays.asList(ENTITY::getId));
    }

    /**
     * 授权删除 常用方法
     */
    public static <ENTITY extends IModel> TableAuthData<ENTITY> delete() {
        return new TableAuthData<>(null, null, null, Arrays.asList(EnumSysRole.values()), Arrays.asList(ENTITY::getId));
    }
}
