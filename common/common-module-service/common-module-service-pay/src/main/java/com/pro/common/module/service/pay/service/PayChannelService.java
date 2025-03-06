package com.pro.common.module.service.pay.service;

import com.pro.common.module.api.pay.model.db.PayChannel;
import com.pro.common.module.service.pay.dao.PayChannelDao;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

/**
 * 支付渠道 服务实现类
 */
@Service
public class PayChannelService extends BaseService<PayChannelDao, PayChannel> {

    public PayChannel getByPayType(Long payChannelId, String merchantCode, String payType, String cardType) {
        if (payChannelId != null) {
            return getById(payChannelId);
        }
        return getOne(qw().lambda().eq(PayChannel::getMerchantCode, merchantCode)
                .eq(StrUtils.isNotBlank(payType), PayChannel::getPayType, payType)
                .eq(StrUtils.isNotBlank(cardType), PayChannel::getCardType, cardType)
                .last(FrameworkConst.Str.limit_1)
                .orderByAsc(PayChannel::getSort)
        );
    }
}
