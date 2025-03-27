package com.pro.common.module.api.agent.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.common.modules.api.dependencies.model.classes.ISimpleInfo;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "代理")
@NoArgsConstructor
@JTDTable(entityId = 302)
public class Agent extends BaseModel implements IAdminClass, IEnumToDbDbId, ILoginInfo, ISimpleInfo {
    public static final Agent EMPTY = new Agent();


    @ApiModelProperty(value = "登录名")
    @JTDField(group = "基础信息")
    private String username;
    @ApiModelProperty(value = "登录密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.password)
    private String password;
    @ApiModelProperty(value = "邀请码")
    @JTDField(group = "基础信息")
    private String codeNumber;
    @ApiModelProperty(value = "启用")
    @JTDField(group = "基础信息")
    private Boolean enabled;
    @ApiModelProperty(value = "内部")
    @JTDField(group = "基础信息")
    private Boolean isDemo;

    @ApiModelProperty(value = "上级")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null, entityClass = Agent.class, entityClassKey = "id", entityClassLabel = "username")
    private Long pid;
    @ApiModelProperty(value = "层级")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.hide, defaultValue = "1")
    private Integer level;
    @ApiModelProperty(value = "父级id集合")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.hide)
    private String pids;

    @ApiModelProperty(value = "推广域名")
    @JTDField(group = "配置")
    private String domain;
    @ApiModelProperty(value = "代理比例")
    @JTDField(group = "配置")
    private BigDecimal rate;
    @ApiModelProperty(value = "余额")
    @JTDField(group = "配置")
    private BigDecimal balance;


    @ApiModelProperty(value = "昵称")
    @JTDField(group = "通讯信息")
    private String nickName;
    @ApiModelProperty(value = "手机号")
    @JTDField(group = "通讯信息")
    private String phone;
    @ApiModelProperty(value = "telegram")
    @JTDField(group = "通讯信息")
    private String telegram;
    @ApiModelProperty(value = "whatsapp")
    @JTDField(group = "通讯信息")
    private String whatsapp;
    @ApiModelProperty(value = "邮箱")
    @JTDField(group = "通讯信息")
    private String email;


    @ApiModelProperty(value = "ip")
    @JTDField(group = "登录注册")
    private String ip;
    @ApiModelProperty(value = "ip地址信息")
    @JTDField(group = "登录注册")
    private String ipAddress;
    @ApiModelProperty(value = "登录次数")
    @JTDField(group = "登录注册")
    private Integer loginTimes;
    @ApiModelProperty(value = "最后登录时间")
    @JTDField(group = "登录注册")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty(value = "绑定角色")
    @JTDField(entityName = "authRole", entityClassKey = "id", javaTypeEnumClassMultiple = true)
    private String roleIds;
//    private transient String parentName;
//    private transient String parentCode;

    @Override
    @JsonIgnore
    public EnumSysRole getSysRole() {
        return EnumSysRole.AGENT;
    }
}
