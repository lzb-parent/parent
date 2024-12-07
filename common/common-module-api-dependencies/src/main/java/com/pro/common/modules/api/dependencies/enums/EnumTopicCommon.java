package com.pro.common.modules.api.dependencies.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EnumTopicCommon {
    socketToUser("推送消息给用户端UI"),
    socketToBack("推送消息给后台UI"),
    ;
    String label;
}
