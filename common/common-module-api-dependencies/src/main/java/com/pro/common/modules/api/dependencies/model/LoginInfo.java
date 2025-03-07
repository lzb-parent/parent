package com.pro.common.modules.api.dependencies.model;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.Collections;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo implements ILoginInfo, Principal {
    @Schema(hidden = true)
    private EnumSysRole sysRole;
    @Schema(hidden = true)
    private Long id;
    @Schema(hidden = true)
    private Set<String> permissionPaths = Collections.EMPTY_SET;
    @Schema(hidden = true)
    private Set<String> permissionPathsPrefix = Collections.EMPTY_SET;

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public Boolean getEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
