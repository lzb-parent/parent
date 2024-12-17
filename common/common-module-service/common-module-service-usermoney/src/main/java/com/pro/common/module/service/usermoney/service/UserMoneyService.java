package com.pro.common.module.service.usermoney.service;

import com.pro.common.module.service.usermoney.dao.UserMoneyDao;
import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.common.modules.api.dependencies.user.intf.IUserRegisterInitService;
import com.pro.common.modules.api.dependencies.user.model.IUser;
import com.pro.framework.mybatisplus.BaseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * 资金待结算总计服务
 */
@Slf4j
//@Service
//        (CommonConst.Bean.userMoneyService)
public class UserMoneyService<M extends UserMoneyDao<T>, T extends UserMoney> extends BaseService<M, T> implements IAmountEntityService<T>, IUserRegisterInitService {
    @Override
    @SneakyThrows
    public void init(IUser user) {
        T entity = this.getEntityClass().newInstance();
        entity.setUserId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setIsDemo(user.getIsDemo());
        this.save(entity);
    }
}
