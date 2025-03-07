package com.pro.common.modules.api.dependencies.model;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;
import java.util.Set;

public interface ILoginInfo extends Serializable {
    //系统角色
    EnumSysRole getSysRole();

    Long getId();

    String getPassword();

    Boolean getEnabled();

    @Schema(hidden = true)
    default Boolean getGoogleAuthOpen() {
        return false;
    }

    Set<String> getPermissionPaths();

    Set<String> getPermissionPathsPrefix();

}
