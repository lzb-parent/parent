package com.pro.common.modules.api.dependencies.model;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;

import java.io.Serializable;
import java.util.Set;

public interface ILoginInfo extends Serializable {
    //系统角色
    EnumSysRole getSysRole();

    Long getId();

    String getPassword();

    Boolean getEnabled();

    default Boolean getGoogleAuthOpen() {
        return false;
    }

    Set<String> getPermissionPaths();

    Set<String> getPermissionPathsPrefix();

}
