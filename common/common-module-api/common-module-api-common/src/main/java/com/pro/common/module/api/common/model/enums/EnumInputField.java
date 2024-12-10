package com.pro.common.module.api.common.model.enums;

import com.pro.common.module.api.common.model.db.InputField;
import com.pro.framework.api.enums.IEnumToDbEnum;
import com.pro.framework.javatodb.constant.JTDConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.pro.common.module.api.common.model.enums.EnumInputFieldTypeOpt.userBankCard;
import static com.pro.framework.javatodb.constant.JTDConst.EnumFieldUiType.password;

/**
 * 字典配置 定制
 */
@Getter
@AllArgsConstructor
public enum EnumInputField implements IEnumToDbEnum<InputField> {
    _1(1L, userBankCard, "bankName", "银行名称", 1, true, null, null, null, null, null, null, null, null, null),
    _2(2L, userBankCard, "bankAccount", "银行账号", 2, true, null, null, null, null, null, null, null, null, null),
    _3(3L, userBankCard, "tkPassword", "提款密码", 3, true, null, null, null, null, null, null, password, null, null),
    ;
    private Long id;
    private EnumInputFieldTypeOpt opt;
    private String prop;
    private String label;
    private Integer sort;
    private Boolean required;
    private Boolean readonly;
    private String defaultValue;
    private Boolean hide;
    private String placeholder;
    private String ruleReg;
    private String ruleErrorTip;
    private JTDConst.EnumFieldUiType type;
    //    @JTDField(entityClass = PayCardType.class, notNull = JTDConst.EnumFieldNullType.can_null, description = "充值提现才有用")
    private String groupCode;

    /**
     * 统一重置修改掉,这个配置时间以前的,旧的配置
     */
    private final String forceChangeTime;
}
