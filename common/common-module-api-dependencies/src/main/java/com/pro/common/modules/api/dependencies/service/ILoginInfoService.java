package com.pro.common.modules.api.dependencies.service;

import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.api.dependencies.model.LoginRequest;

import java.io.Serializable;
import java.util.Set;

public interface ILoginInfoService<T extends ILoginInfoPrepare> {
    T doLogin(LoginRequest loginRequest);

    default T register(String request, String ip, String lang){
        throw new BusinessException("can not register now");
    }

    T getById(Serializable id);

    Set<Long> getRoleIds(Long loginId);
}
