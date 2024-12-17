package com.pro.common.module.service.usermoney.service;

import com.pro.common.module.api.usermoney.model.db.UserMoneyWaitRecord;
import com.pro.common.module.service.usermoney.dao.UserMoneyWaitRecordDao;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资金待结算李璐服务
 */
@Service
@Slf4j
public class UserMoneyWaitRecordService extends BaseService<UserMoneyWaitRecordDao, UserMoneyWaitRecord> {
    public List<UserMoneyWaitRecord> getActiveList(UserMoneyWaitRecord demo) {
        return this.lambdaQuery().setEntity(demo)
                .list();
    }
}
