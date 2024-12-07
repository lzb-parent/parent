package com.pro.common.web.security.component.xss;

import cn.hutool.http.HtmlUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @Description: 使用HttpServletRequestWrapper重新封装request参数
 */
public class XSSRequestWrapper extends HttpServletRequestWrapper {
    /**
     * 构造方法
     */
    public XSSRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 处理参数值
     */
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = dealString(values[i]);
            // 修复 前端传递&后端接收到了&amp 问题
            // 不能对header做处理,这里会报错 String sessionInfo = request.getHeader(Constant.X_SESSION_INFO);
            //            LoginInfoVo info = JSON.parseObject(sessionInfo, LoginInfoVo.class);
            encodedValues[i] = doDecode(encodedValues[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        // 修复 前端传递&后端接收到了&amp 问题
        // 不能对header做处理,这里会报错 String sessionInfo = request.getHeader(Constant.X_SESSION_INFO);
        //            LoginInfoVo info = JSON.parseObject(sessionInfo, LoginInfoVo.class);
        value = doDecode(value);
        return dealString(value);
    }

    private static String doDecode(String value) {
        if (value == null) {
            return null;
        }
        return HtmlUtil.unescape(value);
//        return URLDecoder.decode(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return dealString(value);
    }

    /**
     * 字符串处理
     */
    private String dealString(String value) {
        if (value != null) {
            value = XssFilterUtil.clean(value);
        }
        return value;
    }
}
