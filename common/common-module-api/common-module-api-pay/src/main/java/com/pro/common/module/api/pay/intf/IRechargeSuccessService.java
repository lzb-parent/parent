package com.pro.common.module.api.pay.intf;

import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.modules.api.dependencies.user.model.IUser;

public interface IRechargeSuccessService{
    void afterRechargeSuccess(IUser user,UserRecharge userRecharge);
}
