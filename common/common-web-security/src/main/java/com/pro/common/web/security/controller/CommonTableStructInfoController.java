package com.pro.common.web.security.controller;

import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.model.request.JTDTableInfoRequest;
import com.pro.common.web.security.service.CommonTableService;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.javatodb.model.JTDTableInfoVo;
import com.pro.framework.javatodb.model.UITableInfo;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "统一数据结构信息")
@Slf4j
@RestController
@RequestMapping("/commonTable")
@AllArgsConstructor
public class CommonTableStructInfoController {
    private CommonTableService commonTableService;
    private CommonProperties commonProperties;

    @SneakyThrows
    @Operation(summary = "查询完整表格构造信息")
    @GetMapping("/info/{entityClassName}")
    public R<UITableInfo> info(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable(required = false) String entityClassName, JTDTableInfoRequest request) {
        checkPermission(loginInfo, entityClassName);
        return R.ok(commonTableService.getTableStruct(entityClassName, request));
    }

    @SneakyThrows
    @Operation(summary = "查询概要表格信息")
    @GetMapping(value = "/simpleInfo/{entityClassName}")
    public R<JTDTableInfoVo> simpleInfo(@Parameter(hidden = true) ILoginInfo loginInfo, @PathVariable(required = false) String entityClassName) {
        checkPermission(loginInfo, entityClassName);
        return R.ok(commonTableService.simpleInfo(entityClassName));
    }

    private void checkPermission(@Parameter(hidden = true) ILoginInfo loginInfo, String entityClassName) {
        switch (commonProperties.getApplication()) {
            case admin:
            case agent:
            case user:
                if (loginInfo == null || loginInfo.getId() == null || EnumSysRole.ANONYMOUS.equals(loginInfo.getSysRole())) {
                    throw new BusinessException("暂无权限");
                }
                break;
            default:
                throw new BusinessException("暂无权限");
        }
        switch (commonProperties.getApplication()) {
            case agent:
            case user:
                Class<?> beanClass = MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName);
                AssertUtil.isTrue(IUserClass.class.isAssignableFrom(beanClass),"暂无权限");
                break;
            default:
                throw new BusinessException("暂无权限");
        }
    }


}
