package com.pro.common.module.service.paythird.controller.common;

import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.dto.MchParams;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 代付下单
 */
@Data
@Builder
public class UnifiedOrderIO {
    @Data
    @Builder
    public static class Params implements MchParams {
        private Long userId;
        private String phone;//user.phone
        private String email;//user.phone
        private String username;//user.username
        private String notifyUrl;//EnumDict.PAY_NOTIFY_URL
        private String returnUrl;//requestVo.getReturnUrl()
        private String mchId;//payMerchant.mchId
        private String mchKey;//payMerchant.mchKey
//        private String channelCode;//payChannel.channelCode
        private String payType;//payChannel.payType
        private String no;//userRecharge.no
        private String ip;
        private BigDecimal money;//userRecharge.money
        private Long now_long;//now
        private String now_yyyyMMddHHmmss;//now
        private String now_yyyy_MM_dd_HH_mm_ss;//now
        private String countryCurrency;//now

        private PayMerchant payMerchant;

    }

    @Data
    @Builder
    public static class Result {
        private Boolean success;
        private String errorMsg;
        private String payUrl;
    }
}
