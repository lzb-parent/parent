package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.framework.EnumProperties;
import com.pro.framework.api.IReloadService;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.enums.EnumMethodType;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 鉴权服务工厂类
 */
@Slf4j
@Service
public class AuthServiceFactory implements IReloadService {
    // 实体 权限service
    private Map<String, AuthService<?>> authMap = new ConcurrentHashMap<>();
    // 实体 权限service
    private IEntityProperties entityProperties;

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Autowired(required = false)
    public void setAuthMap(Map<String, AuthService> authMap, IEntityProperties entityProperties) {
        this.authMap = authMap.values().stream().filter(s -> null != s.getEntityClass()).collect(Collectors.toMap(service -> service.getEntityClass().getSimpleName(), auth -> auth, (v1, v2) -> v1, ConcurrentHashMap::new));
        this.entityProperties = entityProperties;
    }

    @Override
    public void reload() {
        authMap = new ConcurrentHashMap<>();
    }

    public AuthService<?> getService(String classSimpleName) {
        return authMap.computeIfAbsent(classSimpleName, (c) -> {
            AuthService<?> service = new AuthService<>();
            service.reload(getBeanClass(classSimpleName));
            return service;
        });
    }

    public <T> Class<T> getBeanClass(String classSimpleName) {
        Class<T> tClass = MultiClassRelationFactory.INSTANCE.getEntityClass(StrUtils.firstToLowerCase(classSimpleName));
        AssertUtil.notEmpty(tClass, "entity not exist: " + classSimpleName);
        return tClass;
    }
}
