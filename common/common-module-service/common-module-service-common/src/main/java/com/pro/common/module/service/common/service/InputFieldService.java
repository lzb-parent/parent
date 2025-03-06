package com.pro.common.module.service.common.service;

import com.pro.common.module.service.common.dao.InputFieldDao;
import com.pro.common.module.api.common.model.db.InputField;
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
public class InputFieldService extends BaseService<InputFieldDao, InputField> {

    @Override
    public boolean updateById(InputField entity) {
        entity.setLabel(null);// 翻译统一,暂时不可编辑
        return super.updateById(entity);
    }


    @Override
    public List<InputField> selectList(String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, Long limit, List<String> selects, List<String> selectMores, List<String> selectLess, List<OrderItem> orderInfos) {
        List<InputField> authRoutes = super.selectList(entityClassName, paramMap, timeQuery, limit, selects, selectMores, selectLess, orderInfos);
        authRoutes.forEach(authRoute -> authRoute.setLabel(I18nUtils.get(authRoute.getLabel())));
        return authRoutes;
    }

    @Override
    public IMultiPageResult<InputField> selectPage(String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery) {
        IMultiPageResult<InputField> page = super.selectPage(entityClassName, pageInput, paramMap, timeQuery);
        page.getRecords().forEach(authRoute -> authRoute.setLabel(I18nUtils.get(authRoute.getLabel())));
        return page;
    }
}
