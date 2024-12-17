package com.pro.common.web.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 */
@Slf4j
@Configuration
@EnableAsync
@DependsOn("jTDServiceImpl")
public class TaskConfig {

    @Autowired
    private TaskExecutorBuilder taskExecutorBuilder;

    @Bean
    @Primary
    public ThreadPoolTaskExecutor taskExecutor() {
        return taskExecutorBuilder.build();
    }
}
