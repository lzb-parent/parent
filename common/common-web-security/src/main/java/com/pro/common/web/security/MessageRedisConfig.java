package com.pro.common.web.security;

import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.system.model.intf.IAuthDictService;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.message.BaseApplicationMessageListener;
import com.pro.framework.message.IApplicationMessageReceiveService;
import com.pro.framework.message.IApplicationMessageSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

import java.util.List;

@Configuration
public class MessageRedisConfig {

    /**
     * 订阅消息
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            IAuthDictService authDictService,
                                            CommonProperties commonProperties,
                                            List<IApplicationMessageReceiveService> messageReceiveServices,
                                            IApplicationMessageSerializer serializeService
    ) {
        String systemVersion = authDictService.getValueOnStart(EnumDict.SYSTEM_VERSION);
        EnumApplication application = commonProperties.getApplication();

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(
                // 消息监听任务
                new BaseApplicationMessageListener(CollUtils.listToMap(messageReceiveServices, IApplicationMessageReceiveService::getTopicAppend), serializeService),
                // 消息模糊匹配
                new PatternTopic(systemVersion + "_" + application + "_*")
        );
        return container;
    }

    /**
     * redis服务器间消息的序列和反系列化
     */
    @Bean
    @ConditionalOnMissingBean
    GenericJackson2JsonRedisSerializer applicationMessageSerializer(
    ) {
        return new GenericJackson2JsonRedisSerializer();
    }


}
