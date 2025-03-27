package com.pro.common.module.api.common.model.db;

import com.pro.common.module.api.common.model.enums.EnumInputFieldTypeOpt;
import com.pro.common.modules.api.dependencies.model.classes.BaseConfigModel;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.framework.api.enums.IEnumToDbDb;
import com.pro.framework.api.enums.IEnumToDbDbId;
import com.pro.framework.javatodb.annotation.JTDField;
import com.pro.framework.javatodb.annotation.JTDTable;
import com.pro.framework.javatodb.constant.JTDConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 支付渠道
 *
 * @author admin
 */
@Data
@ApiModel(description = "定制属性")
@JTDTable(entityId = 112, module = "pay", sequences = {
        "UNIQUE KEY `uk_opt_prop_groupCode` (`opt`,`prop`,`group_code`)"
})
public class InputField extends BaseConfigModel implements IConfigClass, IEnumToDbDbId {
    @ApiModelProperty("业务操作")
    private EnumInputFieldTypeOpt opt;
    @ApiModelProperty("业务操作关联类")
    @JTDField(uiType = JTDConst.EnumFieldUiType.hide)
    private String submitFormEntityName;
    @ApiModelProperty("属性值")
    private String prop;
    @ApiModelProperty("属性名")
    @JTDField(disabled = true)
    private String label;
    @ApiModelProperty("必填")
    private Boolean required;
    @ApiModelProperty("是否只读")
    private Boolean readonly;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("隐藏")
    private Boolean hide;
    @ApiModelProperty("输入框内提示")
    private String placeholder;
    @ApiModelProperty(value = "校验规则", notes = "例如 ^01\\\\d{9}$ 表示01开头以及后面9位数字")
    private String ruleReg;
    @ApiModelProperty("校验规则提示")
    private String ruleErrorTip;
    @ApiModelProperty("输入类型")
    @JTDField(defaultValue = "text")
    private JTDConst.EnumFieldUiType type;
    @ApiModelProperty("分组编号")
//    @JTDField(entityClass = PayCardType.class, notNull = JTDConst.EnumFieldNullType.can_null, description = "充值提现才有用")
    private String groupCode;
}
