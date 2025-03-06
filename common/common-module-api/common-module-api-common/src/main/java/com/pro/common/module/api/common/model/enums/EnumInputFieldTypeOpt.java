package com.pro.common.module.api.common.model.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 卡号类型,相关操作
 */
@AllArgsConstructor
@Getter
public enum EnumInputFieldTypeOpt implements IEnum {

    userAddress("用户地址管理", "userAddress"),
    userRecharge("用户充值记录提交", "userRecharge"),
    userBankCard("用户银行卡管理", "userBankCard"),
    ;

    private final String label;
    private final String submitFormEntityName;

}
