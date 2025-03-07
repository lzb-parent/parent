package com.pro.common.modules.service.dependencies.modelauth.base;

import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.service.dependencies.modelauth.base.model.ExportField;
import com.pro.framework.api.database.AggregateResult;
import com.pro.framework.api.database.GroupBy;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.database.page.PageInput;
import com.pro.framework.api.model.IModel;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

// 创建接口
// 创建接口
public interface  ICommonDataService<T extends IModel>  {
    IMultiPageResult<T> selectPage(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query);

    List<AggregateResult> selectCountSum(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, GroupBy groupBy, UserDataQuery query);

    List<T> selectList(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query, List<String> selects, PageInput orderInfos);

    T selectOne(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, IPageInput pageInput, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query);

    T selectById(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Long id, UserDataQuery query);

    T insertOrUpdate(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body);

    T insert(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body);

    Boolean update(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, String body);

    Boolean delete(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, Long id);

//    Class<T> getBeanClass(String entityClassName);

    void export(List<ExportField> fields, @Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName, PageInput page, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query, PrintWriter responseWriter);

    Class<T> getBeanClass(String entityClassName);

//    CommonProperties getCommonProperties();
//    IEntityProperties getEnumProperties();

//    default String entityNameFilter(String entityClassName) {
////        switch (entityClassName) {
////            case "user":
////            case "userLevelConfig":
////            case "userMoney":
////                entityClassName = entityClassName + StrUtils.upperFirst(getCommonProperties().getPlatform());
////                break;
////        }
//        return entityClassName;
//    }

//    default Class<T> getBeanClass(String entityClassName) {
//        Class<T> tClass = (Class<T>) getEnumProperties().getEntityClassReplaceMap().computeIfAbsent(entityClassName, c -> MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName));
//        AssertUtil.notEmpty(tClass, "entity not exist: " + entityClassName);
//        return tClass;
//    }
}
