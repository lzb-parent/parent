package com.pro.common.module.api.user.intf;

import com.pro.common.module.api.user.model.db.UserBankCard;
import com.pro.framework.api.util.inner.FunSerializable;

public interface IUserBankCardService {
    boolean updates(UserBankCard param, UserBankCard setData, FunSerializable<UserBankCard,?>... functions);

    UserBankCard getById(Long id);
}
