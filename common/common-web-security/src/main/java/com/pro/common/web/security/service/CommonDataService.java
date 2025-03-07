package com.pro.common.web.security.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.opencsv.CSVWriter;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.service.dependencies.modelauth.base.CommonDataAuthService;
import com.pro.common.modules.service.dependencies.modelauth.base.ICommonDataService;
import com.pro.common.modules.service.dependencies.modelauth.base.model.ExportField;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.web.security.model.dto.ExportConfigData;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.clazz.ClassCaches;
import com.pro.framework.api.database.AggregateResult;
import com.pro.framework.api.database.GroupBy;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.database.page.PageInput;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.enums.EnumCommonDataMethodType;
import com.pro.framework.api.enums.IEnum;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.structure.Tuple3;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.javatodb.model.JTDFieldInfoDbVo;
import com.pro.framework.javatodb.model.JTDTableInfoVo;
import com.pro.framework.javatodb.service.IJTDService;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import com.pro.framework.mybatisplus.CRUDService;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    @Autowired
    private IJTDService jtdService;

    @Override
    public IMultiPageResult<T> selectPage(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectPage, beanClass, loginInfo, paramMap, query, pageInput);

        IMultiPageResult<T> page = crudService.selectPage(entityClassName, pageInput, paramMap, timeQuery);

        // 执行后 过滤数据结果
        page.setRecords(commonDataAuthService.filterEntity(selectPage, beanClass, loginInfo, page.getRecords()));
        return page;
    }


    @Override
    public List<AggregateResult> selectCountSum(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, GroupBy groupBy, UserDataQuery query) {
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
    public List<T> selectList(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query, List<String> selects, PageInput pageInput) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectList, beanClass, loginInfo, paramMap, query, pageInput);

        List<T> list = crudService.selectList(entityClassName, paramMap, timeQuery, null, null, null, null, pageInput.getOrders());

        // 执行后 过滤数据结果
        return commonDataAuthService.filterEntity(selectList, beanClass, loginInfo, list);
    }

    @Override
    public T selectOne(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        Class<T> beanClass = getBeanClass(entityClassName);

        // 执行前 过滤数据来源
        commonDataAuthService.filterRequestQuery(selectOne, beanClass, loginInfo, paramMap, query, pageInput);

        T entity = crudService.selectOne(entityClassName, pageInput, paramMap, timeQuery);

        // 执行后 过滤数据结果
        return commonDataAuthService.filterEntity(selectOne, beanClass, loginInfo, entity);
    }


    @Override
    public T selectById(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Long id, UserDataQuery query) {

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
    public T insertOrUpdate(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);
        // 过滤实体
        T entityNew = this.filterEntityInsertUpdatePrepare(null, beanClass, loginInfo, entityClassName, body);
        crudService.insertOrUpdate(entityClassName, entityNew);
        return entityNew;
    }


    @Override
    public T insert(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);
        // 过滤实体
        T entityNew = this.filterEntityInsertUpdatePrepare(insert, beanClass, loginInfo, entityClassName, body);
        crudService.insert(entityClassName, entityNew);
        return entityNew;
    }

    @Override
    public Boolean update(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);
        // 过滤实体
        T entityNew = this.filterEntityInsertUpdatePrepare(update, beanClass, loginInfo, entityClassName, body);
        crudService.updateById(entityClassName, entityNew);
        return true;
    }

    @Override
    public Boolean delete(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Long id) {
        Class<T> beanClass = getBeanClass(entityClassName);
        commonDataAuthService.filterRequest(selectById, beanClass, loginInfo);

        T entity = crudService.selectOneById(entityClassName, id);
        // 过滤实体
        commonDataAuthService.filterEntity(delete, beanClass, loginInfo, entity);
        crudService.delete(entityClassName, id);
        return true;
    }

    @Override
    public void export(List<ExportField> fields, @Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, PageInput page, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query, PrintWriter responseWriter) {
        Class<T> beanClass = getBeanClass(entityClassName);
        Map<String, Tuple3<Field, Method, Method>> classMetaMap = ClassCaches.computeIfAbsentClassFieldMapFull(
                beanClass);
        if (fields == null) {
            JTDTableInfoVo tableInfo = jtdService.readTableInfo(MultiClassRelationFactory.INSTANCE.getEntityClass(
                    StrUtils.firstToLowerCase(beanClass.getSimpleName())));
            // 驼峰属性名
            List<JTDFieldInfoDbVo> fieldCfgs = tableInfo.getFields();
            fieldCfgs.forEach(field -> field.setFieldName(StrUtil.toCamelCase(field.getFieldName())));
            fields = fieldCfgs.stream()
                    .filter(f -> {
                        Tuple3<Field, Method, Method> classMeta = classMetaMap.get(f.getFieldName());
                        return !Long.class.equals(classMeta.getT1().getType());
                    }).map(f -> {
                        ExportField fieldConfigOne = new ExportField();
                        fieldConfigOne.setFieldName(f.getFieldName());
                        fieldConfigOne.setLabel(f.getLabel());
                        return fieldConfigOne;
                    }).collect(Collectors.toList());
        }
        List<String> selectFieldNames = fields.stream()
                .map(ExportField::getFieldName)
                .collect(Collectors.toList());
        selectFieldNames.add(0, "id");
        List<T> list = this.selectList(loginInfo, entityClassName, paramMap, timeQuery, query,
                selectFieldNames, page);


        ExportConfigData exportConfigData = new ExportConfigData();
        exportConfigData.setBoolTrueValue(translate(FrameworkConst.Str.TRUE_TRANSLATE_KEY));
        exportConfigData.setBoolFalseValue(translate(FrameworkConst.Str.FALSE_TRANSLATE_KEY));
        List<ExportField> finalFields = fields;

        List<String[]> rows = new ArrayList<>(list.size() + 1);
        // 写入表头
        rows.add(finalFields.stream()
                .map(ExportField::getLabel)
                .map(CommonDataService::translate)
                .toArray(String[]::new));
        for (T data : list) {
            rows.add(convertDataToCsvRow(data, finalFields, classMetaMap, exportConfigData));
        }
        CSVWriter writer = new CSVWriter(responseWriter);
        writer.writeAll(rows);
    }

    private static String translate(String label) {
        return StrUtils.or(I18nUtils.get(StrUtils.replaceSpecialToUnderline(label)), label);
    }

    private static <T extends IModel> String[] convertDataToCsvRow(T data, List<ExportField> fields, Map<String, Tuple3<Field, Method, Method>> classFieldMap, ExportConfigData exportConfigData) {
        return fields.stream()
                .map(f -> invokeAndToString(data, classFieldMap.get(f.getFieldName()).getT3(), exportConfigData))
                .toArray(String[]::new);
    }

    @SneakyThrows
    private static <T extends IModel> String invokeAndToString(T data, Method getMethod, ExportConfigData exportConfigData) {
        Object val = getMethod.invoke(data);
        if (val == null) {
            return null;
        } else {
            return valueRead(val, exportConfigData);
        }
    }


    private static String valueRead(Object o, ExportConfigData exportConfigData) {
        Class<?> aClass = o.getClass();
        if (Boolean.class.isAssignableFrom(aClass)) {
            return ((Boolean) o) ? exportConfigData.getBoolTrueValue() : exportConfigData.getBoolFalseValue();
        } else if (IEnum.class.isAssignableFrom(aClass)) {
            return ((IEnum) o).getLabel();
        } else if (LocalDateTime.class.isAssignableFrom(aClass)) {
            return ((LocalDateTime) o).format(FrameworkConst.DateTimes.DATE_TIME_FORMAT);
        }
        return o.toString();
    }


    /**
     * 检查userId 过滤属性
     */
    private T filterEntityInsertUpdatePrepare(EnumCommonDataMethodType methodInput, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body) {
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
            @Parameter(hidden = true) ILoginInfo loginInfo, AggregateResult result,
            String funPropName
    ) {
        HashMap<String, ?> map = BeanUtil.getProperty(result, funPropName);
        Map<String, ?> data = map.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().replace(entityClassName + ".", ""), Map.Entry::getValue));
        //noinspection deprecation
        T entity = BeanUtil.mapToBean(data, beanClass, true);
        BeanUtil.setProperty(result, funPropName, BeanUtil.beanToMap(commonDataAuthService.filterEntity(selectCountSum, beanClass, loginInfo, entity)));
    }

    @Override
    public Class<T> getBeanClass(String entityClassName) {
        Class<T> tClass = MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName);
        AssertUtil.notEmpty(tClass, "entity not exist: " + entityClassName);
        return tClass;
    }

}
