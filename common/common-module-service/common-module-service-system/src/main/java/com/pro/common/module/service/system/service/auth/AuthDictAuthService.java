package com.pro.common.module.service.system.service.auth;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.module.api.system.model.db.AuthDict;
import com.pro.common.modules.service.dependencies.modelauth.base.AuthService;
import com.pro.common.modules.service.dependencies.modelauth.base.TableAuthData;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AuthDictAuthService extends AuthService<AuthDict> {
    @Override
    public List<TableAuthData<AuthDict>> getConfigs() {
        return List.of(
                TableAuthData.query(AuthDict::getShowUser, List.of(EnumSysRole.USER, EnumSysRole.AGENT), Arrays.asList(AuthDict::getCode, AuthDict::getValue)),
                TableAuthData.query(AuthDict::getShowAdmin, List.of(EnumSysRole.ADMIN), null)
        );
    }
}
