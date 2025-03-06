package com.pro.common.module.service.common.service;

import com.pro.common.module.service.common.dao.PosterDao;
import com.pro.common.module.api.common.model.db.Poster;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
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
public class PosterService extends BaseService<PosterDao, Poster> {
    @Autowired
    private CommonProperties commonProperties;

    @Override
    public List<Poster> selectList(Map<String, Object> paramMap, TimeQuery timeQuery) {
        if (FrameworkConst.Str.TRUE.equals(paramMap.get("showDetail"))) {
            return super.selectList(paramMap, timeQuery);
        }
        // 默认不查询 content 出来 (数据量太卡卡死)
        return readService.selectList(entityName, paramMap, timeQuery, 2000L, null, null, List.of("content"), null);
    }

    @Override
    public Poster selectOne(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        // 先查询当前语言
        switch (commonProperties.getApplication()) {
            case user:
                Locale locale = LocaleContextHolder.getLocale();
                paramMap.put("lang", "#eq#" + locale.toLanguageTag());
                break;
        }
        Poster poster = super.selectOne(entityClassName, pageInput, paramMap, timeQuery);

        // 查不到再统一查询英语
        switch (commonProperties.getApplication()) {
            case user:
                if (poster == null) {
                    paramMap.put("lang", "#eq#" + CommonConst.Str.DEFAULT_LANG_EN);
                    poster = super.selectOne(entityClassName, pageInput, paramMap, timeQuery);
                }
                break;
        }
        return poster;
    }
}
