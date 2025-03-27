package com.pro.common.web.security.service;

import cn.hutool.core.bean.BeanUtil;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.service.dependencies.modelauth.base.ICommonDataService;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.database.AggregateResult;
import com.pro.framework.api.database.GroupBy;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.database.page.PageInput;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.enums.EnumCommonDataMethodType;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import com.pro.framework.mybatisplus.CRUDService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pro.framework.api.enums.EnumCommonDataMethodType.*;

/**
 * 数据权限
 *
 * @param <T>
 */
@Service
@Getter
public class CommonDataService<T extends IModel> implements ICommonDataService<T> {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private CRUDService<T> crudService;
    @Autowired
    private CommonDataAuthService<T> commonDataAuthService;
    @Autowired
    private IEntityProperties entityProperties;

    @Override
    public IMultiPageResult<T> selectPage(ILoginInfo loginInfo, String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectPage, beanClass, loginInfo, paramMap, query, pageInput);

        IMultiPageResult<T> page = crudService.selectPage(entityClassName, pageInput, paramMap, timeQuery);

        // 执行后 过滤数据结果
        page.setRecords(commonDataAuthService.filterEntity(selectPage, beanClass, loginInfo, page.getRecords()));
        return page;
    }


    @Override
    public List<AggregateResult> selectCountSum(ILoginInfo loginInfo, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, GroupBy groupBy, UserDataQuery query) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectCountSum, beanClass, loginInfo, paramMap, query, null);

        List<AggregateResult> results = crudService.selectCountSum(entityClassName, paramMap, timeQuery, groupBy);

        for (AggregateResult result : results) {
            // 执行后 过滤数据结果
            Stream.of("props", "avg", "countDistinct", "max", "min", "groupConcat").forEach(funPropName -> this.filterEntitySelectCountSum(entityClassName, beanClass, loginInfo, result, funPropName));
        }
        return results;
    }

    @Override
    public List<T> selectList(ILoginInfo loginInfo, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query, List<String> selects, PageInput pageInput) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectList, beanClass, loginInfo, paramMap, query, pageInput);

        List<T> list = crudService.selectList(entityClassName, paramMap, timeQuery, null, null, null, null, pageInput.getOrders());

        // 执行后 过滤数据结果
        return commonDataAuthService.filterEntity(selectList, beanClass, loginInfo, list);
    }

    @Override
    public T selectOne(ILoginInfo loginInfo, String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectOne, beanClass, loginInfo, paramMap, query, pageInput);

        T entity = crudService.selectOne(entityClassName, pageInput, paramMap, timeQuery);

        // 执行后 过滤数据结果
        return commonDataAuthService.filterEntity(selectOne, beanClass, loginInfo, entity);
    }


    @Override
    public T selectById(ILoginInfo loginInfo, String entityClassName, Long id, UserDataQuery query) {

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("id", id);

        // 执行前 过滤数据来源
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequestQuery(selectById, beanClass, loginInfo, paramMap, query, null);

        T entity = crudService.selectOne(entityClassName, null, paramMap, null);

        // 执行后 过滤数据结果
        return commonDataAuthService.filterEntity(selectById, beanClass, loginInfo, entity);
    }

    //
    @Override
    public T insertOrUpdate(ILoginInfo loginInfo, String entityClassName, String body) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);
        // 过滤实体
        T entityNew = this.filterEntityInsertUpdate(null, beanClass, loginInfo, entityClassName, body);
        crudService.insertOrUpdate(entityClassName, entityNew);
        return entityNew;
    }


    @Override
    public T insert(ILoginInfo loginInfo, String entityClassName, String body) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);
        // 过滤实体
        T entityNew = this.filterEntityInsertUpdate(insert, beanClass, loginInfo, entityClassName, body);
        crudService.insert(entityClassName, entityNew);
        return entityNew;
    }

    @Override
    public Boolean update(ILoginInfo loginInfo, String entityClassName, String body) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);
        // 过滤实体
        T entityNew = this.filterEntityInsertUpdate(update, beanClass, loginInfo, entityClassName, body);
        crudService.updateById(entityClassName, entityNew);
        return true;
    }

    @Override
    public Boolean delete(ILoginInfo loginInfo, String entityClassName, Long id) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);

        T entity = crudService.selectOneById(entityClassName, id);
        // 过滤实体
        commonDataAuthService.filterEntity(delete, beanClass, loginInfo, entity);

        crudService.delete(entityClassName, id);
        return true;
    }

    /**
     * 检查userId 过滤属性
     */
    private T filterEntityInsertUpdate(EnumCommonDataMethodType methodInput, Class<T> beanClass, ILoginInfo loginInfo, String entityClassName, String body) {
        T entityNew = JSONUtils.fromString(body, getBeanClass(entityClassName));
        T entityOld = null;
        EnumCommonDataMethodType methodById;
        Serializable id = entityNew.getId();
        // 新增
        if (id == null) {
            methodById = insert;
            AssertUtil.notEmpty(entityNew, "输入数据为空");
            entityOld = entityNew;
        }
        // 修改
        else {
            methodById = update;
            entityOld = crudService.selectOneById(entityClassName, id);
            AssertUtil.notEmpty(entityOld, "数据不存在");
        }
        if (methodInput != null) {
            AssertUtil.isTrue(methodById.equals(methodInput), "methodById=" + methodById + " and methodInput=" + methodInput + " is not consistent");
        }
        return commonDataAuthService.filterEntity(methodById, beanClass, loginInfo, entityNew, entityOld);
    }

    private void filterEntitySelectCountSum(
            String entityClassName, Class<T> beanClass,
            ILoginInfo loginInfo, AggregateResult result,
            String funPropName
    ) {
        HashMap<String, ?> map = BeanUtil.getProperty(result, funPropName);
        Map<String, ?> data = map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().replace(entityClassName + ".", ""), Map.Entry::getValue));
        //noinspection deprecation
        T entity = BeanUtil.mapToBean(data, beanClass, true);
        BeanUtil.setProperty(result, funPropName, BeanUtil.beanToMap(commonDataAuthService.filterEntity(selectCountSum, beanClass, loginInfo, entity)));
    }


    public Class<T> getBeanClass(String entityClassName) {
        //noinspection unchecked
        Class<T> tClass = (Class<T>) entityProperties.getEntityClassReplaceMap().computeIfAbsent(StrUtils.firstToUpperCase(entityClassName), c -> MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName));
        AssertUtil.notEmpty(tClass, "entity not exist: " + entityClassName);
        return tClass;
    }
}
