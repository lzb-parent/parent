package com.pro.common.module.service.paythird.controller.common;

import lombok.Builder;
import lombok.Data;

/**
 * 三方代收/代付,成功或失败通知
 */
@Data
@Builder
public class NotifyIO {
    @Data
    @Builder
    public static class Params {
       private String no;
       private Boolean statusSuccess;
       private Boolean statusPending;
       private Boolean statusFail;
       private String amount;
       private String payNo;
    }
}
