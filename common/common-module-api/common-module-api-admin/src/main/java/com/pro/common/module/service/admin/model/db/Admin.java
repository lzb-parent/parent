package com.pro.common.module.service.admin.model.db;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pro.common.module.api.common.model.db.AuthRole;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;
import com.pro.common.modules.api.dependencies.model.classes.IAdminClass;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "管理员表")
@JTDTable(entityId = 10001, sequences = {"KEY `username` (`username`)"})
public class Admin extends BaseModel implements IAdminClass, ILoginInfoPrepare, IEnumToDbDbId {

    @Schema(description = "登录名")
    private String username;
    @Schema(description = "登录密码")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JTDField(uiType = JTDConst.EnumFieldUiType.password)
    private String password;
    @Schema(description = "昵称")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String nickname;
    @Schema(description = "手机号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String phone;
    @Schema(description = "ip")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String ip;
    @Schema(description = "ip地址信息")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String ipAddress;
    @Schema(description = "登录次数")
    private Integer loginTimes;
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    @Schema(description = "最后登录时间")
    private LocalDateTime lastLoginTime;
    @Schema(description = "启用")
    private Boolean enabled;
//    @Schema(description = "谷歌验证器")
//    private Boolean googleAuthOpen;
    @Schema(description = "谷歌验证器密钥")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private String googleAuthSecret;
    @Schema(description = "绑定角色")
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
