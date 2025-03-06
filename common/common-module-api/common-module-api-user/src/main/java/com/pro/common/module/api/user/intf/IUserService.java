package com.pro.common.module.api.user.intf;

import com.pro.common.module.api.user.model.db.User;
import com.pro.common.modules.api.dependencies.model.LoginRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface IUserService<T extends User> {
    Map<Long, T> idMap(Collection<? extends Serializable> userIds);

    List<Long> getIdsByAgentIds(Collection<Long> agentIds);

    T getById(Serializable userId);

    T newInstant();

    boolean updateById(T entity);

    List<T> listByIds(Collection<? extends Serializable> userIds);

//    List<T> listByIdsAndLevelIds(Collection<? extends Serializable> userIds,Collection<? extends Serializable> userLevelIds);

    Map<Long, T> getMap(String queryProp, Collection<?> queryPropValues);

    List<Long> listIdByPids(List<Long> userIds);

    String buildCode(String valueCache);

    T query(LoginRequest loginRequest);
}
