package com.pro.common.web.security.websocket;

import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MyWebSocketHandlerDecoratorFactory implements WebSocketHandlerDecoratorFactory {
    // 已登录用户
    public static final Map<Long, ILoginInfo> loginUserMap = new ConcurrentHashMap<>(4096);

    @Override
    public @NonNull
    WebSocketHandler decorate(@NonNull WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {
            @Override
            public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
                log.info("建立连接==》[{} : {}] has be connected...", session.getUri(), session.getId());
                ILoginInfo userInfoVo = (ILoginInfo) session.getPrincipal();
                if (userInfoVo != null && null != userInfoVo.getId()) {
                    loginUserMap.put(userInfoVo.getId(), userInfoVo);
                }
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus closeStatus) throws Exception {
                ILoginInfo userInfoVo = (ILoginInfo) session.getPrincipal();
                if (null != userInfoVo && null != userInfoVo.getId()) {
                    loginUserMap.remove(userInfoVo.getId());
                }
                log.debug("{}：断开连接了", session.getId());
                super.afterConnectionClosed(session, closeStatus);
            }

            @Override
            public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
                log.debug("{}：handleTransportError", session.getId(), exception);
                super.handleTransportError(session, exception);
            }
        };
    }
}
