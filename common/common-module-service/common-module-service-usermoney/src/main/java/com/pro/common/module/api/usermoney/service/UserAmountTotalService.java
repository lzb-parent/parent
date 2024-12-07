package com.pro.common.module.api.usermoney.service;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.pro.common.module.api.usermoney.dao.UserAmountTotalDao;
import com.pro.common.module.api.usermoney.model.db.UserAmountTotal;
import com.pro.common.module.api.usermoney.model.enums.EnumTradeType;
import com.pro.common.modules.api.dependencies.user.intf.IUserRegisterInitService;
import com.pro.common.modules.api.dependencies.user.model.IUser;
import com.pro.framework.api.structure.Tuple2;
import com.pro.framework.mybatisplus.BaseService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 资金待结算总计服务
 */
@Slf4j
//@Service
//        (CommonConst.Bean.UserAmountTotalService)
public class UserAmountTotalService<M extends UserAmountTotalDao<T>, T extends UserAmountTotal> extends BaseService<M, T> implements IUserRegisterInitService {
    @Override
    @SneakyThrows
    public void init(IUser user) {
        T entity = getEntityClass().newInstance();
        entity.setUserId(user.getId());
        entity.setUsername(user.getUsername());
        entity.setIsDemo(user.getIsDemo());
        this.save(entity);
    }

    /**
     * 加减某些字段
     * update user set login_times = login_times + 1
     */
    public void addIncreaseField(
            Long userId,
            List<Tuple2<String, Number>> addNums
    ) {
        // 查询条件
        LambdaUpdateChainWrapper<T> uw = lambdaUpdate();
//        query.accept(uw);
        uw.eq(T::getUserId, userId);
        // 累加属性
        uw.setSql(
                addNums.stream().map(addNum -> {
                    String fieldName = addNum.getT1();
                    Number number = addNum.getT2();
                    return fieldName + " = " + fieldName + " + " + number;

                }).collect(Collectors.joining(",")));
        uw.update();
    }

    public void batchIncrease(Collection<UserAmountTotal> increaseList) {
        if (!increaseList.isEmpty()) {
            this.baseMapper.batchIncrease(increaseList);
        }
    }


    /**
     * 1.总计 初始化
     */
    @SneakyThrows
    public T newTotalZero(Long userId) {
        T totalNew = getEntityClass().newInstance();
        totalNew.setUserId(userId);
        totalNew.setTodayCommissionMoney(BigDecimal.ZERO);
        totalNew.setCommissionMoney(BigDecimal.ZERO);
        totalNew.setTodayRechargeMoney(BigDecimal.ZERO);
        totalNew.setTotalRechargeMoney(BigDecimal.ZERO);
        totalNew.setTotalRechargeTimes(0);
        totalNew.setTodayTkMoney(BigDecimal.ZERO);
        totalNew.setTotalTkMoney(BigDecimal.ZERO);
        totalNew.setTodayTkTimes(0);
        totalNew.setTotalTkTimes(0);
        return totalNew;
    }



    public void addUserAmountTotal(T total, String recordType, BigDecimal amount) {
        EnumTradeType tradeType = EnumTradeType.MAP.get(recordType);
        if (tradeType != null) {
            switch (tradeType) {
                case RECHARGE_BACK:
                case RECHARGE_ONLINE:
                    total.setTodayRechargeMoney(total.getTodayRechargeMoney().add(amount));
                    total.setTotalRechargeMoney(total.getTotalRechargeMoney().add(amount));
                    total.setTotalRechargeTimes(total.getTotalRechargeTimes() + 1);
                    break;
                case WITHDRAW:
                    total.setTotalTkMoney(total.getTotalTkMoney().add(amount));
                    total.setTodayTkMoney(total.getTodayTkMoney().add(amount));
                    total.setTotalTkTimes(total.getTotalTkTimes() + 1);
                    total.setTodayTkTimes(total.getTodayTkTimes() + 1);
                    break;
                case WITHDRAW_REJECT:
                    total.setTotalTkMoney(total.getTotalTkMoney().subtract(amount));
                    total.setTodayTkMoney(total.getTodayTkMoney().subtract(amount));
                    total.setTotalTkTimes(total.getTotalTkTimes() - 1);
                    total.setTodayTkTimes(total.getTodayTkTimes() - 1);
                    break;

                case RECHARGE_TG_COMMISSION:
                case REGISTER_TG_COMMISSION:
                case VIP_TG_COMMISSION:
                    total.setCommissionMoney(total.getCommissionMoney().add(amount));
                    total.setTodayCommissionMoney(total.getTodayCommissionMoney().add(amount));
                    break;

            }
        }
    }
}
