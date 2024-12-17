package com.pro.common.module.service.pay.service;

import com.pro.common.module.api.pay.model.db.PayCardType;
import com.pro.common.module.api.user.intf.IUserBankCardService;
import com.pro.common.module.api.user.model.db.UserBankCard;
import com.pro.common.module.service.pay.dao.PayCardTypeDao;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 例如 支付宝 微信 银行卡 Gcash PIX服务
 */
@Service
@Slf4j
public class PayCardTypeService extends BaseService<PayCardTypeDao, PayCardType> {
    @Autowired
    private IUserBankCardService userBankCardService;

    @Override
    public boolean updateById(PayCardType entity) {
        PayCardType old = getById(entity.getId());
        boolean ok = super.updateById(entity);
        Integer newSort = entity.getSort();
        Integer oldSort = old.getSort();
        if (ok && newSort != null && entity.getCode() != null) {
            // 更新sort
            UserBankCard param = new UserBankCard();
            param.setSort(Integer.valueOf(PayCardType.DEFAULT_SORT));
            UserBankCard setData = new UserBankCard();
            setData.setSort(newSort);
            userBankCardService.updates(param, setData, UserBankCard::getSort);
            // 更新sort
            UserBankCard param2 = new UserBankCard();
            param.setSort(oldSort);
            UserBankCard setData2 = new UserBankCard();
            setData2.setSort(newSort);
            userBankCardService.updates(param2, setData2, UserBankCard::getSort);
        }
        return ok;
    }
}
