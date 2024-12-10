package com.pro.common.module.api.usermoney.model.db;

import com.pro.common.modules.api.dependencies.model.classes.IUserRecordClass;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityWithType;
import com.pro.framework.api.enums.IEnumToDbDbId;

/**
 * 用户余额 基础类
 * 配合 充值提现 使用
 */

public class UserMoney extends AmountEntityWithType implements IUserRecordClass , IEnumToDbDbId {
}
