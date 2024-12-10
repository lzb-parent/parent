package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.util.StrUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.pro.framework.api.structure.Tuple2;

/**
 * 小船出海短信
 */
@Slf4j
@Service("sysMsgChannelServiceSmsOtp")
public class SysMsgChannelServiceSmsOtp implements ISysMsgChannelService {

    public static final EnumSysMsgChannel ENUM_SMS_CHANNEL = EnumSysMsgChannel.SMS_OTP;

    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
        return accountAndParams.stream().collect(Collectors.groupingBy(accountAndParam -> getContent(template, accountAndParam))).entrySet().stream().flatMap(e -> {
            String content = e.getKey();
            List<String> accounts = e.getValue().stream().map(Tuple2::getT1).collect(Collectors.toList());
            return send(merchant, accounts, template, content).stream();
        }).collect(Collectors.toList());
    }

    private static String getContent(SysMsgChannelTemplate template, Tuple2<String, Map<String, ?>> accountAndParam) {
        return StrUtil.format(template.getContentTemplate(), accountAndParam.getT2());
    }

    private List<SysMsgRecordSendResult> send(SysMsgChannelMerchant merchant, List<String> accounts, SysMsgChannelTemplate template, String content) {

        String appId = template.getAppId();
//        String content = StrUtil.format(template.getContentTemplate(), paramMap);
        final String baseUrl = merchant.getBaseUrl();
        final String apiKey = merchant.getApiKey();
        final String apiPwd = merchant.getApiPwd();

        final String numbers = String.join(",", accounts);

        final String url = baseUrl.concat("/sms/batch/v2");

        HttpRequest request = HttpRequest.post(url);

        //generate md5 key
//        final String datetime = String.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
//        final String sign = SecureUtil.md5(apiKey.concat(apiPwd).concat(datetime));

//        request.header(Header.CONNECTION, "Keep-Alive")
//                .header(Header.CONTENT_TYPE, "application/json;charset=UTF-8")
//                .header("Sign", sign)
//                .header("Timestamp", datetime)
//                .header("Api-Key", apiKey);


        JSONObject jsonObject = JSONUtil.createObj()
                .set("appkey", apiKey)
                .set("appcode", appId)
                .set("appsecret", apiPwd)
                .set("phone", numbers)
                .set("msg", content);
//        if (senderId != null) {
//            jsonObject.set("senderId", senderId);
//        }
        log.info("SMS {} request: url {}", ENUM_SMS_CHANNEL, url);
        log.info("SMS {} request: {}", ENUM_SMS_CHANNEL, jsonObject.toString());
        HttpResponse response = null;
        if (false) {//SpringContextUtils.isDev()
            log.info("SMS {} result: {}", ENUM_SMS_CHANNEL, " isDev success");
            return Collections.singletonList(SysMsgRecordSendResult.SUCCESS);
        } else {
            response = request.form(jsonObject).execute();
        }
        if (response.isOk()) {
            String result = response.body();
            log.info("SMS {} result: {}", ENUM_SMS_CHANNEL, result);

            //{
            //   "status": "0",
            //   "reason": "success",
            //   "success": "2",
            //   "fail": "0",
            //   "array":[
            //     {
            //       "msgId": "2108021054011000095",
            //       "number": "91856321412"
            //     },
            //     {
            //       "msgId": "2108021059531000096",
            //       "number": "91856321413"
            //     }
            //   ]
            // }
            OptSmsReturn returnJson = JSONUtil.toBean(result, OptSmsReturn.class);
            if ("00000".equals(returnJson.getCode())) {
                return returnJson.getArray();
            } else {
                log.error("SMS {} result: {}", ENUM_SMS_CHANNEL, result);
                throw new BusinessException(ENUM_SMS_CHANNEL + " SMS ERROR:" + (StrUtils.isContainsChinese(result) ? "请联系管理员检查短信通道配置" : result));
//                throw new BusinessException(ENUM_SMS_CHANNEL + " SMS ERROR:" + (StrUtils.isContainsChinese(result) ? I18nUtils.get("请联系管理员检查短信通道配置") : result));
            }
        } else {
            log.error("send message fail|{}|{}", merchant, response);
            return accounts.stream().map(p -> SysMsgRecordSendResult.error("unknown error")).collect(Collectors.toList());
        }
    }

    @Data
    public static class OptSmsReturn {
        private String code;
        private String desc;
        private String success;
        private String fail;
        private List<SysMsgRecordSendResult> array;
    }
}
