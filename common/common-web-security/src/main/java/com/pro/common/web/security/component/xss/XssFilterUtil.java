package com.pro.common.web.security.component.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

/**
 * @Description: xss非法标签过滤
 */
public class XssFilterUtil {
    public static String clean(String content) {
        return Jsoup.clean(content, Safelist.none());
    }
}
