package com.pro.common.module.api.user.model.db;

import com.pro.common.modules.api.dependencies.model.BaseUserModel;
import com.pro.common.modules.api.dependencies.model.classes.IUserDataClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data

@EqualsAndHashCode(callSuper = true)
@ApiModel(description = "用户收货地址")
@JTDTable(entityId = 126, module = "user")
public class UserAddress extends BaseUserModel implements IUserDataClass {
    @ApiModelProperty(value = "用户ID")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null) // 设置为可空，为了创建模拟收货地址
    private Long userId;

    @ApiModelProperty(value = "联系电话")
    private String phone;
    @ApiModelProperty(value = "姓名")
    private String receiver;

    @ApiModelProperty(value = "国家")
    private String country;
    @ApiModelProperty(value = "邮政编码")
    private String postalCode;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区县")
    private String district;
    @ApiModelProperty(value = "最小区域编号")
    @JTDField(notNull = JTDConst.EnumFieldNullType.can_null)
    private Long lastSysAddressCode;
    @ApiModelProperty(value = "详细地址")
    private String addressDetail;
    @ApiModelProperty(value = "是否默认")
    private Boolean isDefault;
}
