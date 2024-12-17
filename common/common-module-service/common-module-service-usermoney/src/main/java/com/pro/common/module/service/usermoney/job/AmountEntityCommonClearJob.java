package com.pro.common.module.service.usermoney.job;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import com.pro.common.module.service.usermoney.service.IAmountEntityService;
import com.pro.common.module.service.usermoney.service.IDayClearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数额变化的基础类
 */
@Service
@Slf4j
public class AmountEntityCommonClearJob {
    @Autowired
    private Map<String, IAmountEntityService> amountEntityServiceMap;
    @Autowired(required = false)
    private List<IDayClearService> dayClearServices;

    /**
     * 每日0点执行
     */
    @Scheduled(cron = "1 0 0 * * ?")
    public void clearDay() {
        LocalDateTime now = LocalDateTime.now();
        // 每日清理
        updateToZero("每日清理",
                AmountEntity::getTodayIncreaseAmount,
                AmountEntity::getTodayDecreaseAmount,
                AmountEntity::getTodayIncreaseTimes,
                AmountEntity::getTodayDecreaseTimes
        );

        // 每周清理
        if (DayOfWeek.MONDAY.equals(now.getDayOfWeek())) {
            updateToZero("每周清理",
                    AmountEntity::getWeeklyIncreaseAmount,
                    AmountEntity::getWeeklyDecreaseAmount,
                    AmountEntity::getWeeklyIncreaseTimes,
                    AmountEntity::getWeeklyDecreaseTimes
            );
            log.warn("每日清理");
        }

        // 每月清理
        if (1 == now.getDayOfMonth()) {
            updateToZero("每月清理",
                    AmountEntity::getMonthIncreaseAmount,
                    AmountEntity::getMonthDecreaseAmount,
                    AmountEntity::getMonthIncreaseTimes,
                    AmountEntity::getMonthDecreaseTimes
            );
        }

        // 每年清理
        if (1 == now.getDayOfYear()) {
            updateToZero("每年清理",
                    AmountEntity::getYearIncreaseAmount,
                    AmountEntity::getYearDecreaseAmount,
                    AmountEntity::getYearIncreaseTimes,
                    AmountEntity::getYearDecreaseTimes
            );
        }

        // 执行其他每日清理service
        if (dayClearServices != null) {
            dayClearServices.forEach(IDayClearService::clearDay);
        }
    }

    @SafeVarargs
    private <Entity extends AmountEntity> void updateToZero(String title, SFunction<Entity, Object>... funs) {
        for (IAmountEntityService<Entity> service : amountEntityServiceMap.values()) {
            LambdaUpdateChainWrapper<Entity> wrapper = service.lambdaUpdate();
            for (SFunction<Entity, Object> fun : funs) {
                wrapper.set(fun, 0);
            }
            wrapper.update();
        }
        log.warn("AmountEntity {} services|{}", title, amountEntityServiceMap.values().stream().map(IAmountEntityService<Entity>::getEntityClass).map(Class::getSimpleName).collect(Collectors.joining(",")));
    }

}
