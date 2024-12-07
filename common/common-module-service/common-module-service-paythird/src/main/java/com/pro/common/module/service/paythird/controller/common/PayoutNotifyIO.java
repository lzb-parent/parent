package com.pro.common.module.service.paythird.controller.common;

import lombok.Builder;
import lombok.Data;

/**
 * 三方代收/代付,成功或失败通知
 */
@Data
@Builder
public class PayoutNotifyIO {
    @Data
    @Builder
    public static class Params {
       private String no;
       private Boolean statusPending;
       private Boolean statusFail;
       private Boolean statusSuccess;
       private String amount;
       private String payNo;
       private String message;
    }
}
