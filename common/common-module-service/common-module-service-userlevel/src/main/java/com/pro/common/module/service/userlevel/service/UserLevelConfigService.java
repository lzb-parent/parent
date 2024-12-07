package com.pro.common.module.service.userlevel.service;

import com.pro.common.module.api.userlevel.model.db.UserLevelConfig;
import com.pro.common.module.api.userlevel.model.intf.IUserLevelConfigService;
import com.pro.common.module.service.userlevel.dao.UserLevelConfigDao;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.framework.api.IReloadService;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Map;
import java.util.Objects;

//@Service
public class UserLevelConfigService<DAO extends UserLevelConfigDao<T>, T extends UserLevelConfig> extends BaseService<DAO, T> implements IUserLevelConfigService<T> , IReloadService {
    @Autowired
    @Lazy
    private UserLevelConfigService THIS;

    @Override
    public T getByIdCache(Long id) {
        return (T) THIS.getMapCache().get(id);
    }

    @Cacheable(CommonConst.CacheKey.UserLevelConfig)
    public Map<Long, T> getMapCache() {
        return getMap();
    }

    private Map<Long, T> getMap() {
        return CollUtils.listToMap(this.lambdaQuery().eq(T::getEnabled, true).list(), T::getId, o -> o);
    }

    @Override
    @CacheEvict(value = CommonConst.CacheKey.UserLevelConfig, allEntries = true)
    public void reload() {
    }

    @Override
    public List<T> getActiveList(Boolean EnjoyTgCommission) {
        return list(qw().lambda().eq(Objects.nonNull(EnjoyTgCommission),T::getIsEnjoyTgCommission,EnjoyTgCommission).eq(T::getEnabled, true).orderByAsc(T::getLevel));
    }

    @Override
    public boolean save(T entity) {
        boolean success = super.save(entity);
        this.reload();
        return success;
    }

    @Override
    public boolean updateById(T entity) {
        boolean success = super.updateById(entity);
        this.reload();
        return success;
    }
}
