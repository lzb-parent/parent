package com.pro.common.module.service.usermoney.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.api.usermoney.model.db.UserMoneyWait;
import com.pro.common.module.api.usermoney.model.db.UserMoneyWaitRecord;
import com.pro.common.module.api.usermoney.model.dto.AmountEntityUnitDTO;
import com.pro.common.module.api.usermoney.model.dto.UserMoneyWaitChangeDTO;
import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.enums.EnumUserMoneyWaitState;
import com.pro.common.module.api.usermoney.model.modelbase.AmountEntity;
import com.pro.framework.api.enums.EnumAmountUpDown;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.framework.api.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 数额变化的基础类
 */
@Service
@Slf4j
public class UserMoneyWaitUnitService extends AmountEntityTypeUnitService<UserMoneyWait, UserMoneyWaitRecord, UserMoneyWaitChangeDTO> {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserMoneyWaitService userMoneyWaitService;
    @Autowired
    private UserMoneyWaitRecordService userMoneyWaitRecordService;

    @Override
    public IService<UserMoneyWait> getAmountEntityService() {
        return userMoneyWaitService;
    }

    @Override
    public IService<UserMoneyWaitRecord> getAmountEntityRecordService() {
        return userMoneyWaitRecordService;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<UserMoneyWaitRecord> changeStateByOrders(LocalDateTime settleTimeLimit, UserMoneyWaitRecord params, EnumUserMoneyWaitState state) {
        AssertUtil.isTrue(!EnumUserMoneyWaitState.WAIT.equals(state), "waitRecord cannot change state to [" + EnumUserMoneyWaitState.WAIT + "]");
        boolean orderIdsNotEmpty = CollUtil.isNotEmpty(params.getOrderIds());
        AssertUtil.isTrue(orderIdsNotEmpty || null != params.getOrderId(), "orderId cannot be null");
        LambdaQueryWrapper<UserMoneyWaitRecord> wrapper = userMoneyWaitRecordService.qw()
                .lambda()
                .setEntity(params)
                .in(orderIdsNotEmpty, UserMoneyWaitRecord::getOrderId, params.getOrderIds())
                .eq(null != params.getOrderId(), UserMoneyWaitRecord::getOrderId, params.getOrderId())
                .le(null != settleTimeLimit, UserMoneyWaitRecord::getNextStateTime, settleTimeLimit);
        List<UserMoneyWaitRecord> records = this.getAmountEntityRecordService().list(
                wrapper.orderByAsc(UserMoneyWaitRecord::getId)
//                wrapper.orderByAsc(UserMoneyWaitRecord::getUserId, UserMoneyWaitRecord::getId)
        );
        if (CollUtil.isEmpty(records)) {
            log.warn("cancel order has not UserMoneyWaitRecord");
            return Collections.emptyList();
        }
        List<Long> recordIds = records.stream().map(BaseModel::getId).collect(Collectors.toList());
//        LocalDateTime now = LocalDateTime.now();
        // 乐观锁
        for (Long recordId : recordIds) {
            boolean update = this.getAmountEntityRecordService().lambdaUpdate()
                    .set(UserMoneyWaitRecord::getUpdateTime, settleTimeLimit)
                    .set(UserMoneyWaitRecord::getStateTime, settleTimeLimit)
                    .set(UserMoneyWaitRecord::getState, state)
                    .eq(UserMoneyWaitRecord::getId, recordId)
                    .eq(UserMoneyWaitRecord::getState, EnumUserMoneyWaitState.WAIT)
//                    .in(UserMoneyWaitRecord::getId, recordIds)
                    .update();
            if (!update) {
                log.error("State of the order has changed, please refresh then try|params={},records={}", JSONUtil.toJsonStr(params), JSONUtil.toJsonStr(records));
            }
            AssertUtil.isTrue(update, "State of the order has changed, please refresh then try");
        }
        Map<String, List<UserMoneyWaitRecord>> groupByKey = records.stream().collect(Collectors.groupingBy(UserMoneyWaitRecord::getEntityKey));
        List<UserMoneyWaitChangeDTO> dtosToCancel = groupByKey.entrySet().stream().map(e ->
                {
                    // 原来的金额
                    BigDecimal sumAmountOri = e.getValue().stream().map(d -> EnumAmountUpDown.up.equals(d.getUpDown()) ? d.getAmount() : d.getAmount().negate()).reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
                    boolean isUpOri = sumAmountOri.compareTo(BigDecimal.ZERO) > 0;
                    UserMoneyWaitChangeDTO changeDTO = UserMoneyWaitChangeDTO.builder()
                            //无论完成还是取消,都是反向操作(扣除)
                            .upDown(isUpOri ? EnumAmountUpDown.down : EnumAmountUpDown.up)
                            .amount(sumAmountOri.abs())
                            .build();
                    String entityKey = e.getKey();
                    changeDTO.setEntityKey(entityKey);
                    return changeDTO;
                }
        ).collect(Collectors.toList());
        this.change(
                new AmountEntityUnitDTO<>(
                        EnumAmountNegativeDeal.toNegative,
                        null,
                        dtosToCancel,
                        false
                )
        );
        return null;
    }

//    public void updateStateByTime(LocalDateTime now, EnumUserMoneyWaitState updateState, UserMoneyWaitRecord params) {
//        userMoneyWaitRecordService.lambdaUpdate().setEntity(params)
//                .set(UserMoneyWaitRecord::getState, updateState)
//                .set(UserMoneyWaitRecord::getUpdateTime, now)
//                .set(UserMoneyWaitRecord::getStateTime, now)
//                .le(UserMoneyWaitRecord::getNextStateTime, now)
//                .update();
//    }

    @Override
    protected void fillNewEntityInfos(List<UserMoneyWait> newEntitys) {
        List<Long> userIds = newEntitys.stream().map(AmountEntity::getUserId).collect(Collectors.toList());
        Map<Long, User> userMap = userService.idMap(userIds);
        newEntitys.forEach(e -> {
            User user = userMap.get(e.getUserId());
            e.setIsDemo(user.getIsDemo());
            e.setUsername(user.getUsername());
        });
        super.fillNewEntityInfos(newEntitys);
    }
}
