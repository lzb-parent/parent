package com.pro.common.modules.service.dependencies.modelauth.base;

import cn.hutool.core.collection.CollUtil;
import com.pro.common.modules.api.dependencies.auth.IAgentUserFilterService;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.model.classes.IOpenConfigClass;
import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.page.IPageInput;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.enums.EnumCommonDataMethodType;
import com.pro.framework.api.model.IModel;
import com.pro.framework.api.util.AssertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 外层数据过滤服务
 */
@Service
//@AllArgsConstructor
public class CommonDataAuthService<T extends IModel> {
    @Autowired
    private AuthServiceFactory authServiceFactory;
    @Autowired(required = false)
    private IAgentUserFilterService commonDataAuthFilterService;
    @Autowired
    private IEntityProperties entityProperties;

//    /**
//     * 执行前 过滤数据来源
//     */
//    public void filterRequestModify(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo) {
//        this.filterRequest(methodType, beanClass, loginInfo);
//    }

    /**
     * 执行前 过滤数据来源
     */
    public void filterRequestQuery(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, Map<String, Object> paramMap, UserDataQuery query, IPageInput pageInput) {
        this.filterRequest(methodType, beanClass, loginInfo, () -> this.filterRequestQueryMore(methodType, beanClass, loginInfo, paramMap, query, pageInput));
    }

    public void filterRequestQueryMore(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, Map<String, Object> paramMap, UserDataQuery query, IPageInput pageInput) {
        // 需要登录的请求,都登录了
        AssertUtil.isTrue(IOpenConfigClass.class.isAssignableFrom(beanClass) || null != loginInfo.getId(), "暂无权限");
        switch (loginInfo.getSysRole()) {
            case ADMIN:
                if (commonDataAuthFilterService != null) {
                    commonDataAuthFilterService.filterAgentQuery(paramMap, null, query);
                }
                break;
            case AGENT:
                if (commonDataAuthFilterService != null) {
                    commonDataAuthFilterService.filterAgentQuery(paramMap, loginInfo.getId(), query);
                }
                break;
            case USER:
                String userIdPropName = entityProperties.getEntityClassReplaceMap().get("User").equals(beanClass) ? "id" : "userId";
                commonDataAuthFilterService.filterUserTeamQuery(loginInfo, paramMap, query, userIdPropName, beanClass);
                break;
            case ANONYMOUS:
            default:
                throw new BusinessException("暂无权限");
        }

        switch (loginInfo.getSysRole()) {
            case ADMIN:
                break;
            default:
                // 只显示开启的配置信息
                if (!IUserClass.class.isAssignableFrom(beanClass)) {
                    paramMap.put("enabled", true);
                }
                break;
        }
        /**
         *
         1.先判断 参数有没有权限
         选中用户父亲用户s
         选中用户父亲代理s
         选中用户子用户s
         选中用户子代理s

         2.获取userIds 参数对应userIds ||   自己/自己直接代理/所有userIds 加入过滤

         3.如果是新增 有userId的对象必填userId,  用户自己的/

         4.如果是修改,删除 userId 要在 userIds区间中
         */
        switch (methodType) {
            // id查询其他过滤无效 只能查询出来再过滤
            case insert:
            case insertOrUpdate:
            case update:
            case delete:
//               filterRequest
                // 检查userId
        }
        // 用户端,配置类,默认正序
        if (IOpenConfigClass.class.isAssignableFrom(beanClass)) {
            if (null != pageInput && CollUtil.isEmpty(pageInput.getOrders())) {
                pageInput.setOrders(Arrays.asList(new OrderItem("sort", true), new OrderItem("id", true)));
            }
        }
    }


    public <T extends IModel> void filterRequest(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo) {
        filterRequest(methodType, beanClass, loginInfo, () -> {
        });
    }

    public <T extends IModel> void filterRequest(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, Runnable filterMore) {
        switch (methodType) {
            case selectById:
            case selectOne:
            case selectLists:
            case selectList:
            case selectCountSum:
            case selectPage:
                // 查询公共资料不用登录
                if (!IOpenConfigClass.class.isAssignableFrom(beanClass)) {
                    AssertUtil.notEmpty(loginInfo.getId(), "Please login first");
                    filterMore.run();
                }
                break;
            // 暂时只需要登录 先查询数据,才能检查
//            case insert:
//            case update:
//            case delete:
//            case insertOrUpdate:
//            AssertUtil.notEmpty(loginInfo.getId(), "Please login first");
//                break;
            default:
                // 其他都要登录
                AssertUtil.notEmpty(loginInfo.getId(), "Please login first");
                filterMore.run();
        }
    }

    /**
     * (执行后) 过滤实体数据结果
     */
    public T filterEntity(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, T record) {
        List<T> list = this.filterEntity(methodType, beanClass, loginInfo, Collections.singletonList(record), record);
        return list.isEmpty() ? null : list.get(0);
    }

    public T filterEntity(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, T record, T entityOld) {
        List<T> list = this.filterEntity(methodType, beanClass, loginInfo, Collections.singletonList(record), entityOld);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<T> filterEntity(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, List<T> records) {
        return filterEntity(methodType, beanClass, loginInfo, records, null);
    }

    public List<T> filterEntity(EnumCommonDataMethodType methodType, Class<T> beanClass, @Parameter(hidden = true) ILoginInfo loginInfo, List<T> records, T entityOld) {
        AuthService<T> authService = (AuthService<T>) authServiceFactory.getService(beanClass.getSimpleName());
        AssertUtil.notEmpty(authService, "暂无权限");

        if (records.isEmpty()) {
            return records;
        }
        T newEntity = records.get(0);
        switch (methodType) {
            // id查询其他过滤无效 只能查询出来再过滤
            case insert:
                if (newEntity instanceof IUserClass) {
                    switch (loginInfo.getSysRole()) {
                        case USER:
                            records.forEach(record -> ((IUserClass) record).setUserId(loginInfo.getId()));
                            break;
                        case AGENT:
//                            Set<Long> userIds = new HashSet<>();
                            if (commonDataAuthFilterService != null) {
                                commonDataAuthFilterService.filterAgentInsertUpdate(loginInfo, records);
                            }
                            break;
                    }
                }
                break;
            // id查询其他过滤无效 只能查询出来再过滤
            case delete:
            case update:
                AssertUtil.notEmpty(entityOld, "数据不存在");
                if (newEntity instanceof IUserClass) {
                    switch (loginInfo.getSysRole()) {
                        case USER:
                            AssertUtil.isTrue(loginInfo.getId().equals(((IUserClass) entityOld).getUserId()), "暂无权限");
//                            records.forEach(record -> AssertUtil.isTrue(loginInfo.getId().equals(((IUserClass) record).getUserId()), "暂无权限"));
                            break;
                        case AGENT:
//                            Set<Long> userIds = new HashSet<>();
                            if (commonDataAuthFilterService != null) {
                                commonDataAuthFilterService.filterAgentInsertUpdate(loginInfo, records);
                            }
                            break;
                    }
                }
                break;
        }

        return authService.fillNull(entityOld, records, loginInfo.getSysRole(), methodType.getType());
    }
}
