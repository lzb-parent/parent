package com.pro.common.module.api.usermoney.model.db;

import com.pro.common.module.api.usermoney.model.enums.EnumTradeType;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 用户余额变动 基础类
 */
@ApiModel(description = "用户余额变动记录")
@SuperBuilder
@NoArgsConstructor
//@AllArgsConstructor
public class UserMoneyRecord extends AmountEntityRecord implements IUserRecordClass {
    @ApiModelProperty("变动类型")
    @JTDField(javaTypeEnumClass = EnumTradeType.class, uiType = JTDConst.EnumFieldUiType.select)
    private String recordType;
}
