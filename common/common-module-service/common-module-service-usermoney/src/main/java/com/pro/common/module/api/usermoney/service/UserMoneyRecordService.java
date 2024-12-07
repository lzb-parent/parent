package com.pro.common.module.api.usermoney.service;

import com.pro.common.module.api.usermoney.model.db.UserMoneyRecord;
import com.pro.common.module.api.usermoney.dao.UserMoneyRecordDao;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资金待结算李璐服务
 */
@Service
@Slf4j
public class UserMoneyRecordService extends BaseService<UserMoneyRecordDao, UserMoneyRecord> {
    public List<UserMoneyRecord> getActiveList(UserMoneyRecord demo) {
        return this.lambdaQuery().setEntity(demo)
                .list();
    }
}
