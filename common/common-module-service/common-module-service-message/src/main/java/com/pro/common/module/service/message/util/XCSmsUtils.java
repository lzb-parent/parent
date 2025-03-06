package com.pro.common.module.service.message.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * 小船出海短信
 */
@Slf4j
public class XCSmsUtils {

    public static JSONObject sendSms(String url, String appkey, String secretkey, String phone, String content) {
        Map<String, Object> params = new HashMap();
        params.put("appkey", appkey);
        params.put("secretkey", secretkey);
        params.put("phone", phone);
        params.put("content", content);
        String result = HttpUtil.post(url, params);
        log.error("小船出海短信发送结果：{}", result);
        JSONObject jo = JSONUtil.parseObj(result);
        Integer code = jo.getInt("code");
        if (0 != code) {
//            throw new BusinessException(jo.getString("result"));
            throw new BusinessException("sms send error");
        }
//        JSONArray array = jo.getJSONArray("array");
//        jo.put("sid", array.getJSONArray(0).get(1).toString());
        return jo;
    }

}
