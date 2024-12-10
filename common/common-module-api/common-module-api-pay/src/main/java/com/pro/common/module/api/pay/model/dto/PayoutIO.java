package com.pro.common.module.api.pay.model.dto;

import com.pro.common.module.api.pay.model.db.PayMerchant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 代付下单
 */
@Data
@Builder
public class PayoutIO {
    @Data
    @Builder
    public static class Params implements MchParams {
        private String ip;
        private String notifyUrl;//EnumDict.PAY_NOTIFY_URL
        private String payoutMchId;//payMerchant.payoutMchId
        private String payoutMchKey;//payMerchant.payoutMchId
        private String channelCode;//payoutChannel.channelCode
        private String payType;//payoutChannel.payType
        private String no;//userWithdraw.no
        private BigDecimal amount;//userWithdraw.amount
        private Long now_long;//now 1654320567000
        private String now_yyyyMMddHHmmss;//now
        private String cardType;
        private String bankAccount;
        private String bankId;
        private String bankName;
        private String bankUsername;
        private String bankBranchName;
        private String bankPhone;
        private String bankAddress;
        private String email;
        private String idCard;
        private String ifsc;
        private String upi;
        private String cci;
        private String wallet;
        private String username;
        private String countryCurrency;

        private PayMerchant payMerchant;

        @Override
        public String getMchId() {
            return this.payoutMchId;
        }

        @Override
        public String getMchKey() {
            return this.payoutMchKey;
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Result {
        Boolean success;
        String errorMsg;
        // 三方单号
        String payNo;

        public static Result success(String payNo) {
            return Result.builder()
                    .success(true)
                    .payNo(payNo)
                    .build();
        }

        public static Result fail(String errorMsg) {
            return Result.builder()
                    .success(false)
                    .errorMsg(errorMsg)
                    .build();
        }
    }
}
