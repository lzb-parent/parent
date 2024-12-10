package com.pro.common.module.api.usermoney.service;

import com.pro.common.module.api.usermoney.model.db.UserMoneyWait;
import com.pro.common.module.api.usermoney.dao.UserMoneyWaitDao;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 资金待结算总计服务
 */
@Slf4j
@Service
public class UserMoneyWaitService extends BaseService<UserMoneyWaitDao, UserMoneyWait> implements IAmountEntityService<UserMoneyWait> {
}
