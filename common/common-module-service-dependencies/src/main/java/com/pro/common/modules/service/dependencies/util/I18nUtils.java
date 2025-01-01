package com.pro.common.modules.service.dependencies.util;

import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.framework.api.util.LogicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class I18nUtils {
    public static Map<String, Map<String, String>> transMap = Collections.emptyMap();
    private static MessageSource messageSource;
    private static Set<String> errorKeySet = new HashSet<>();


    @Autowired
    public I18nUtils(MessageSource messageSource) {
        I18nUtils.messageSource = messageSource;
    }


    /**
     * 抛出异常
     */
    public static void throwBusinessException(String template, Object... params) {
        throw newBusinessException(template, params);
    }

    /**
     * 创建异常
     */
    public static BusinessException newBusinessException(String template, Object... params) {
        return new BusinessException(template, params);
    }

    /**
     * 获取单个国际化翻译值
     */
    public static String get(String msgKey) {
        return get(msgKey, (Object[]) null);
    }

    public static String get(String msgKey, Object... params) {
        return getByLang(LocaleContextHolder.getLocale().toLanguageTag(), msgKey, params);
    }

    public static String getByLang(String languageTag, String msgKey, Object... params) {
        languageTag = LogicUtils.or(languageTag, CommonConst.Str.DEFAULT_LANG_EN);
        Map<String, String> langMap = getLangMap(languageTag);
        String message = langMap.get(msgKey);
        if (message != null) {
            if (null != params) {
                for (int i = 0; i < params.length; i++) {
                    message = message.replace("{" + i + "}", String.valueOf(params[i]));
                }
            }
            return message;
        }
        String message1 = null;
        try {
            Locale locale = Locale.forLanguageTag(languageTag);
            message1 = messageSource.getMessage(msgKey, params, locale);
        } catch (NoSuchMessageException e) {
            if (!errorKeySet.contains(msgKey)) {
                errorKeySet.add(msgKey);
                log.warn("{}", e.getMessage());
            }
        }
        return message1;
    }

    private static Map<String, String> getLangMap(String lang) {
        if (lang == null) {
            return Collections.emptyMap();
        }
        lang = lang.replace("_", "-");
//        Console.log("当前语言：{}", lang);
        return transMap.getOrDefault(lang, Collections.emptyMap());
    }


}
