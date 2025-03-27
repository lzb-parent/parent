package com.pro.common.module.api.user.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pro.common.module.api.agent.model.db.Agent;
import com.pro.common.module.api.common.model.db.Country;
import com.pro.common.module.api.common.model.db.Poster;
import com.pro.common.module.api.userlevel.model.db.UserLevelConfig;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.classes.IUserOrderClass;
import com.pro.common.modules.api.dependencies.user.model.IUser;
import com.pro.common.modules.api.dependencies.user.model.IUserMsg;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data

@EqualsAndHashCode(callSuper = true)
//@JTDTable(
//        sequences = {
//                "UNIQUE KEY `uk_username` (`username`)",
//                "KEY `idx_code` (`code`)",
//        }
//)
public class User extends BaseModel implements IUserOrderClass, ILoginInfo, IEnumToDbDbId, IUser, IUserMsg {
    public static final User EMPTY = new User();
    @ApiModelProperty(value = "头像")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.image)
    private String photo;
    @ApiModelProperty(value = "登录名")
    @JTDField(group = "基础信息", disabled = true)
    private String username;
    @ApiModelProperty(value = "昵称")
    @JTDField(group = "基础信息")
    private String nickName;
    @ApiModelProperty(value = "手机号")
    @JTDField(group = "基础信息")
    private String phone;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "登录密码")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.password)
    private String password;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ApiModelProperty(value = "提款密码")
    @JTDField(group = "基础信息", uiType = JTDConst.EnumFieldUiType.password)
    private String tkPassword;

    @ApiModelProperty(value = "用户等级")
    @JTDField(group = "基础信息", entityClass = UserLevelConfig.class, entityClassKey = "id", defaultValue = "1")
    private Long levelId;
    @JTDField(group = "基础信息", notNull = JTDConst.EnumFieldNullType.can_null)
    @ApiModelProperty(value = "等级过期时间")
    private LocalDateTime levelDueTime;

    @ApiModelProperty(value = "启用")
    @JTDField(group = "基础信息")
    private Boolean enabled;
    @ApiModelProperty(value = "内部")
    @JTDField(group = "基础信息", disabled = true)
    private Boolean isDemo;
    @ApiModelProperty(value = "是否游客")
    @JTDField(group = "基础信息", disabled = true)
    private Boolean isGuest;
    @ApiModelProperty(value = "提款开关")
    @JTDField(group = "基础信息", defaultValue = "1")
    private Boolean withdrawOpen;
    @ApiModelProperty(value = "提款禁止文章")
    @JTDField(group = "基础信息", defaultValue = "withdraw_user_close")
    private String withdrawPosterCode;
    @ApiModelProperty(value = "加粉开关")
    @JTDField(group = "基础信息", defaultValue = "1")
    private Boolean fanOpen;
    @ApiModelProperty(value = "内部备注")
    @JTDField(group = "基础信息", mainLength = 2000, uiType = JTDConst.EnumFieldUiType.textarea)
    private String remark;


    @ApiModelProperty(value = "telegram")
    @JTDField(group = "其他信息")
    private String telegram;
    @ApiModelProperty(value = "whatsapp")
    @JTDField(group = "其他信息")
    private String whatsapp;
    @ApiModelProperty(value = "邮箱")
    @JTDField(group = "其他信息")
    private String email;
    @ApiModelProperty(value = "真实姓名")
    @JTDField(group = "其他信息")
    private String realName;
    @ApiModelProperty(value = "身份证号")
    @JTDField(group = "其他信息")
    private String idCard;
    @ApiModelProperty(value = "手机号区号")
    @JTDField(group = "其他信息")
    private String phonePrefix;
    @ApiModelProperty(value = "默认语言")
    @JTDField(defaultValue = CommonConst.Str.DEFAULT_LANG_EN, entityClass = Country.class, entityClassKey = "langCode", group = "其他信息")
    private String lang;

    @ApiModelProperty(value = "注册时间")
    @JTDField(group = "注册登录", disabled = true)
    protected LocalDateTime createTime;
    @ApiModelProperty(value = "注册ip")
    @JTDField(group = "注册登录", disabled = true)
    private String registerIp;
    @ApiModelProperty(value = "注册ip地址")
    @JTDField(group = "注册登录", disabled = true)
    private String registerIpAddress;
    @ApiModelProperty(value = "注册域名")
    @JTDField(group = "注册登录", disabled = true)
    private String registerHost;
    @ApiModelProperty(value = "登录次数")
    @JTDField(group = "注册登录", disabled = true)
    private Integer loginTimes;
    @ApiModelProperty(value = "最后登录时间")
    @JTDField(group = "注册登录", notNull = JTDConst.EnumFieldNullType.can_null, disabled = true)
    private LocalDateTime lastLoginTime;
    @ApiModelProperty(value = "最后登录IP")
    @JTDField(group = "注册登录", disabled = true)
    private String lastLoginIp;
    @ApiModelProperty(value = "最后登录IP地址")
    @JTDField(group = "注册登录", disabled = true)
    private String lastLoginIpAddress;

    @ApiModelProperty(value = "推广代理")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null, entityClass = Agent.class, entityClassKey = "id", entityClassLabel = "username")
    private Long agentId;
    @ApiModelProperty(value = "上级用户")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null)
    private String pusername;
    @ApiModelProperty(value = "上级邀请码")
    @JTDField(group = "邀请", disabled = true)
    private String inviteCode;

    @ApiModelProperty(value = "1级推广人")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long pid;
    @ApiModelProperty(value = "2级推广人")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long pid2;
    @ApiModelProperty(value = "3级推广人")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long pid3;
    @ApiModelProperty(value = "4级推广人")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long pid4;
    @ApiModelProperty(value = "5级推广人")
    @JTDField(group = "邀请", notNull = JTDConst.EnumFieldNullType.can_null)
    private Long pid5;
    @ApiModelProperty(value = "父级id集合", notes = "越直接的在越后面,不包含自己,以逗号开头,以逗号结尾. 例如 ,1,100, ")
    @JTDField(group = "邀请", uiType = JTDConst.EnumFieldUiType.hide)
    private String pids;
    @ApiModelProperty(value = "个人推广码")
    @JTDField(group = "邀请", defaultValue = "NULL", notNull = JTDConst.EnumFieldNullType.can_null, disabled = true)
    private String code;
//    @ApiModelProperty(value = "邀请码开关")
//    @JTDField(group = "邀请")
//    private Boolean isAgency;
//    @ApiModelProperty(value = "邀请码关闭文章")
//    @JTDField(group = "邀请", defaultValue = "inviteCloseTip", entityClass = Poster.class)
//    private String agencyClosePosterCode;


    // 旧密码
    transient private String passwordOld;


    @Override
    public EnumSysRole getSysRole() {
        return EnumSysRole.USER;
    }

    @Override
    public Long getUserId() {
        return id;
    }

    @Override
    public void setUserId(Long userId) {
        this.id = userId;
    }
}
