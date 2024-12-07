package com.pro.common.module.service.admin.service;

import com.pro.common.module.api.pay.model.db.PayBank;
import com.pro.common.module.service.admin.dao.PayBankDao;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

/**
 * 支付渠道 服务实现类
 */
@Service
public class PayBankService extends BaseService<PayBankDao, PayBank> {
}
