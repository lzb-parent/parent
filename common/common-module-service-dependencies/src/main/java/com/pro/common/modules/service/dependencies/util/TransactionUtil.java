package com.pro.common.modules.service.dependencies.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
public class TransactionUtil {
    private static ThreadPoolTaskExecutor executor;

    @Autowired
//    @Qualifier("taskExecutor")
    public void setExecutor(ThreadPoolTaskExecutor executor) {
        TransactionUtil.executor = executor;
    }

    /**
     * 事务后执行
     */
    public static void doAfter(Runnable runnable) {
        // 如果有事务 事务后执行
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
                @Override
                public void afterCommit() {
                    executor.execute(runnable);
                }
            });
        }
        // 如果无事务 直接执行
        else {
            runnable.run();
        }
    }
}
