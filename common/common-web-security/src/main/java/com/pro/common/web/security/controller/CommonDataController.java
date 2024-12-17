package com.pro.common.web.security.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.opencsv.CSVWriter;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.service.dependencies.modelauth.base.ICommonDataService;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.framework.api.clazz.ClassCaches;
import com.pro.framework.api.database.AggregateResult;
import com.pro.framework.api.database.GroupBy;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.PageInput;
import com.pro.framework.api.enums.IEnum;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.model.IdModel;
import com.pro.framework.api.structure.Tuple3;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.javatodb.model.JTDFieldInfoDbVo;
import com.pro.framework.javatodb.model.JTDTableInfoVo;
import com.pro.framework.javatodb.model.UITableInfo;
import com.pro.framework.javatodb.service.IJTDService;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * entityClassName，实体类名称，首字母小写 例如 loginInfo,loginInfoMoney 等
 * 通用接口
 * http://127.0.0.1:8888/commonData/selectPage/loginInfoMoney   分页
 * http://127.0.0.1:8888/commonData/selectList/loginInfoMoney   列表
 * http://127.0.0.1:8888/commonData/selectOne/loginInfoMoney     查询的单个对象
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
@Api(tags = "公共接口")
@RestController
@RequestMapping("/commonData")
@ConditionalOnProperty(name = "common.data.enabled", havingValue = "true")
public class CommonDataController<T extends IModel> {
    @Autowired
    private ICommonDataService<T> commonDataService;

    @Autowired
    private IJTDService jtdService;

    @ApiOperation(value = "通用分页查询")
    @GetMapping("/selectPage/{entityClassNames}")
    public R<IMultiPageResult<T>> selectPage(ILoginInfo loginInfo, @PathVariable String entityClassNames,
                                             PageInput page, @RequestParam Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(commonDataService.selectPage(loginInfo, entityClassNames, page, paramMap, timeQuery, query));
    }

    @ApiOperation(value = "通用统计查询")
    @GetMapping("/selectCountSum/{entityClassName}")
    public R<List<AggregateResult>> selectCountSum(ILoginInfo loginInfo, @PathVariable String entityClassName,
                                                   @RequestParam Map<String, Object> paramMap, TimeQuery timeQuery, GroupBy groupBy, UserDataQuery query) {
        return R.ok(commonDataService.selectCountSum(loginInfo, entityClassName, paramMap, timeQuery, groupBy, query));
    }

    @ApiOperation(value = "通用列表查询")
    @GetMapping("/selectList/{entityClassName}")
    public R<List<T>> selectList(ILoginInfo loginInfo, @PathVariable String entityClassName,
                                 PageInput page, @RequestParam Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(commonDataService.selectList(loginInfo, entityClassName, paramMap, timeQuery, query, null, page));
    }

    @ApiOperation(value = "通用列表查询")
    @GetMapping("/selectLists/{entityClassNames}")
    public R<Map<String, List<T>>> selectLists(ILoginInfo loginInfo, @PathVariable String entityClassNames,
                                               PageInput page, @RequestParam Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(Arrays.stream(entityClassNames.split(",")).collect(Collectors.toMap(c -> c, c -> commonDataService.selectList(loginInfo, c, paramMap, timeQuery, query, null, page))));
    }

    @ApiOperation(value = "查询单个信息")
    @GetMapping("/selectOne/{entityClassName}")
    public R<T> selectOne(ILoginInfo loginInfo, @PathVariable String entityClassName, PageInput page,
                          @RequestParam Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(commonDataService.selectOne(loginInfo, entityClassName, page, paramMap, timeQuery, query));
    }

    @ApiOperation(value = "查询单个信息")
    @GetMapping("/selectById/{entityClassName}/{id}")
    public R<T> selectById(ILoginInfo loginInfo, @PathVariable String entityClassName, @PathVariable Long id, UserDataQuery query) {
        return R.ok(commonDataService.selectById(loginInfo, entityClassName, id, query));
    }

    //
    @ApiOperation(value = "保存或更新信息")
    @PostMapping("/insertOrUpdate/{entityClassName}")
    public R<T> insertOrUpdate(ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody String body) {
        return R.ok(commonDataService.insertOrUpdate(loginInfo, entityClassName, body));
    }

    @ApiOperation(value = "保存信息")
    @PostMapping("/insert/{entityClassName}")
    public R<T> insert(ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody String body) {
        return R.ok(commonDataService.insert(loginInfo, entityClassName, body));
    }

    @ApiOperation(value = "更新信息")
    @PostMapping("/update/{entityClassName}")
    public R<Boolean> update(ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody String body) {
        return R.ok(commonDataService.update(loginInfo, entityClassName, body));
    }

    @ApiOperation(value = "删除信息")
    @PostMapping("/delete/{entityClassName}")
    public R<Boolean> delete(ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody IdModel model) {
        return R.ok(commonDataService.delete(loginInfo, entityClassName, model.getId()));
    }

    @ApiOperation(value = "通用列表查询")
    @GetMapping("/export/{entityClassName}")
    @SneakyThrows
    public void export(HttpServletResponse response,
                       @RequestParam(value = "fields", required = false) List<UITableInfo.FieldConfigOne> fields,
                       @RequestParam(value = "fileName", required = false) String fileName,
                       ILoginInfo loginInfo,
                       @PathVariable String entityClassName,
                       PageInput page,
                       @RequestParam Map<String, Object> paramMap,
                       TimeQuery timeQuery,
                       UserDataQuery query) {
        Class<T> beanClass = commonDataService.getBeanClass(entityClassName);
        Map<String, Tuple3<Field, Method, Method>> classMetaMap = ClassCaches.computeIfAbsentClassFieldMapFull(beanClass);
        if (fields == null) {
            JTDTableInfoVo tableInfo = jtdService.readTableInfo(MultiClassRelationFactory.INSTANCE.getEntityClass(StrUtils.firstToLowerCase(beanClass.getSimpleName())));
            // 驼峰属性名
            tableInfo.getFields().forEach(field -> field.setFieldName(StrUtil.toCamelCase(field.getFieldName())));
            fields = tableInfo.getFields().stream()
                    .filter(f -> {
                        Tuple3<Field, Method, Method> classMeta = classMetaMap.get(f.getFieldName());
                        return !Long.class.equals(classMeta.getT1().getType());
                    }).map(f -> {
                        UITableInfo.FieldConfigOne fieldConfigOne = new UITableInfo.FieldConfigOne();
                        fieldConfigOne.setFieldName(f.getFieldName());
                        fieldConfigOne.setLabel(f.getLabel());
                        return fieldConfigOne;
                    }).collect(Collectors.toList());
        }
        if (fileName == null) {
            fileName = entityClassName + "_" + DateUtil.now() + ".csv";
        }
        List<String> selectFieldNames = fields.stream().map(JTDFieldInfoDbVo::getFieldName).collect(Collectors.toList());
        selectFieldNames.add(0, "id");
        List<T> list = commonDataService.selectList(loginInfo, entityClassName, paramMap, timeQuery, query, selectFieldNames, page);

        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8"); // 设置字符编码为 UTF-8
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

        List<UITableInfo.FieldConfigOne> finalFields = fields;

        List<String[]> rows = new ArrayList<>(list.size() + 1);
        // 写入表头
        rows.add(finalFields.stream().map(JTDFieldInfoDbVo::getLabel).toArray(String[]::new));
        for (T data : list) {
            rows.add(convertDataToCsvRow(data, finalFields, classMetaMap));
        }
        CSVWriter writer = new CSVWriter(response.getWriter());
        writer.writeAll(rows);
    }

    private static <T extends IModel> String[] convertDataToCsvRow(T data, List<UITableInfo.FieldConfigOne> fields, Map<String, Tuple3<Field, Method, Method>> classFieldMap) {
        return fields.stream().map(f -> invokeAndToString(data, classFieldMap.get(f.getFieldName()).getT3())).toArray(String[]::new);
    }

    @SneakyThrows
    private static <T extends IModel> String invokeAndToString(T data, Method getMethod) {
        Object val = getMethod.invoke(data);
        if (val == null) {
            return null;
        } else {
            return valueRead(val);
        }
    }


    private static String valueRead(Object o) {
        Class<?> aClass = o.getClass();
        if (Boolean.class.isAssignableFrom(aClass)) {
            return ((Boolean) o) ? "是" : "否";
        } else if (IEnum.class.isAssignableFrom(aClass)) {
            return ((IEnum) o).getLabel();
        }
        return o.toString();
    }

}
