package com.pro.common.module.service.admin.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pro.common.module.api.common.model.db.AuthRole;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 管理员表
 *
 * @author admin
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(description = "管理员表")
@JTDTable(entityId = 1001, sequences = {"KEY `username` (`username`)"})
public class Admin extends BaseModel implements IAdminClass, ILoginInfo, IEnumToDbDbId {

    @ApiModelProperty(value = "登录名")
    private String username;
    @ApiModelProperty(value = "登录密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JTDField(uiType = JTDConst.EnumFieldUiType.password)
    private String password;
    @ApiModelProperty(value = "昵称")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String nickName;
    @ApiModelProperty(value = "手机号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String phone;
    @ApiModelProperty(value = "ip")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String ip;
    @ApiModelProperty(value = "ip地址信息")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String ipAddress;
    @ApiModelProperty(value = "登录次数")
    private Integer loginTimes;
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    @ApiModelProperty(value = "最后登录时间")
    private LocalDateTime lastLoginTime;
    @ApiModelProperty(value = "启用")
    private Boolean enabled;
//    @ApiModelProperty(value = "谷歌验证器")
//    private Boolean googleAuthOpen;
    @ApiModelProperty(value = "谷歌验证器密钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String googleAuthSecret;
    @ApiModelProperty(value = "绑定角色")
    @JTDField(entityClass = AuthRole.class, entityClassKey = "id", javaTypeEnumClassMultiple = true)
    private String roleIds;

    @Override
    @JsonIgnore
    public EnumSysRole getSysRole() {
        return EnumSysRole.ADMIN;
    }

    // 旧密码
    transient private String passwordOld;

}
