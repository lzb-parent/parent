package com.pro.common.module.service.message.util;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 节点短信
 */
@Slf4j
public class NodeSmsUtils {
    private static AtomicInteger seq = new AtomicInteger(0);

    public static JSONObject sendSms(String url, String account, String password, String phone, String content) {
        Map<String, Object> params = new HashMap();
        params.put("account", account);
        params.put("password", password);
        params.put("seq", seq.incrementAndGet()); // 序列号，每次请求递增，初始值为 1
        params.put("time", System.currentTimeMillis());
        params.put("smstype", 0);
        params.put("sender", "???"); // 发件人ID
        params.put("numbers", phone); // 发件人ID
        params.put("content", content);
        params.put("mobile", phone);
        String result = HttpUtil.post(url, JSONUtil.toJsonStr(params));
        log.error("节点短信发送结果：{}", result);
        JSONObject jo = JSONUtil.parseObj(result);
        Integer code = jo.getInt("status");
        if (0 != code) {
            throw new BusinessException(EnumError.valueOf(code.toString().replace("-", "_")).getMsg());
        }
        JSONArray array = jo.getJSONArray("array");
        jo.put("sid", array.getJSONArray(0).get(1).toString());
        return jo;
    }

    @Getter
    @AllArgsConstructor
    enum EnumError {
        _1("认证错误"),
        _2("Ip访问受限"),
        _3("短信内容含有敏感字符"),
        _4("短信内容为空"),
        _5("短信内容过长"),
        _6("不是模板的短信"),
        _7("号码个数过多"),
        _8("号码为空"),
        _9("号码异常"),
        _10("该通道余额不足，不能满足本次发送"),
        _11("定时时间格式不对"),
        _12("由于平台的原因，批量提交出错，请与管理员联系"),
        _13("用户被锁定"),
        ;
        String msg;
    }
//
//    public static void main(String[] args) {
//        NodeSmsUtils.sendSms("http://47.241.32.151:20003/sendsms",
//                "O-@adiduojin",
//                "Qwer123.",
//                "55757438",
//                "Your SMS verification code is: {code}, if it is not your own operation, please ignore it!");
//    }
}
