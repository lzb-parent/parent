package com.pro.common.module.api.pay.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EnumUserRechargeProp {
    pic("上传截图"),
    merchantNo("交易单号"),
    bankName("付款银行"),
    bankUsername("付款人姓名"),
    bankAccount("付款账号"),
    ;

    String label;

}
