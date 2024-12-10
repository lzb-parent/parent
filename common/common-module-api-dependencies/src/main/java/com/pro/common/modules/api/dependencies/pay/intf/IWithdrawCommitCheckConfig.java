package com.pro.common.modules.api.dependencies.pay.intf;

import java.math.BigDecimal;

public interface IWithdrawCommitCheckConfig {

    BigDecimal getWithdrawMoneyMin();

    BigDecimal getWithdrawMoneyMax();

    String getWithdrawMoneyPoster();

    Integer getWithdrawTimes();

    Integer getWithdrawFreeTimes();

    Integer getWithdrawFreeMonthTimes();

    Integer getWithdrawFreeTotalTimes();

    String getWithdrawTimesPoster();

    BigDecimal getWithdrawFee();

    BigDecimal getWithdrawFeeMoney();
}
