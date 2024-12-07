package com.pro.common.module.service.message.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class YPSmsUtils {

    public static JSONObject sendSms(SysMsgChannelMerchant merchant, String phone, String content, String no) {
        Map<String, Object> params = new HashMap();
        params.put("apikey", merchant.getApiKey());
        params.put("mobile", phone);
        params.put("uid", no);
        params.put("text", content);
//        params.put("callback_url", EnumDict.SMS_NOTIFY_URL.getValueCache());
        String result = HttpUtil.post(merchant.getBaseUrl(), params);
        log.error("云片短信发送结果：{}", result);
        JSONObject jo = JSONUtil.parseObj(result);
        if (0 != jo.getInt("code")) {
            throw new BusinessException(jo.getStr("msg"));
        }
        return jo;
    }
}
