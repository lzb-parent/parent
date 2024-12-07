package com.pro.common.module.service.message.service;

import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.service.message.dao.SysMsgChannelMerchantDao;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

/**
 * 国家 服务实现类
 */
@Service
public class SysMsgChannelMerchantService extends BaseService<SysMsgChannelMerchantDao, SysMsgChannelMerchant> {
    public SysMsgChannelMerchant getOne(EnumSysMsgChannel channel) {
        return lambdaQuery()
                .eq( SysMsgChannelMerchant::getChannel, channel)
                .eq(SysMsgChannelMerchant::getEnabled, true)
                .orderByAsc(SysMsgChannelMerchant::getSort)
                .last("limit 1")
                .one();
    }
}
