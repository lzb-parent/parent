package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.common.modules.service.dependencies.modelauth.base.MessageService;
import com.pro.framework.api.message.IBaseMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pro.framework.api.structure.Tuple2;

/**
 * 推送系统通知
 */
@Slf4j
@Service("sysMsgChannelServiceSiteMessage")
public class SysMsgChannelServiceSiteMessage implements ISysMsgChannelService {

    @Autowired
    private MessageService messageService;

    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
        for (Tuple2<String, Map<String, ?>> accountAndParam : accountAndParams) {
            SysMsgRecord record = SysMsgRecord.builder()
                    .titleTemplate(template.getTitleTemplate())
                    .contentTemplate(template.getContentTemplate())
                    .paramMap(accountAndParam.getT2())
                    .dialogPosterCode(template.getDialogPosterCode())
                    .build();
            messageService.sendToUser(ToSocket.toUser(record.getClass().getSimpleName(), record, Long.valueOf(accountAndParam.getT1())));
        }
        return accountAndParams.stream().map(userId -> SysMsgRecordSendResult.SUCCESS).collect(Collectors.toList());
    }
}
