package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.service.message.util.SubMailUtils;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
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
@Service("sysMsgChannelServiceSubmail")
public class SysMsgChannelServiceEmailSubmail implements ISysMsgChannelService {

    // public static final EnumMessageChannel ENUM_MESSAGE_CHANNEL = EnumMessageChannel.EMAIL;

    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
        return accountAndParams.stream().map(accountAndParam -> {
            Map<String, ?> paramMap = accountAndParam.getT2();
            String title = StrUtil.format(template.getTitleTemplate(), paramMap);
            String content = StrUtil.format(template.getContentTemplate(), paramMap);
            return send(merchant,title, content, accountAndParam.getT1(), paramMap);
        }).collect(Collectors.toList());
    }

    private SysMsgRecordSendResult send(SysMsgChannelMerchant merchant, String title, String content, String account, Map<String, ?> paramMap) {
        try {
            JSONObject cfg = StrUtil.isBlank(merchant.getOtherJson()) ? null : JSONUtil.parseObj(merchant.getOtherJson());
            //发邮件
            JSONObject jo = SubMailUtils.sendEmail(
                    merchant.getBaseUrl(),
                    merchant.getApiKey(),
                    merchant.getApiPwd(),
                    cfg==null?null:cfg.getStr("from"),
                    account, // 邮箱地址
                    //                        StrUtil.format("【{}】{}", EnumDict.SITE_NAME.getValueCache(), I18nUtils.get(smsRecord.getType().getLabel())),
                    //                        I18nUtils.get("邮件验证码内容", smsRecord.getCode()));
                    I18nUtils.get("欢迎加入", EnumDict.SITE_NAME.getValueCache()),
                    I18nUtils.get("邮件验证码", paramMap.get("code")));
//            smsRecord.setSid(jo.getString("send_id"));
            return SysMsgRecordSendResult.success(jo.getStr("messageId"));
        } catch (Exception e) {
            e.printStackTrace();
            return SysMsgRecordSendResult.error(e.getMessage());
        }
    }
}
