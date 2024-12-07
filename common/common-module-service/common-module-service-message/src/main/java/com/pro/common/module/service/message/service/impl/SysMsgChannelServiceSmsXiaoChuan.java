package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.module.service.message.util.XCSmsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pro.framework.api.structure.Tuple2;

/**
 * 推送系统通知
 */
@Slf4j
@Service("sysMsgChannelServiceSmsXiaoChuan")
public class SysMsgChannelServiceSmsXiaoChuan implements ISysMsgChannelService {


    // public static final EnumMessageChannel ENUM_MESSAGE_CHANNEL = EnumMessageChannel.SMS_XIAO_CHUANG;


    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
        return accountAndParams.stream().map(accountAndParam -> {
            Map<String, ?> paramMap = accountAndParam.getT2();
            String title = StrUtil.format(template.getTitleTemplate(), paramMap);
            String content = StrUtil.format(template.getContentTemplate(), paramMap);
            return send(merchant, title, content, accountAndParam.getT1());
        }).collect(Collectors.toList());
    }


    private SysMsgRecordSendResult send(SysMsgChannelMerchant merchant, String title, String content, String phone) {
        try {
            JSONObject jo = XCSmsUtils.sendSms(merchant.getBaseUrl(),
                    merchant.getApiKey(),
                    merchant.getApiPwd(),
                    phone,
                    content);
            return SysMsgRecordSendResult.success(jo.getStr("sid"));
        } catch (Exception e) {
            e.printStackTrace();
            return SysMsgRecordSendResult.error(e.getMessage());
        }
    }
}
