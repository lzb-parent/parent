package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.module.service.message.util.EmailService;
import com.pro.framework.api.structure.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 小船出海短信
 */
@Slf4j
@Service("sysMsgChannelServiceEmailGoogle")
public class SysMsgChannelServiceEmailGoogle implements ISysMsgChannelService {

    // public static final EnumMessageChannel ENUM_MESSAGE_CHANNEL = EnumMessageChannel.EMAIL;
    @Autowired
    EmailService emailService;
    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
        return accountAndParams.stream().map(accountAndParam -> {
            Map<String, ?> paramMap = accountAndParam.getT2();
            String title = StrUtil.format(template.getTitleTemplate(), paramMap);
            String content = StrUtil.format(template.getContentTemplate(), paramMap);
            return send(merchant,title, content, accountAndParam.getT1());
        }).collect(Collectors.toList());
    }

    private SysMsgRecordSendResult send(SysMsgChannelMerchant merchant, String title, String content, String account) {
        try {
//            EmailUtils.sendEmail(
//                    account, // 邮箱地址
//                    StrUtil.format("【{}】{}", EnumDict.SITE_NAME.getValueCache(), I18nUtils.get(smsRecord.getType().getLabel())),
//                    I18nUtils.get("邮件验证码内容", smsRecord.getCode()));
            //发邮件
            emailService.sendEmail(
                    merchant,
                    account,
                    title,
                    content
            );
            return SysMsgRecordSendResult.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return SysMsgRecordSendResult.error(e.getMessage());
        }
    }
}
