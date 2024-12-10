package com.pro.common.module.api.userlevel.model.intf;

import com.pro.common.module.api.userlevel.model.db.UserLevelConfig;

import java.util.List;

public interface IUserLevelConfigService<T extends UserLevelConfig> {
    T getByIdCache(Long id);

    void reload();

    List<T> getActiveList(Boolean EnjoyTgCommission);

}
