package com.pro.common.module.api.message.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMsgRecordSendResult {
    public static final SysMsgRecordSendResult SUCCESS = success(null);
    Boolean success;
    String errorMsg;//失败的错误信息

    String msgId;   //消息唯一ID
    BigDecimal fee; //费用
    String unit;    //单位
    Integer count;  //条数

    public static SysMsgRecordSendResult success(String msgId) {
        SysMsgRecordSendResult rs = new SysMsgRecordSendResult();
        rs.setSuccess(true);
        rs.setMsgId(msgId);
        return rs;
    }

    public static SysMsgRecordSendResult error(String errorMsg) {
        SysMsgRecordSendResult rs = new SysMsgRecordSendResult();
        rs.setSuccess(false);
        rs.setErrorMsg(errorMsg);
        return rs;
    }
}
