package com.pro.common.module.api.system.service;

import com.pro.common.module.api.system.dao.AuthDictDao;
import com.pro.common.module.api.system.model.db.AuthDict;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.system.model.enums.IEnumAuthDict;
import com.pro.common.module.api.system.model.intf.IAuthDictService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.service.ITranslateDateService;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 国家 服务实现类
 */
@Service
public class AuthDictService extends BaseService<AuthDictDao, AuthDict> implements IAuthDictService, ITranslateDateService {
    private static Map<String, String> MAP;
    @Autowired
    @Lazy
    private AuthDictService THIS;

    @Override
    public <T> T getValueCacheOrDefault(IEnumAuthDict enumDict, T defaultVal) {
        assert defaultVal != null;
        String valueStr = this.getValueCache(enumDict);
        return toType(valueStr, defaultVal);
    }

    @SuppressWarnings("unchecked")
    private static <T> T toType(String valueStr, T defaultVal) {
        if (null == valueStr || valueStr.isEmpty()) {
            return defaultVal;
        }
        if (defaultVal instanceof Integer) {
            return (T) Integer.valueOf(valueStr);
        } else if (defaultVal instanceof BigDecimal) {
            return (T) new BigDecimal(valueStr);
        } else if (defaultVal instanceof Long) {
            return (T) Long.valueOf(valueStr);
        } else if (defaultVal instanceof Double) {
            return (T) Double.valueOf(valueStr);
        } else if (defaultVal instanceof String) {
            return (T) valueStr;
        } else if (defaultVal instanceof Boolean) {
            return (T) (Boolean) "1".equals(valueStr);
        }
        throw new BusinessException("unknown type " + defaultVal.getClass());
    }

    @Override
    public String getValue(IEnumAuthDict enumAuthDict) {
        return this.lambdaQuery().eq(AuthDict::getCode, enumAuthDict.name()).one().getValue();
    }

    @Override
    public String getValueCache(IEnumAuthDict enumDict) {
        return THIS.getMapCache().get(enumDict.name());
    }

    @Cacheable(CommonConst.CacheKey.AuthDict)
    public Map<String, String> getMapCache() {
        return getMap();
    }

    private Map<String, String> getMap() {
        return CollUtils.listToMap(lambdaQuery().eq(AuthDict::getEnabled, true).list(), AuthDict::getCode, AuthDict::getValue);
    }


    @Override
    @CacheEvict(value = CommonConst.CacheKey.AuthDict, allEntries = true)
    public void reload() {
    }

    @Override
    public String getValueOnStart(IEnumAuthDict enumDict) {
        if (MAP == null) {
            MAP = this.getMap();
        }
        return MAP.get(enumDict.name());
    }

    @Override
    public <T> T getValueOnStart(IEnumAuthDict enumDict, T defaultVal) {
        if (MAP == null) {
            MAP = this.getMap();
        }
        return toType(MAP.get(enumDict.name()), defaultVal);
    }

    @Override
    public boolean updateById(AuthDict entity) {
        entity.setLabel(null);// 翻译统一,暂时不可编辑
        entity.setRemark(null);// 翻译统一,暂时不可编辑
        boolean success = super.updateById(entity);
        // 刷新缓存
        THIS.reload();
        return success;
    }


    @Override
    public List<AuthDict> selectList(String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, Long limit, List<String> selects, List<String> selectMores, List<String> selectLess, List<OrderItem> orderInfos) {
        List<AuthDict> authRoutes = super.selectList(entityClassName, paramMap, timeQuery, limit, selects, selectMores, selectLess, orderInfos);
        authRoutes.forEach(authRoute -> authRoute.setLabel(I18nUtils.get(authRoute.getLabel())));
        authRoutes.forEach(authRoute -> authRoute.setRemark(I18nUtils.get(authRoute.getRemark())));
        return authRoutes;
    }

    @Override
    public IMultiPageResult<AuthDict> selectPage(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        IMultiPageResult<AuthDict> page = super.selectPage(entityClassName, pageInput, paramMap, timeQuery);
        page.getRecords().forEach(authRoute -> authRoute.setLabel(I18nUtils.get(authRoute.getLabel())));
        page.getRecords().forEach(authRoute -> authRoute.setRemark(I18nUtils.get(authRoute.getRemark())));
        return page;
    }

    @Override
    public LinkedHashMap<String, String> getKeyValueMap(boolean isCommon) {
        Set<String> baseCodes = Arrays.stream(EnumDict.values()).map(Enum::name).collect(Collectors.toSet());
        return this.list().stream().filter(e -> isCommon == baseCodes.contains(e.getCode())).collect(Collectors.toMap(AuthDict::getLabel, AuthDict::getLabel, (v1, v2) -> v1, LinkedHashMap::new));
    }
}
