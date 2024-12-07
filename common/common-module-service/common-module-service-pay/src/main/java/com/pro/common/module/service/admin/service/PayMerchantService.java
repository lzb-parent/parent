package com.pro.common.module.service.admin.service;

import cn.hutool.core.lang.Console;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.service.admin.dao.PayMerchantDao;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.util.IPUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 支付商户 服务实现类
 */
@Service
public class PayMerchantService extends BaseService<PayMerchantDao, PayMerchant> {
    @Autowired
    private PayChannelService payChannelService;
    @Autowired
    private PayoutChannelService payoutChannelService;
    @Autowired
    private PayBankService payBankService;

    public PayMerchant getByCode(String code) {
        return getOne(qw().lambda().eq(PayMerchant::getCode, code));
    }

    public void checkNotifyIp(String whitelist, HttpServletRequest request) {
        String ip = IPUtils.getIpAddress(request);
        if (StrUtils.isNotBlank(whitelist) && Arrays.stream(whitelist.split(",")).noneMatch(ip::equals)) {
            Console.log("您的IP:{}不在白名单中，无法调用回调接口.", ip);
            throw new BusinessException("您的IP:{}不在白名单中，无法调用回调接口.", ip);
        }
    }

    @Override
    public boolean updateById(PayMerchant entity) {
        PayMerchant pa = this.getById(entity.getId());
        if (entity.getShowAdmin()) {

        }

        return super.updateById(entity);
    }
}
