package com.pro.common.web.security.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.common.modules.api.dependencies.model.classes.ISimpleInfo;
import com.pro.common.modules.service.dependencies.modelauth.base.ICommonDataService;
import com.pro.common.modules.service.dependencies.modelauth.base.model.ExportField;
import com.pro.framework.api.clazz.ClassCaches;
import com.pro.framework.api.database.AggregateResult;
import com.pro.framework.api.database.GroupBy;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.PageInput;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.model.IdModel;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.mtq.service.multiwrapper.entity.IMultiPageResult;
import com.pro.framework.mybatisplus.CRUDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
@Tag(name = "统一数据接口")
@RestController
@RequestMapping("/commonData")
@ConditionalOnProperty(name = "common.data.enabled", havingValue = "true")
public class CommonDataController<T extends IModel> {
    @Autowired
    private ICommonDataService<T> commonDataService;
    @Autowired
    private CRUDService<T> crudService;

    @ApiOperationSupport(order = 10)
    @Operation(summary = "通用分页查询")
    @GetMapping("/selectPage/{entityClassNames}")
//    @Parameters({
//            @Parameter(name = "token",description = "请求token",required = true,in = ParameterIn.HEADER),
//            @Parameter(name = "file",description = "文件",required = true,in=ParameterIn.DEFAULT,ref = "file"),
//            @Parameter(name = "name",description = "文件名称",required = true),
//    })
    public R<IMultiPageResult<T>> selectPage(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassNames, PageInput page, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(commonDataService.selectPage(loginInfo, entityClassNames, page, paramMap, timeQuery, query));
    }

    @ApiOperationSupport(order = 20)
    @Operation(summary = "通用统计查询")
    @GetMapping("/selectCountSum/{entityClassName}")
    public R<List<AggregateResult>> selectCountSum(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, GroupBy groupBy, UserDataQuery query) {
        return R.ok(commonDataService.selectCountSum(loginInfo, entityClassName, paramMap, timeQuery, groupBy, query));
    }

    @ApiOperationSupport(order = 30)
    @Operation(summary = "通用列表查询")
    @GetMapping("/selectList/{entityClassName}")
    public R<List<T>> selectList(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, PageInput page, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(commonDataService.selectList(loginInfo, entityClassName, paramMap, timeQuery, query, null, page));
    }

    @ApiOperationSupport(order = 40)
    @Operation(summary = "通用列表查询-多表")
    @GetMapping("/selectLists/{entityClassNames}")
    public R<Map<String, List<T>>> selectLists(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassNames, PageInput page, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(Arrays.stream(entityClassNames.split(",")).collect(Collectors.toMap(c -> c, c -> commonDataService.selectList(loginInfo, c, paramMap, timeQuery, query, null, page))));
    }

    @ApiOperationSupport(order = 50)
    @Operation(summary = "查询单个信息")
    @GetMapping("/selectById/{entityClassName}/{id}")
    public R<T> selectById(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, @PathVariable Long id, UserDataQuery query) {
        return R.ok(commonDataService.selectById(loginInfo, entityClassName, id, query));
    }

    @ApiOperationSupport(order = 60)
    @Operation(summary = "查询单个信息")
    @GetMapping("/selectOne/{entityClassName}")
    public R<T> selectOne(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, PageInput page, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(commonDataService.selectOne(loginInfo, entityClassName, page, paramMap, timeQuery, query));
    }

    @ApiOperationSupport(order = 70)
    @Operation(summary = "新增或修改信息")
    @PostMapping("/insertOrUpdate/{entityClassName}")
    public R<T> insertOrUpdate(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody String body) {
        return R.ok(commonDataService.insertOrUpdate(loginInfo, entityClassName, body));
    }

    @ApiOperationSupport(order = 80)
    @Operation(summary = "新增信息")
    @PostMapping("/insert/{entityClassName}")
    public R<T> insert(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody String body) {
        return R.ok(commonDataService.insert(loginInfo, entityClassName, body));
    }

    @ApiOperationSupport(order = 90)
    @Operation(summary = "修改信息")
    @PostMapping("/update/{entityClassName}")
    public R<Boolean> update(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody String body) {
        return R.ok(commonDataService.update(loginInfo, entityClassName, body));
    }

    @ApiOperationSupport(order = 100)
    @Operation(summary = "删除信息")
    @PostMapping("/delete/{entityClassName}")
    public R<Boolean> delete(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, @RequestBody IdModel model) {
        return R.ok(commonDataService.delete(loginInfo, entityClassName, model.getId()));
    }

    @ApiOperationSupport(order = 110)
    @Operation(summary = "通用导出csv文件流")
    @GetMapping("/export/{entityClassName}")
    @SneakyThrows
    public void export(HttpServletResponse response, @RequestParam(value = "fields", required = false) List<ExportField> fields, @RequestParam(value = "fileName", required = false) String fileName, @Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable String entityClassName, PageInput page, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        response.setContentType("text/csv");
        response.setCharacterEncoding("UTF-8"); // 设置字符编码为 UTF-8
        if (fileName == null) {
            fileName = entityClassName + "_" + DateUtil.now() + ".csv";
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        PrintWriter responseWriter = response.getWriter();
        commonDataService.export(fields, loginInfo, entityClassName, page, paramMap, timeQuery, query, responseWriter);
    }


    @ApiOperationSupport(order = 120)
    @Operation(summary = "通用少量公开信息列表查询")
    @GetMapping("/selectListsSimple/{entityClassNames}")
    public R<Map<String, List<?>>> selectListsSimple(@PathVariable String entityClassNames, PageInput pageInput, @RequestParam(required = false) Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(Arrays.stream(entityClassNames.split(",")).collect(Collectors.toMap(c -> c, c -> this.selectListsSimpleOne(pageInput, c, paramMap, timeQuery, query))));
    }

    private static final List<String> SIMPLE_PROPS = Arrays.asList("id", "code", "name", "username", "label", "langCode", "cname", "enabled", "sort", "sysRole");

    private List<?> selectListsSimpleOne(PageInput pageInput, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        Class<?> beanClass = commonDataService.getBeanClass(entityClassName);
        AssertUtil.isTrue(IOpenConfigClass.class.isAssignableFrom(beanClass) || ISimpleInfo.class.isAssignableFrom(beanClass), "暂无权限");
        // 用户端,配置类,默认正序
        if (IOpenConfigClass.class.isAssignableFrom(beanClass)) {
            if (CollUtil.isEmpty(pageInput.getOrders())) {
                List<OrderItem> orders = Arrays.asList(new OrderItem("sort", true), new OrderItem("id", true));
                pageInput.setOrders(orders);
            }
        }
        return crudService.selectList(entityClassName, paramMap, timeQuery, null, new ArrayList<>(CollUtil.intersection(ClassCaches.getFieldNamesByClass(beanClass), SIMPLE_PROPS)), null, null, pageInput.getOrders());
    }
}
