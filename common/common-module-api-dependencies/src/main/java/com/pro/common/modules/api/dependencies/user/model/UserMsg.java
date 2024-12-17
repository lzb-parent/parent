package com.pro.common.modules.api.dependencies.user.model;

import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import lombok.Data;

@Data
public class UserMsg implements IUserMsg {
    private EnumSysRole sysRole;          // 用户ID
    private Long id;          // 用户ID
    private String username;  // 用户名
    private Long agentId;     // 代理商ID
    private String lang;      // 语言
    private String phone;     // 电话号码
    private String email;     // 电子邮件
}
