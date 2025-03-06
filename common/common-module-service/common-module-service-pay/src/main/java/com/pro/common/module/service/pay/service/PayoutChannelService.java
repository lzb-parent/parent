package com.pro.common.module.service.pay.service;

import com.pro.common.module.api.pay.model.db.PayoutChannel;
import com.pro.common.module.service.pay.dao.PayoutChannelDao;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

/**
 * 代付渠道 服务实现类
 */
@Service
public class PayoutChannelService extends BaseService<PayoutChannelDao, PayoutChannel> {
}

