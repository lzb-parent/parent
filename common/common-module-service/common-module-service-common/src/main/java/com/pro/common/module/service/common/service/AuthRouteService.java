package com.pro.common.module.service.common.service;

import com.pro.common.module.api.common.model.db.AuthRoute;
import com.pro.common.module.service.common.dao.AuthRouteDao;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.service.ITranslateDateService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.core.EnumConstant;
import com.pro.framework.core.EnumUtil;
import com.pro.framework.mtq.service.multiwrapper.dto.MultiPageResult;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return getAuthRoutes(paramMap,
                (paramMapNew) -> super.selectList(entityClassName, paramMapNew, timeQuery, limit, selects, selectMores,
                        selectLess, orderInfos));
    }

    private List<AuthRoute> getAuthRoutes(Map<String, Object> paramMap, Function<Map<String, Object>, List<AuthRoute>> queryListFunction) {
        paramMap = new HashMap<>(paramMap);
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
        List<AuthRoute> authRoutes = queryListFunction.apply(paramMap);
        switch (application) {
            case user:
                return authRoutes;
            case agent:
                Map<String, List<AuthRoute>> listMap = authRoutes.stream()
                        .collect(Collectors.groupingBy(AuthRoute::getCode));
                return listMap.values().stream().map(routes -> {
                    AuthRoute authRouteAgent = routes.stream()
                            .filter(r -> EnumSysRole.AGENT.equals(r.getSysRole()))
                            .findFirst()
                            .orElse(null);
                    AuthRoute authRouteAdmin = routes.stream()
                            .filter(r -> EnumSysRole.ADMIN.equals(r.getSysRole()))
                            .findFirst()
                            .orElse(null);
                    if (authRouteAgent == null) {
                        return authRouteAdmin;
                    } else if (authRouteAgent.getEnabled()) {
                        return authRouteAgent;
                    }
                    return null;
                }).filter(Objects::nonNull).collect(Collectors.toList());
            case admin:
        }
        authRoutes.forEach(authRoute -> authRoute.setName(I18nUtils.get(authRoute.getName())));
        return authRoutes;
    }

    @Override
    public IMultiPageResult<AuthRoute> selectPage(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        MultiPageResult<AuthRoute> page = new MultiPageResult<>();
        page.setRecords(getAuthRoutes(paramMap,
                (paramMapNew) -> super.selectPage(entityClassName, pageInput, paramMapNew, timeQuery).getRecords()));
        return page;
    }

    @Override
    public boolean updateById(AuthRoute entity) {
        entity.setName(null);// 翻译统一,暂时不可编辑
        return super.updateById(entity);
    }

    @Override
    public List<String> getTranslateKeys(boolean isCommon) {
        Class<IEnumToDbEnum> intf = IEnumToDbEnum.class;
        Set<Class> enumClasses = EnumConstant.simpleNameClassMapNoReplace.values()
                .stream()
                .filter(intf::isAssignableFrom)
                .filter(c -> c.getPackage().getName().startsWith("com.pro.common.module.service."))
                .collect(Collectors.toSet());
        Set<String> baseCodes = enumClasses.stream()
                .flatMap(c -> ((List<Enum>) EnumUtil.enumList(c)).stream())
                .map(Enum::name)
                .collect(Collectors.toSet());
        return this.list().stream().filter(e -> isCommon == baseCodes.contains(e.getCode()))
                .flatMap(authRoute -> Stream.of(authRoute.getName(), authRoute.getRemark())).distinct().collect(Collectors.toList());
    }
}
