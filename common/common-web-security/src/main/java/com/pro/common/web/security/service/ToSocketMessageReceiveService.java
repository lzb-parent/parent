package com.pro.common.web.security.service;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.common.web.security.websocket.SocketSender;
import com.pro.framework.message.IApplicationMessageReceiveService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.stereotype.Component;

@Component
@Getter
@Slf4j
public class ToSocketMessageReceiveService implements IApplicationMessageReceiveService<ToSocket> {
    @Autowired
    private SocketSender socketSender;
    private final String topicAppend = CommonConst.TopicAppend.ToSocket;
    private final Class<ToSocket> dataClass = ToSocket.class;

    @Override
    public void doReceive(ToSocket object, Message message) {
        String line = object.getData();
        socketSender.send(object);
    }
}
