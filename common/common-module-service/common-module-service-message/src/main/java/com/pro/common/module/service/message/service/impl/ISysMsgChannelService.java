package com.pro.common.module.service.message.service.impl;


import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.framework.api.structure.Tuple2;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 发消息
 */
public interface ISysMsgChannelService {
    SysMsgRecordSendResult EMPTY = new SysMsgRecordSendResult();

    /**
     * 单个发消息
     */
    default SysMsgRecordSendResult sendMsg(SysMsgChannelMerchant merchant, String account, SysMsgChannelTemplate template, Map<String, Object> paramMap, String senderId, String lang) {
        return sendMsgs(merchant, Collections.singletonList(new Tuple2<>(account, paramMap)), template, senderId, lang).get(0);
    }

//    default SysMsgRecordSendResult sendMsg(SysMsgChannelMerchant merchant, Tuple2<String, Map<String, ?>> accountAndParam, SysMsgChannelTemplate template, String senderId, String lang) {
//        return sendMsgs(merchant, Collections.singletonList(accountAndParam), template, senderId, lang).get(0);
//    }

    /**
     * 批量发消息
     *
     * @param merchant         消息通道对应的商户信息
     * @param accountAndParams 消息接收方账号 参数信息
     * @param template         消息模板
     * @param senderId         消息拟定发送者
     * @param lang
     * @return 发送结果
     */
    List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang);
}
