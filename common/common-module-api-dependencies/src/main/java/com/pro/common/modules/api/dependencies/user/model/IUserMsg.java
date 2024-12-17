package com.pro.common.modules.api.dependencies.user.model;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;

public interface IUserMsg {
    //系统角色
    EnumSysRole getSysRole();
    Long getId();
    String getUsername() ;

    Long getAgentId();

    String getLang() ;

    String getPhone() ;
    String getEmail() ;
}
