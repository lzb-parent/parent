package com.pro.common.module.service.usermoney.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.module.service.usermoney.job.AmountEntityCommonClearJob;
import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntityRecord;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;
import com.pro.common.module.service.usermoney.service.AmountEntityUnitService;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.auth.ICommonDataAuthFilterService;
import com.pro.common.modules.api.dependencies.enums.EnumEnv;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.classes.IUserClass;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.SpringContextUtils;
import com.pro.framework.mybatisplus.wrapper.MyWrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "数额变化")
//@RestController
//@RequestMapping("/amountEntity")
public abstract class AmountEntityBaseController<Entity extends AmountEntity, Record extends AmountEntityRecord, DTO extends IAmountEntityRecord, UnitService extends AmountEntityUnitService<Entity, Record, DTO>> {

    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ICommonDataAuthFilterService commonDataAuthFilterService;

    @ApiOperation(value = "变动记录")
    @RequestMapping("/getInfo")
    public R<Entity> getInfo(ILoginInfo loginInfo, Entity params) {
        // 获取用户id过滤
        List<Entity> entities = getAmountEntityService().list(getFilterUserId(MyWrappers.lambdaQuery(params), loginInfo));
        if (entities.isEmpty()) {
            return R.ok(null);
        }
        if (entities.size() > 1) {
            throw new BusinessException("find " + entities.size() + " types of amount");
        }
        return R.ok(entities.get(0));
    }


    @ApiOperation(value = "变动记录")
    @RequestMapping("/getInfos")
    public R<List<Entity>> getInfos(ILoginInfo loginInfo, Entity params) {
        // 获取用户id过滤
        List<Entity> entities = getAmountEntityService().list(getFilterUserId(MyWrappers.lambdaQuery(params), loginInfo));
        return R.ok(entities);
    }


    @ApiOperation(value = "变动记录")
    @RequestMapping("/getRecordList")
    public R<IPage<Record>> getRecordList(ILoginInfo loginInfo, Page<Record> pageInput, Record params) {
        // 获取用户id过滤
        IPage<Record> page = getAmountEntityRecordService().page(pageInput, getFilterUserId(MyWrappers.lambdaQuery(params), loginInfo));
        return R.ok(page);
    }

    /**
     * 测试方法
     */
    @ApiOperation(value = "变动记录")
    @RequestMapping("/change")
    public R<Record> change(ILoginInfo loginInfo, DTO dto, @RequestParam(value = "needSaveRecords", required = false) Boolean needSaveRecords) {
        switch (loginInfo.getSysRole()) {
            case ADMIN:
            case AGENT:
                break;
            default:
                throw new BusinessException("无权限");
        }
        if (needSaveRecords == null) {
            needSaveRecords = true;
        }
        Record record = getAmountEntityUnitService().change(EnumAmountNegativeDeal.throwException, dto, needSaveRecords);
        return R.ok(record);
    }

    /**
     * 测试方法
     */
    @ApiOperation(value = "每日清理")
    @RequestMapping("/clearDay")
    public R<?> clearDay() {
        if (EnumEnv.prod.equals(commonProperties.getEnv())) {
            return R.ok();
        }
        SpringContextUtils.getBean(AmountEntityCommonClearJob.class).clearDay();
        return R.ok();
    }

    protected abstract IService<Entity> getAmountEntityService();

    protected abstract IService<Record> getAmountEntityRecordService();

    protected abstract UnitService getAmountEntityUnitService();


    private <T extends IUserClass> LambdaQueryWrapper<T> getFilterUserId(LambdaQueryWrapper<T> qw, ILoginInfo loginInfo) {
        if (null != loginInfo) {
            switch (loginInfo.getSysRole()) {
                case USER:
                    qw.eq(T::getUserId, loginInfo.getId());
                    break;
                case AGENT:
                    List<Long> userIds = commonDataAuthFilterService.getAgentUserIds(loginInfo.getId(), true, null, null);
                    if (userIds.isEmpty()) {
                        qw.in(T::getUserId, userIds);
                    } else {
                        qw.last("limit 0");
                    }
                    break;
                case ADMIN:
                    break;
                default:
                    throw new BusinessException("无权限");

            }
        }
        return qw;
    }
}
