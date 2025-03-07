package com.pro.common.module.api.user.model.db;

import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserDataClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户收货地址")
@JTDTable(entityId = 126, module = "user")
public class UserAddress extends BaseUserModel implements IUserDataClass {
    @Schema(description = "用户ID")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null) // 设置为可空，为了创建模拟收货地址
    private Long userId;

    @Schema(description = "联系电话")
    private String phone;
    @Schema(description = "姓名")
    private String receiver;

    @Schema(description = "国家")
    private String country;
    @Schema(description = "邮政编码")
    private String postalCode;
    @Schema(description = "省")
    private String province;
    @Schema(description = "市")
    private String city;
    @Schema(description = "区县")
    private String district;
    @Schema(description = "最小区域编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long lastSysAddressCode;
    @Schema(description = "详细地址")
    private String addressDetail;
    @Schema(description = "是否默认")
    private Boolean isDefault;
}
