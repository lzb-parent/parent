package com.pro.common.module.service.message.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * sendinblue 邮件
 * 接口文档: https://developers.sendinblue.com/?lang=en
 */
@Slf4j
public class SendInBlueMailUtils {

    public static JSONObject sendEmail(String url,
                                       String apiKey,
                                       String senderName,
                                       String senderMail,
                                       String toName,
                                       String toEmail,
                                       String title,
                                       String content) {
        // 发件人
        Map<String, Object> sender = new HashMap<>();
        sender.put("name", senderName);
        sender.put("email", senderMail);

        // 收件人
        JSONObject to = new JSONObject();
        to.put("name", toName);
        to.put("email", toEmail);
        JSONArray toArray = new JSONArray();
        toArray.add(to);

        Map<String, Object> params = new HashMap<>();
        params.put("sender", sender);
        params.put("to", toArray);
        params.put("subject", title); // 邮件标题（100个字符以内）
        params.put("htmlContent", content);

        log.error("SendInBlueMail邮件发送参数：{}", params);
        HttpRequest request = HttpUtil.createPost(url).body(JSONUtil.toJsonStr(params));
        request.header("api-key", apiKey);
        request.header("accept", "application/json");
        String result = request.execute().body();
        log.error("SendInBlueMail邮件发送结果：{}", result);
        JSONObject jo = JSONUtil.parseObj(result);
        if (StrUtil.isBlank(jo.getStr("messageId"))) {
            throw new BusinessException(jo.getStr("message"));
        }
        return jo;
    }

    public static void main(String[] args) {
        SendInBlueMailUtils.sendEmail(
            "https://api.sendinblue.com/v3/smtp/email",
                "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",
                "MTBTC",
                "zc220399@gmail.com",
                "xiaoxiaomao666888@gmail.com",
                "xiaoxiaomao666888@gmail.com",
                "【MTBTC】邮件验证码",
                "<html><head></head><body><div style='width: 400px;margin: 0 auto;'>"+"<img src='http://180.178.35.226/logo_header.png' width='100%'/><br/><h4>尊敬的用户, 您好！您的验证码是：123456。</h4><br/>这是您私人的邮箱验证码，注意隐私安全，请勿随意发给别人，如果不是本人操作，请忽略！！！"+"</div></body></html>"
            );
    }
}
