package com.pro.common.module.api.agent.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumCustomerType {
    kf("客服"),
    telegram("Telegram"),
    whatsapp("Whatsapp"),
    line("Line"),
    facebook("Facebook"),
    ;
    String label;
}
