package com.pro.common.modules.service.dependencies.modelauth.base;

import cn.hutool.core.util.ClassUtil;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.common.modules.api.dependencies.model.classes.IUserDataClass;
import com.pro.common.modules.api.dependencies.model.classes.IUserOrderClass;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.api.clazz.ClassCaches;
import com.pro.framework.api.enums.EnumMethodType;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.structure.Tuple3;
import com.pro.framework.api.util.LambdaUtil;
import com.pro.framework.api.util.inner.FunSerializable;
import com.pro.framework.api.util.inner.SerializedLambdaData;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 新增,修改,删除 的默认权限:
 * 配置类
 * admin 有
 * agent,user 没有
 * 数据类
 * admin,agent,user 没有
 * 查询 的默认权限:
 * 配置类
 * admin,user,agent 有
 * 数据类
 * admin 有
 * agent,user 有,但过滤自己的
 */
@Getter
@Slf4j
public class AuthService<ENTITY extends IModel> {
    private Class<ENTITY> entityClass;
    private Map<String, Tuple3<Field, Method, Method>> classInfosFull;
    private Map<EnumSysRole, Map<EnumMethodType, Map<EnumTableAuthDataType, List<TableAuthDataUnit<ENTITY>>>>> setMap;

    protected List<TableAuthData<ENTITY>> getConfigs() {
        return Collections.emptyList();
    }

//    @PostConstruct
    public void reload() {
        List<TableAuthData<ENTITY>> datas = getConfigs();
        try {
            //noinspection unchecked
            Class<ENTITY> entityClassRead = (Class<ENTITY>) ClassUtil.getTypeArgument(this.getClass());
            if (entityClassRead == null) {
//                log.warn("AuthService reload entityClass is null: {}", entityClass);
            } else {
                this.entityClass = entityClassRead;
            }
            this.classInfosFull = ClassCaches.getClassInfosFull(entityClass);
            this.setMap = this.initSetMap(datas);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload(Class<?> entityClass) {
        this.entityClass = (Class<ENTITY>) entityClass;
        this.classInfosFull = ClassCaches.getClassInfosFull(entityClass);
        this.setMap = Collections.emptyMap();
    }

    public List<ENTITY> fillNull(ENTITY entityOld, List<ENTITY> list, EnumSysRole role, EnumMethodType method) {
        Set<String> fullProps = new HashSet<>(classInfosFull.keySet());
        // 默认权限
        Set<String> props = this.getPropsDefault(role, method, fullProps);
        // 过滤行
        list = this.fillList(list, role, method, EnumTableAuthDataType.AUTH);
//        ENTITY entityFirst = list.isEmpty() ? null : list.get(0);
        // 定制权限
        Set<String> propsAuth = this.getProps(entityOld, role, method, EnumTableAuthDataType.AUTH, fullProps);
        // 如果定制权限,指定了部分字段,以定制为主
        if (null != propsAuth) {
            props = propsAuth;
        }
        // 没有字段可以增删改查,表示没有权限
        if (props.size() == 0) {
            throw new BusinessException("无权限");
        }

        Set<String> propsUnAuth = this.getProps(entityOld, role, method, EnumTableAuthDataType.UN_AUTH, fullProps);
        if (propsUnAuth != null) {
            props.removeAll(propsUnAuth);
        }

        // 展示的属性个数,占多,还是,占少
        boolean newInstanceFlag = (fullProps.size() / props.size()) > 3;

        switch (method) {
            case QUERY:
//                if (!entityClass.isAssignableFrom(IUserRecordClass.class)) {
//                    props.add("id");
//                }
//                if (!entityClass.isAssignableFrom(IConfigClass.class)) {
//                    props.add("createTime");
//                }
                break;
            case UPDATE:
                props.add("id");
                break;
        }
        // 隐藏属性
        return this.setNull(list, props, newInstanceFlag);
    }

    /**
     * 默认权限
     */
    private Set<String> getPropsDefault(EnumSysRole role, EnumMethodType method, Set<String> fullProps) {
        Set<String> props = new HashSet<>(64);
//        Set<String> unAuthProps = new HashSet<>(64);
        switch (role) {
            case AGENT:
            case ADMIN:
                switch (method) {
                    case QUERY:
                        // 全开放
                        props.addAll(fullProps);
                        break;
                    case INSERT:
                    case UPDATE:
                    case DELETE:
                        // 记录类 不开放,  其他类 开放
                        if (!IUserRecordClass.class.isAssignableFrom(entityClass) && !IModel.class.equals(entityClass) && filterMore(role, entityClass)) {
                            props.addAll(fullProps);
                        }
                        break;
                }
                break;
            case USER:
                switch (method) {
                    case QUERY:
                        // 查询
                        if (IConfigClass.class.isAssignableFrom(entityClass)
                                || IUserDataClass.class.isAssignableFrom(entityClass)
                                || IUserOrderClass.class.isAssignableFrom(entityClass)
                                || IUserRecordClass.class.isAssignableFrom(entityClass)
                        ) {
                            // 全开放
                            props.addAll(fullProps);
                        }
                        break;
                    case INSERT:
                        if (IUserOrderClass.class.isAssignableFrom(entityClass)
                                || IUserDataClass.class.isAssignableFrom(entityClass)) {
                            props.addAll(fullProps);
                        }
                        break;
                    case UPDATE:
                    case DELETE:
                        // 增删改
                        if (IUserDataClass.class.isAssignableFrom(entityClass)) {
                            props.addAll(fullProps);
                        }
                        // 不开放
                        break;
                }
            case ANONYMOUS:
                switch (method) {
                    case QUERY:
                        if (IConfigClass.class.isAssignableFrom(entityClass)) {
                            props.addAll(fullProps);
                        }
                        break;
                }
                // 不开放
                break;
            default:
                // 不开放
                break;
        }
        return props;
    }

    private boolean filterMore(EnumSysRole role, Class<ENTITY> entityClass) {
        if (EnumSysRole.AGENT.equals(role)) {
            // 代理端不能改配置文件
            return !IConfigClass.class.equals(entityClass);
        }
        return true;
    }

    /**
     * 获取定制字段
     * 1.按默认
     * 2.按定制 [...] (可能为空,即不开放)
     * 3.按定制 全部字段
     */
    private Set<String> getProps(ENTITY entity, EnumSysRole role, EnumMethodType method, EnumTableAuthDataType auth, Set<String> fullProps) {
        List<TableAuthDataUnit<ENTITY>> auths = setMap.getOrDefault(role, Collections.emptyMap())
                .getOrDefault(method, Collections.emptyMap())
                .get(auth);
        // 按默认权限
        if (auths == null) {
            return null;
        }
        auths = auths.stream().filter(a -> null == entity || a.getCondition().apply(entity)).collect(Collectors.toList());
        if (auths.size() == 1) {
            // 全不开放
            TableAuthDataUnit<ENTITY> authOnlyOne = auths.get(0);
            if (TableAuthDataUnit.AUTH_NULL.equals(authOnlyOne.getPropName())) {
                return Collections.emptySet();
            }
            // 全字段开放
            if (TableAuthDataUnit.AUTH_ALL.equals(authOnlyOne.getPropName())) {
                return fullProps;
            }
        }
        // 指定部分字段
        return auths.stream().map(TableAuthDataUnit::getPropName).collect(Collectors.toSet());
    }

    /**
     * 过滤行
     */
    private List<ENTITY> fillList(List<ENTITY> list, EnumSysRole role, EnumMethodType method, EnumTableAuthDataType auth) {
        List<TableAuthDataUnit<ENTITY>> auths = setMap.getOrDefault(role, Collections.emptyMap())
                .getOrDefault(method, Collections.emptyMap())
                .get(auth);
        // 按默认权限
        if (auths == null) {
            return list;
        }
        for (TableAuthDataUnit<ENTITY> tableAuthDataUnit : auths) {
            switch (tableAuthDataUnit.getMethod()) {
                case QUERY:
                    // 过滤数据
                    list = list.stream().filter(e -> null != e && null == tableAuthDataUnit.getCondition() || tableAuthDataUnit.getCondition().apply(e)).collect(Collectors.toList());
                    break;
            }
        }
        return list;
    }

    // 清空特定属性
    @SneakyThrows
    private List<ENTITY> setNull(List<ENTITY> list, Set<String> props, boolean newInstanceFlag) {
        if (newInstanceFlag) {
            List<Tuple3<Field, Method, Method>> propMetas = classInfosFull.entrySet().stream().filter(p -> props.contains(p.getKey())).map(Map.Entry::getValue).collect(Collectors.toList());
            for (int i = 0; i < list.size(); i++) {
                ENTITY entity = list.get(i);
                // 新建实体
                //noinspection deprecation
                ENTITY entityNew = entityClass.newInstance();
                list.set(i, entityNew);
                // 旧对象的属性 填入 新对象
                for (Tuple3<Field, Method, Method> propMeta : propMetas) {
                    propMeta.getT2().invoke(entityNew, propMeta.getT3().invoke(entity));
                }
            }
        } else {
            List<Method> setNullMethods = classInfosFull.entrySet().stream().filter(p -> !props.contains(p.getKey())).map(e -> e.getValue().getT2()).collect(Collectors.toList());
            for (ENTITY entity : list) {
                // 清空其他属性
                for (Method setMethod : setNullMethods) {
                    setMethod.invoke(entity, (Object) null);
                }
            }
        }
        return list;
    }


    private Map<EnumSysRole, Map<EnumMethodType, Map<EnumTableAuthDataType, List<TableAuthDataUnit<ENTITY>>>>> initSetMap(List<TableAuthData<ENTITY>> datas) {
        List<TableAuthDataUnit<ENTITY>> units = new ArrayList<>();
        for (TableAuthData<ENTITY> data : datas) {
            if (data != null) {
                for (EnumMethodType method : data.getMethods()) {
                    for (EnumSysRole role : data.getRoles()) {
                        List<FunSerializable<ENTITY, Object>> props = data.getProps();
                        if (TableAuthData.AUTH_PROP_ALL == (List) props) {
                            TableAuthDataUnit<ENTITY> unit = new TableAuthDataUnit<>();
                            unit.setCondition(data.getCondition());
                            unit.setType(data.getType());
                            unit.setMethod(method);
                            unit.setRole(role);
                            unit.setPropName(TableAuthDataUnit.AUTH_ALL);
                            units.add(unit);
//                            units.add((TableAuthDataUnit<ENTITY>) TableAuthDataUnit.AUTH_ALL);
                        } else if (TableAuthData.AUTH_PROP_NULL == (List) props) {
                            TableAuthDataUnit<ENTITY> unit = new TableAuthDataUnit<>();
                            unit.setCondition(data.getCondition());
                            unit.setType(data.getType());
                            unit.setMethod(method);
                            unit.setRole(role);
                            unit.setPropName(TableAuthDataUnit.AUTH_NULL);
                            units.add(unit);
//                            units.add((TableAuthDataUnit<ENTITY>) TableAuthDataUnit.AUTH_ALL);
                        } else {
                            for (FunSerializable<ENTITY, Object> getFun : props) {
                                SerializedLambdaData serializedLambdaData = LambdaUtil.resolveCache(getFun);
                                String propName = serializedLambdaData.getPropName();
                                TableAuthDataUnit<ENTITY> unit = new TableAuthDataUnit<>();
                                unit.setCondition(data.getCondition());
                                unit.setType(data.getType());
                                unit.setMethod(method);
                                unit.setRole(role);
                                unit.setPropName(propName);
                                //                        unit.setGetFun(getFun);
                                //                        unit.setSetMethod(ClassCaches.getClassInfos(entityClass).get(propName).getT2());
                                units.add(unit);
                            }
                        }
                    }
                }
            }
        }
        Map<EnumSysRole, Map<EnumMethodType, Map<EnumTableAuthDataType, List<TableAuthDataUnit<ENTITY>>>>> setMap = new HashMap<>();
        for (TableAuthDataUnit<ENTITY> data : units) {
            EnumSysRole role = data.getRole();
            EnumMethodType method = data.getMethod();
            EnumTableAuthDataType type = data.getType();
            setMap.computeIfAbsent(role, r -> new HashMap<>())
                    .computeIfAbsent(method, m -> new HashMap<>())
                    .merge(type, new ArrayList<>(List.of(data)), (list1, list2) -> {
                        list1.addAll(list2);
                        return list1;
                    });
        }
        return setMap;
    }

}
