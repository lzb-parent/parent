package com.pro.common.module.api.system.model.intf;

import com.pro.common.module.api.system.model.enums.IEnumAuthDict;
import com.pro.framework.api.IReloadService;

public interface IAuthDictService extends IReloadService {
//    AuthDict getCache(String code);

    /**
     * 实时查询
     */
    String getValue(IEnumAuthDict code);

    /**
     * 查缓存
     */
    String getValueCache(IEnumAuthDict enumDict);

    /**
     * 查缓存
     */
    <T> T getValueCacheOrDefault(IEnumAuthDict enumDict, T t);

    /**
     * 刷新缓存
     */

//    void reload();

    /**
     * 只在启动批量查询一次
     */
    String getValueOnStart(IEnumAuthDict enumDict);

    <T> T getValueOnStart(IEnumAuthDict enumDict, T defaultVal);
}
