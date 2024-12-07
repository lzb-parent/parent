package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.module.service.message.util.SendInBlueMailUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.pro.framework.api.structure.Tuple2;

/**
 * 小船出海短信
 */
@Slf4j
@Service("sysMsgChannelServiceEmailSendinblue")
public class SysMsgChannelServiceEmailSendinblue implements ISysMsgChannelService {

    // public static final EnumMessageChannel ENUM_MESSAGE_CHANNEL = EnumMessageChannel.EMAIL;


    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
        return accountAndParams.stream().map(accountAndParam -> {
            Map<String, ?> paramMap = accountAndParam.getT2();
            String title = StrUtil.format(template.getTitleTemplate(), paramMap);
            String content = StrUtil.format(template.getContentTemplate(), paramMap);
            return send(merchant,title, content, accountAndParam.getT1(), paramMap, lang);
        }).collect(Collectors.toList());
    }


    private SysMsgRecordSendResult send(SysMsgChannelMerchant merchant, String title, String content, String account, Map<String, ?> paramMap, String lang) {
        try {
            JSONObject cfg = StrUtil.isBlank(merchant.getOtherJson()) ? new JSONObject() : JSONUtil.parseObj(merchant.getOtherJson());
            JSONObject jo = SendInBlueMailUtils.sendEmail(
                    merchant.getBaseUrl(),
                    merchant.getApiKey(),
                    cfg.getStr("fromName"),
                    cfg.getStr("from"),
                    account, // 收件人
                    account, // 收件人邮箱
                    title,
                    content);
            return SysMsgRecordSendResult.success(jo.getStr("messageId"));
        } catch (Exception e) {
            e.printStackTrace();
            return SysMsgRecordSendResult.error(e.getMessage());
        }
    }
}
