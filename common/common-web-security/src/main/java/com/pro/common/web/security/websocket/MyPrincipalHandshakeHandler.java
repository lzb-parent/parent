package com.pro.common.web.security.websocket;

import com.pro.common.web.security.model.LoginInfo;
import com.pro.common.web.security.service.TokenService;
import com.pro.framework.api.util.StrUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class MyPrincipalHandshakeHandler extends DefaultHandshakeHandler {
    private TokenService tokenService;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        Map<String, String> paramMap = StrUtils.parseParameters(request.getURI().getQuery());
        String token = paramMap.get("token");
        Long loginId = tokenService.getIdByToken(token);
        return new LoginInfo(null, loginId, null);
    }
}
