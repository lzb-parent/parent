package com.pro.common.module.api.common.service;

import com.pro.common.module.api.common.dao.AuthRouteDao;
import com.pro.common.module.api.common.model.db.AuthRoute;
import com.pro.common.module.api.common.model.enums.EnumAuthRouteAdmin;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.service.ITranslateDateService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 国家 服务实现类
 */
@Service
public class AuthRouteService extends BaseService<AuthRouteDao, AuthRoute> implements ITranslateDateService {
    private final CommonProperties commonProperties;

    public AuthRouteService(CommonProperties commonProperties) {
        super();
        this.commonProperties = commonProperties;
    }

    public List<AuthRoute> getList(EnumSysRole role, Set<String> codes) {
        return this.lambdaQuery()
                .eq(AuthRoute::getSysRole, role)
                .eq(AuthRoute::getEnabled, true)
                .in(null != codes, AuthRoute::getCode, codes)
                .list();
    }

    @Override
    public List<AuthRoute> selectList(String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, Long limit, List<String> selects, List<String> selectMores, List<String> selectLess, List<OrderItem> orderInfos) {
        EnumApplication application = commonProperties.getApplication();
        switch (application) {
            case agent:
//                paramMap.put("enabled", null);
                paramMap.remove("enabled");
                break;
            default:
                paramMap.put("sysRole", application.getRole());
                break;
        }
        List<AuthRoute> authRoutes = super.selectList(entityClassName, paramMap, timeQuery, limit, selects, selectMores, selectLess, orderInfos);
        authRoutes.forEach(authRoute -> authRoute.setName(I18nUtils.get(authRoute.getName())));
        switch (application) {
            case user:
                return Collections.emptyList();
            case agent:
                Map<String, List<AuthRoute>> listMap = authRoutes.stream().collect(Collectors.groupingBy(AuthRoute::getCode));
                return listMap.values().stream().map(routes -> {
                    AuthRoute authRouteAgent = routes.stream().filter(r -> EnumSysRole.AGENT.equals(r.getSysRole())).findFirst().orElse(null);
                    AuthRoute authRouteAdmin = routes.stream().filter(r -> EnumSysRole.ADMIN.equals(r.getSysRole())).findFirst().orElse(null);
                    if (authRouteAgent == null) {
                        return authRouteAdmin;
                    } else if (authRouteAgent.getEnabled()) {
                        return authRouteAgent;
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            case admin:
        }
        return authRoutes;
    }

    @Override
    public IMultiPageResult<AuthRoute> selectPage(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        IMultiPageResult<AuthRoute> page = super.selectPage(entityClassName, pageInput, paramMap, timeQuery);
        page.getRecords().forEach(authRoute -> authRoute.setName(I18nUtils.get(authRoute.getName())));
        return page;
    }

    @Override
    public boolean updateById(AuthRoute entity) {
        entity.setName(null);// 翻译统一,暂时不可编辑
        return super.updateById(entity);
    }

    @Override
    public LinkedHashMap<String, String> getKeyValueMap(boolean isCommon) {
        Set<String> baseCodes = Arrays.stream(EnumAuthRouteAdmin.values()).map(Enum::name).collect(Collectors.toSet());
        return this.list().stream().filter(e -> isCommon == baseCodes.contains(e.getCode())).collect(Collectors.toMap(AuthRoute::getName, AuthRoute::getName, (v1, v2) -> v1, LinkedHashMap::new));
    }
}
