package com.pro.common.module.service.message.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Submail邮件
 * 文档：https://en.mysubmail.com/documents/4MfRT2
 */
@Slf4j
public class SubMailUtils {

    public static JSONObject sendEmail(String url,
                                       String apiKey,
                                       String apiPwd,
                                       String from,
                                       String to,
                                       String title,
                                       String content) {
        Map<String, Object> params = new HashMap<>();
        params.put("appid", apiKey);
        // 收件人地址 (多个联系人用半角“,”隔开： e.g. "leo <leo@submail.cn>,<info@mysubmail.com>, service@submail.cn",单次请求提交邮箱数量控制在100个以内，SUBMAIL 支持完整的 RFC 822 收件人标准，请确保您的邮件地址的有效性。请参见 维基百科 EMAIL ADDRESS RFC822 文档)
        params.put("from", from);
        params.put("to", to);
        params.put("subject", title); // 邮件标题（100个字符以内）
//        params.put("text", content); // 纯文本邮件正文
        params.put("html", content); // HTML 邮件正文
        params.put("signature", apiPwd);  // appkey或数字签名
        log.info("Submail邮件发送参数：{}", params);
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(params));
        log.error("Submail邮件发送结果：{}", result);
        JSONObject jo = JSONUtil.parseObj(result);
        if (!"success".equalsIgnoreCase(jo.getStr("status"))) {
            throw new BusinessException(jo.getStr("msg"));
        }
        return jo.getJSONArray("return").getJSONObject(0);
    }

    public static void main(String[] args) {
        SubMailUtils.sendEmail(
                "https://api-v4.mysubmail.com/mail/send.json",
                    "17381",
                    "d1f456a2e0060601ef2002cdb79592fd",
                    "service@mtbtc.com",
                    "xiaoxiaomao666888@gmail.com",
                    "【MTBTC】注册验证码",
                "<html><head></head><body><div style='width: 400px;margin: 0 auto;'>"+"<img src='http://180.178.35.226/logo_header.png' width='100%'/><br/><h4>尊敬的用户, 您好！您的验证码是：123456。</h4><br/>这是您私人的邮箱验证码，注意隐私安全，请勿随意发给别人，如果不是本人操作，请忽略！！！"+"</div></body></html>"
                );
    }
}
