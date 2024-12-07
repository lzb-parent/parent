package com.pro.common.web.security.model;

import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo implements ILoginInfo, Principal {
    private EnumSysRole sysRole;
    private Long id;
    private Set<String> paths;

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
