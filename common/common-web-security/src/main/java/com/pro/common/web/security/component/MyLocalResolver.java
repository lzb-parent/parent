package com.pro.common.web.security.component;

import cn.hutool.core.util.StrUtil;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.util.LogicUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MyLocalResolver implements LocaleResolver {

    public static String lang_default;

    public MyLocalResolver(String lang) {
        lang_default = lang;
    }

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        // 获取头部设置的语言
        String lang = LogicUtils.or(request.getHeader(FrameworkConst.Str.HEADER_LANGUAGE), lang_default);
        lang = lang.split(",")[0];
        List<String> langs = Arrays.stream(lang.split("-"))
                .flatMap(s -> Arrays.stream(s.split("_")))
                .filter(StrUtil::isNotBlank).collect(Collectors.toList());
        if (langs.size() > 1) {
            return new Locale(langs.get(0), langs.get(1));
        }
        return Locale.forLanguageTag(langs.get(0));
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {
    }
}
