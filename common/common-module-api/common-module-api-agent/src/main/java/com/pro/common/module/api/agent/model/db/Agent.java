package com.pro.common.module.api.agent.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.common.modules.api.dependencies.model.classes.ISimpleInfo;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data

@EqualsAndHashCode(callSuper = true)
@Schema(description = "代理")
@NoArgsConstructor
@JTDTable(entityId = 302)
public class Agent extends BaseModel implements IAdminClass, IEnumToDbDbId, ILoginInfoPrepare, ISimpleInfo {
    public static final Agent EMPTY = new Agent();


    @Schema(description = "登录名")
    @JTDField(group = "基础信息")
    private String username;
    @Schema(description = "登录密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.password)
    private String password;
    @Schema(description = "邀请码")
    @JTDField(group = "基础信息")
    private String codeNumber;
    @Schema(description = "启用")
    @JTDField(group = "基础信息")
    private Boolean enabled;
    @Schema(description = "内部")
    @JTDField(group = "基础信息")
    private Boolean isDemo;

    @Schema(description = "上级")
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null, entityClass = Agent.class, entityClassKey = "id", entityClassLabel = "username")
    private Long pid;
    @Schema(description = "层级")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.hide, defaultValue = "1")
    private Integer level;
    @Schema(description = "父级id集合")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.hide)
    private String pids;

    @Schema(description = "推广域名")
    @JTDField(group = "配置")
    private String domain;
    @Schema(description = "代理比例")
    @JTDField(group = "配置")
    private BigDecimal rate;
    @Schema(description = "余额")
    @JTDField(group = "配置")
    private BigDecimal balance;


    @Schema(description = "昵称")
    @JTDField(group = "通讯信息")
    private String nickname;
    @Schema(description = "手机号")
    @JTDField(group = "通讯信息")
    private String phone;
    @Schema(description = "telegram")
    @JTDField(group = "通讯信息")
    private String telegram;
    @Schema(description = "whatsapp")
    @JTDField(group = "通讯信息")
    private String whatsapp;
    @Schema(description = "邮箱")
    @JTDField(group = "通讯信息")
    private String email;


    @Schema(description = "ip")
    @JTDField(group = "登录注册")
    private String ip;
    @Schema(description = "ip地址信息")
    @JTDField(group = "登录注册")
    private String ipAddress;
    @Schema(description = "登录次数")
    @JTDField(group = "登录注册")
    private Integer loginTimes;
    @Schema(description = "最后登录时间")
    @JTDField(group = "登录注册")
    private LocalDateTime lastLoginTime;

    @Schema(description = "绑定角色")
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
