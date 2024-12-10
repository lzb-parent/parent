package com.pro.common.module.api.usermoney.service;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.pro.common.module.api.pay.intf.IRechargeSuccessService;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.userlevel.model.db.UserLevelConfig;
import com.pro.common.module.api.userlevel.model.intf.IUserLevelConfigService;
import com.pro.common.module.api.usermoney.model.db.UserAmountTotal;
import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.common.module.api.usermoney.model.db.UserMoneyRecord;
import com.pro.common.module.api.usermoney.model.dto.AmountEntityUnitDTO;
import com.pro.common.module.api.usermoney.model.dto.UserMoneyChangeDTO;
import com.pro.common.module.api.usermoney.model.enums.EnumTradeType;
import com.pro.common.module.api.usermoney.model.intf.IUserMoneyUnitService;
import com.pro.common.module.api.usermoney.model.modelbase.intf.IAmountEntityRecord;
import com.pro.common.modules.api.dependencies.model.BaseModel;
import com.pro.common.modules.api.dependencies.user.model.IUser;
import com.pro.framework.api.util.StrUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数额变化的基础类
 */
//@Service
@Slf4j
@Getter
public abstract class UserMoneyUnitService<Entity extends UserMoney, Record extends UserMoneyRecord, DTO extends IAmountEntityRecord>
        extends AmountEntityTypeUnitService<Entity, Record, DTO> implements IUserMoneyUnitService<Entity, Record, DTO>, IRechargeSuccessService {
    //    @Autowired
//    private IUserService userService;
    @Autowired
    private UserMoneyService amountEntityService;
    @Autowired
    private UserMoneyRecordService amountEntityRecordService;
    @Autowired
    private UserAmountTotalService userAmountTotalService;
    @Autowired
    private IUserLevelConfigService userLevelConfigService;
    @Autowired
    private IUserService userService;

    @Override
    public IService<Entity> getAmountEntityService() {
        return amountEntityService;
    }

    @Override
    public IService<Record> getAmountEntityRecordService() {
        return (IService<Record>) amountEntityRecordService;
    }

    /**
     * 充值成功后,计算佣金
     */
    @Override
    @Transactional
    public void afterRechargeSuccess(IUser user, UserRecharge userRecharge) {
        if (StrUtils.isBlank(user.getPids())) return;
        // 是否受等级限制

        List<UserLevelConfig> levelList = userLevelConfigService.getActiveList(true);
        List<Long> levelIds = CollStreamUtil.toList(levelList, BaseModel::getId);
        Map<Long, UserLevelConfig> levelAuthMap = CollStreamUtil.toIdentityMap(levelList, UserLevelConfig::getId);
        UserLevelConfig userLevel = levelAuthMap.get(user.getLevelId());

        log.error("计算{}上级充值推广佣金---BEGIN", user.getUsername());
        List<Long> parentIdList = Arrays.stream(user.getPids().split(",")).filter(StrUtil::isNotBlank).map(Long::valueOf).collect(Collectors.toList());
        // 倒叙，获取1级->2级...
        Collections.reverse(parentIdList);
        // 批量计算获取用户
        List<IUser> parentList = userService.listByIds(parentIdList);
        Map<Long, IUser> parentLevelMap = CollStreamUtil.toIdentityMap(parentList, IUser::getId);
        List<DTO> tradeList = new ArrayList<>();
        for (int i = 0; i < parentIdList.size(); i++) {
            Long parentId = parentIdList.get(i);
            IUser parentUser = parentLevelMap.get(parentId);
            UserLevelConfig parentLevel = levelAuthMap.get(parentUser.getLevelId());
            if (parentLevel == null) {
                continue;
            }
            if (!parentLevel.getIsEnjoyTgCommission()) return;
            String rates = parentLevel.getRechargeCommissionRate();
            if (StrUtils.isBlank(rates)) return;
            List<BigDecimal> rateList = Arrays.stream(rates.split(",")).map(o -> new BigDecimal(o).movePointLeft(2)).collect(Collectors.toList());
            if (rateList.size() > i) {
                Boolean levelLimitOpen = parentLevel.getCommissionLevelLimitOpen();
                if ((!levelLimitOpen || parentLevel.getLevel() >= userLevel.getLevel())) {
                    UserMoneyChangeDTO userMoneyChangeDTO = new UserMoneyChangeDTO();
                    userMoneyChangeDTO.setTradeType(EnumTradeType.RECHARGE_TG_COMMISSION);
                    userMoneyChangeDTO.setAmount(userRecharge.getMoney().multiply(rateList.get(i)));
                    userMoneyChangeDTO.setUserId(parentUser.getId());
                    userMoneyChangeDTO.setUsername(parentUser.getUsername());
                    userMoneyChangeDTO.setOrderNo(userRecharge.getNo());
                    tradeList.add((DTO) userMoneyChangeDTO);
                }
            }


        }

        if (CollUtil.isNotEmpty(tradeList)) {
            AmountEntityUnitDTO<DTO> dto = new AmountEntityUnitDTO<>();
            dto.setRecordList(tradeList);
            this.change(dto);

        }

        // 在change中执行累计
//        for (DTO dto : tradeList) {
//            Long userId = dto.getUserId();
//            BigDecimal amount = dto.getAmount();
//            ((UserAmountTotalService<?, UserAmountTotal>) userAmountTotalService).addIncreaseField(uw -> uw.eq(UserAmountTotal::getUserId, userId)
//                    , new Tuple2<>(UserAmountTotal::getCommissionMoney, amount)
//                    , new Tuple2<>(UserAmountTotal::getTodayCommissionMoney, amount)
//            );
//        }

        log.error("计算{}上级充值推广佣金---END", user.getUsername());
    }

    @Override
    public List<Record> change(AmountEntityUnitDTO<DTO> dto) {

        // 执行帐变
        List<Record> records = super.change(dto);

        // 更新总计 userAmountTotal
        this.updateUserTotalAmount(records);

        return records;
    }

    /**
     * 更新总计 userAmountTotal
     */
    private void updateUserTotalAmount(List<Record> records) {
        Map<Long, UserAmountTotal> totalMap = new HashMap<>();
        for (Record record : records) {
            // 1.总计 初始化
            // 2.总计 累加
            userAmountTotalService.addUserAmountTotal(totalMap.computeIfAbsent(record.getUserId(), userAmountTotalService::newTotalZero), record.getRecordType(), record.getAmount());
        }
        // 2.总计 更新
        userAmountTotalService.batchIncrease(totalMap.values());
    }

}
