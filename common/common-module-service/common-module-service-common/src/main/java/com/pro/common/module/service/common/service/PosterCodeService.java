package com.pro.common.module.service.common.service;

import com.pro.common.module.service.common.dao.PosterCodeDao;
import com.pro.common.module.api.common.model.db.PosterCode;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 国家 服务实现类
 */
@Service
public class PosterCodeService extends BaseService<PosterCodeDao, PosterCode> {

    @Override
    public boolean updateById(PosterCode entity) {
        entity.setName(null);// 翻译统一,暂时不可编辑
        return super.updateById(entity);
    }


    @Override
    public List<PosterCode> selectList(String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, Long limit, List<String> selects, List<String> selectMores, List<String> selectLess, List<OrderItem> orderInfos) {
        List<PosterCode> authRoutes = super.selectList(entityClassName, paramMap, timeQuery, limit, selects, selectMores, selectLess, orderInfos);
        authRoutes.forEach(authRoute -> authRoute.setName(I18nUtils.get(authRoute.getName())));
        return authRoutes;
    }

    @Override
    public IMultiPageResult<PosterCode> selectPage(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        IMultiPageResult<PosterCode> page = super.selectPage(entityClassName, pageInput, paramMap, timeQuery);
        page.getRecords().forEach(authRoute -> authRoute.setName(I18nUtils.get(authRoute.getName())));
        return page;
    }
}
