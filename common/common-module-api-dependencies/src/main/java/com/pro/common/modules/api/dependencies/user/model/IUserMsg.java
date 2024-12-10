package com.pro.common.modules.api.dependencies.user.model;

public interface IUserMsg {
    Long getId();
    String getUsername() ;

    Long getAgentId();

    String getLang() ;

    String getPhone() ;
    String getEmail() ;
}
