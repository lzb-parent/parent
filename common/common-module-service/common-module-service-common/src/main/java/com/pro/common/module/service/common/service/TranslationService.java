package com.pro.common.module.service.common.service;

import com.pro.common.module.service.common.dao.TranslationDao;
import com.pro.common.module.api.common.model.db.Translation;
import com.pro.common.module.api.common.model.intf.ITranslationService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 国家 服务实现类
 */
@Service
public class TranslationService extends BaseService<TranslationDao, Translation> implements ITranslationService {
    @Autowired
    private CommonProperties commonProperties;

    public List<Translation> getActiveList() {
        return list(qw().lambda().eq(Translation::getEnabled, true).orderByAsc(Translation::getSort));
    }

    @Override
    public List<Translation> selectList(String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, Long limit, List<String> selects, List<String> selectMores, List<String> selectLess, List<OrderItem> orderInfos) {
        // 先查询当前语言
//        switch (commonProperties.getApplication()) {
//            case user:
        Object lang = paramMap.get("lang");
        if (lang == null) {
            Locale locale = LocaleContextHolder.getLocale();
            lang = locale.toLanguageTag();
        }

        paramMap.put("lang", "#eq#" + lang);
//        }
        List<Translation> translations = super.selectList(entityClassName, paramMap, timeQuery, limit, selects, selectMores, selectLess, orderInfos);
        if (translations.isEmpty()) {
            // 查不到再统一查询英语
            paramMap.put("lang", "#eq#" + CommonConst.Str.DEFAULT_LANG_EN);
            translations = super.selectList(entityClassName, paramMap, timeQuery, limit, selects, selectMores, selectLess, orderInfos);
        }
        return translations;
    }
}
