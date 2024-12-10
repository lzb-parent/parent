package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.system.model.intf.IAuthDictService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.framework.message.BaseApplicationMessageSender;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageService {
    private IAuthDictService authDictService;
    private BaseApplicationMessageSender messageSendRedisService;

    /**
     * 给 user.jar推送消息
     */
    public void sendToUser(ToSocket data) {
        this.sendMessageToServers(List.of(EnumApplication.user), CommonConst.TopicAppend.ToSocket, data);
    }

    /**
     * 给 admin.jar,agent.jar 推送消息
     */

    public void sendToManager(ToSocket data) {
        this.sendMessageToServers(List.of(EnumApplication.admin, EnumApplication.agent), CommonConst.TopicAppend.ToSocket, data);
    }

    public void sendMessageToServers(List<EnumApplication> applications, String topicAppend, Object data) {
        applications.forEach(application -> messageSendRedisService.sendMessageToServers(getTopicName(application, topicAppend), data));
    }

    private String getTopicName(EnumApplication application, String topicAppend) {
        return authDictService.getValueOnStart(EnumDict.SYSTEM_VERSION) + "_" + application + "_" + topicAppend;
    }
}
