package com.pro.common.module.service.common.service;

import com.pro.common.module.api.common.model.db.AuthRole;
import com.pro.common.module.api.common.model.db.AuthRoute;
import com.pro.common.module.service.common.dao.AuthRoleDao;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.service.IAuthRoleService;
import com.pro.common.modules.api.dependencies.service.ILoginInfoService;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 国家 服务实现类
 */
@Service
public class AuthRoleService extends BaseService<AuthRoleDao, AuthRole> implements IAuthRoleService {
    public static final String ALL = "#ALL#";
    @Autowired
    private AuthRouteService authRouteService;
    @Autowired
    private ApplicationContext applicationContext;


    private ILoginInfoService<?> getLoginInfoService(EnumSysRole role) {
        return applicationContext.getBean(role.getServiceBean(), ILoginInfoService.class);
    }

    @Override
    public Set<String> getPath(EnumSysRole role, Long loginId) {
        Set<String> routeCodes = this.getRouteCodes(role, loginId);
        if (null == routeCodes || routeCodes.isEmpty()) {
            return Collections.emptySet();
        }
        List<AuthRoute> routes = authRouteService.lambdaQuery()
                .eq(AuthRoute::getSysRole, role)
                .eq(AuthRoute::getEnabled, true)
                .in(AuthRoute::getCode, routeCodes).list();
        // 查询出路由对应的所有权限
        return routes.stream().flatMap(AuthRoleService::initPermissionPath).collect(Collectors.toSet());
    }

    public static final String pathSelectPage = "/commonData/selectPage/";
    public static final String pathSelectCountSum = "/commonData/selectCountSum/";
    public static final String pathSelectList = "/commonData/selectList/";
    public static final String pathSelectLists = "/commonData/selectLists/";
    public static final String pathSelectOne = "/commonData/selectOne/";
    public static final String pathSelectById = "/commonData/selectById/";
    public static final String pathInsertOrUpdate = "/commonData/insertOrUpdate/";
    public static final String pathInsert = "/commonData/insert/";
    public static final String pathUpdate = "/commonData/update/";
    public static final String pathDelete = "/commonData/delete/";
    public static final String pathExport = "/commonData/export/";
    public static final List<String> pathsQuery = Arrays.asList(
            pathSelectPage
            , pathSelectCountSum
            , pathSelectList
            , pathSelectLists
            , pathSelectOne
            , pathSelectById);
    public static final List<String> pathsAll = Arrays.asList(
            pathSelectPage
            , pathSelectCountSum
            , pathSelectList
            , pathSelectLists
            , pathSelectOne
            , pathSelectById
            , pathInsertOrUpdate
            , pathInsert
            , pathUpdate
            , pathDelete
            , pathExport
    );

    private static Stream<String> initPermissionPath(AuthRoute route) {
        return Arrays.stream(route.getPermissionPaths().split(",")).filter(StrUtils::isNotBlank).flatMap(permissionPath -> {
            // 全部权限
            if (permissionPath.startsWith(ALL)) {
                String entityName = permissionPath.substring(ALL.length());
                return pathsAll.stream().map(prefix -> prefix + entityName);
            }
            // 单接口权限
            else if (permissionPath.startsWith("/")) {
                return Stream.of(permissionPath);
            }
            // 查询权限
            else {
                return pathsQuery.stream().map(prefix -> prefix + permissionPath);
            }
        });
    }

    @Override
    public Set<String> getRouteCodes(EnumSysRole role, Long loginId) {
        //noinspection SwitchStatementWithTooFewBranches
        switch (role) {
            case USER:
                // 暂时不需要鉴权
                return authRouteService.selectList("authRoute", new HashMap<>(), new TimeQuery(), null,  null, null, null, null).stream().map(AuthRoute::getCode).collect(Collectors.toSet());
        }
        Set<Long> roleIds = this.getLoginInfoService(role).getRoleIds(loginId);
        if (roleIds.isEmpty()) {
            return Collections.emptySet();
        }
        Set<String> routeCodes;
        // 超级管理员 全部权限
        boolean isSuperAdmin = this.listByIds(roleIds).stream().anyMatch(AuthRole::getSuperFlag);
        if (isSuperAdmin) {
            routeCodes = authRouteService.selectList("authRoute", new HashMap<>(), new TimeQuery(), null,  null, null, null, null).stream().map(AuthRoute::getCode).collect(Collectors.toSet());
        } else {
            // 普通角色权限
            routeCodes = this.lambdaQuery()
                    .select(AuthRole::getRouteCodes)
                    .eq(AuthRole::getEnabled, true)
                    .eq(AuthRole::getSysRole, role)
                    .in(AuthRole::getId, roleIds)
                    .list().stream().flatMap(routeRole -> Arrays.stream(routeRole.getRouteCodes().split(","))).collect(Collectors.toSet());
        }
        return routeCodes;
    }


}
