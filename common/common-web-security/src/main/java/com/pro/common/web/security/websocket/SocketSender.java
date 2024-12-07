package com.pro.common.web.security.websocket;

import com.pro.common.modules.api.dependencies.message.ToSocket;
import lombok.AllArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class SocketSender {
    private SimpMessagingTemplate socketSender;

    /***
     * 2.各个订阅端(game-user)发送socket消息给客户浏览器
     */
    public void send(ToSocket data) {
        if (null != data) {
            if (data.getIsAllUser()) {
                this.sendUserAll(data);
            } else {
                Long[] userIds = data.getUserIds().toArray(new Long[]{});
                this.socketSendUser(data, userIds);
            }
        }
    }

    public void socketSendUser(ToSocket data, Long... userIds) {
        for (Long userId : userIds) {
            this.sendToUser(userId.toString(), data);
        }
    }

    /**
     * 给所有用户发消息
     */
    public void sendUserAll(ToSocket data) {
        data.setIsAllUser(null);
        data.setUserIds(null);
        socketSender.convertAndSend("/defaultTopic", data);
    }

    /**
     * 给单个用户发消息
     */
    public void sendToUser(String principleUsername, ToSocket data) {
        data.setIsAllUser(null);
        data.setUserIds(null);
        socketSender.convertAndSendToUser(principleUsername, "/defaultTopic", data);
    }
}
