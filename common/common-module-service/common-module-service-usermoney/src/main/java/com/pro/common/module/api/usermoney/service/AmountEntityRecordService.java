package com.pro.common.module.api.usermoney.service;

import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.module.api.usermoney.dao.AmountEntityRecordDao;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

/**
 * 数额变化的基础类
 */
@Service
public class AmountEntityRecordService extends BaseService<AmountEntityRecordDao, AmountEntityRecord> {
}
