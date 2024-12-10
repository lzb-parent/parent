package com.pro.common.module.api.usermoney.model.enums;

import com.pro.framework.api.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账变后金额低于0时的,处理办法
 */
@AllArgsConstructor
@Getter
public enum EnumAmountNegativeDeal implements IEnum {
    throwException("抛KnowException异常"),//请考虑是否是 正常流程[即欠费就抛异常取消整笔操作]
    toZero("用户余额设置为0_整笔业务正常进行"),//这将会导致该扣的钱没扣到(平台损失)
    toNegative("用户余额设置为对应负值_整笔业务正常进行")
    ;
    String label;
}
