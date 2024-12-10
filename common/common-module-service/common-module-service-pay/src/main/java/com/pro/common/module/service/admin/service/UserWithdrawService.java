package com.pro.common.module.service.admin.service;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.pay.enums.EnumWithdrawStatus;
import com.pro.common.module.api.pay.model.db.UserWithdraw;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.user.intf.IUserBankCardService;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.api.user.model.db.UserBankCard;
import com.pro.common.module.api.userlevel.model.db.UserLevelConfig;
import com.pro.common.module.api.userlevel.model.intf.IUserLevelConfigService;
import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.common.module.api.usermoney.model.db.UserMoneyRecord;
import com.pro.common.module.api.usermoney.model.dto.UserMoneyChangeDTO;
import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.enums.EnumTradeType;
import com.pro.common.module.api.usermoney.model.intf.IUserMoneyUnitService;
import com.pro.common.module.service.admin.dao.UserWithdrawDao;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumPosterCode;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.exception.BusinessPosterException;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.common.modules.service.dependencies.modelauth.base.MessageService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.TransactionUtil;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.util.*;
import com.pro.framework.mybatisplus.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserWithdrawService extends BaseService<UserWithdrawDao, UserWithdraw> {
    @Autowired
    private ISysMsgService sysMsgService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IUserBankCardService userBankCardService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private IUserLevelConfigService userLevelConfigService;
    @Autowired
    private IUserMoneyUnitService userMoneyUnitService;

    Set<EnumWithdrawStatus> OngoingStatus = new HashSet<>(Arrays.asList(EnumWithdrawStatus.CHECKING, EnumWithdrawStatus.TRANSFERRING));

    /**
     * 付款失败
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void payError(UserWithdraw userWithdrawOld, String reason) {

        if (!OngoingStatus.contains(userWithdrawOld.getStatus())) {
            throw new BusinessException("当前状态为" + userWithdrawOld.getStatus().getLabel() + "，不能执行该操作");
        }
        UserWithdraw userWithdraw = new UserWithdraw();
        userWithdraw.setId(userWithdrawOld.getId());
        userWithdraw.setStatus(EnumWithdrawStatus.REJECT);
        userWithdraw.setRejectReason(reason);
        super.updateById(userWithdraw);
        User user = userService.getById(userWithdrawOld.getUserId());

        // 添加交易记录
        UserMoneyChangeDTO userMoneyDTO = new UserMoneyChangeDTO();
        userMoneyDTO.setUserId(userWithdrawOld.getUserId());
        userMoneyDTO.setTradeType(EnumTradeType.WITHDRAW_REJECT);
        userMoneyDTO.setUsername(userWithdrawOld.getUsername());
        userMoneyDTO.setAmount(userWithdrawOld.getApplyAmount());
        userMoneyDTO.setOrderNo(userWithdrawOld.getNo());
        userMoneyDTO.setOrderId(userWithdrawOld.getId());
        userMoneyDTO.setAmountType(userWithdrawOld.getType());
//        userMoneyDTO.setRemark(userWithdraw.getre);
        userMoneyDTO.setUserRemark(reason);
        userMoneyUnitService.change(EnumAmountNegativeDeal.toNegative, userMoneyDTO);

        TransactionUtil.doAfter(() -> sysMsgService.sendMsgAllChannelType(user, EnumSysMsgBusinessCode.WITHDRAW_FAIL.name(), Collections.singletonMap("userWithdraw", userWithdraw), user.getLang(), null, false, false, null));

        // 推送系统通知
        messageService.sendToUser(ToSocket.toUser(CommonConst.EntityClass.UserWithdraw, userWithdraw, user.getId()));
    }

    public UserWithdraw getByNo(String withdrawNo) {
        return getOne(qw().lambda().eq(UserWithdraw::getNo, withdrawNo).last("limit 1"));
    }

    //
    public void updateSuccess(String withdrawNo) {
        super.lambdaUpdate()
                .set(UserWithdraw::getStatus, EnumWithdrawStatus.PASS)
                .set(UserWithdraw::getUpdateTime, LocalDateTime.now())
                .eq(UserWithdraw::getNo, withdrawNo).update();
    }

    @Override
    @Transactional
    public boolean save(UserWithdraw entity) {
        switch (commonProperties.getApplication()) {
            case user:
                AssertUtil.isTrue(NumUtils.gt0(entity.getApplyAmount()), "无效的金额");
                Long userId = entity.getUserId();
                User user = userService.getById(userId);
                if (!user.getWithdrawOpen()) {
                    throw new BusinessPosterException(user.getWithdrawPosterCode());
                }
                if (!user.getFanOpen()) {
                    throw new BusinessPosterException(EnumPosterCode.withdraw_user_fans_close.name());
                }
                UserLevelConfig userLevelConfig = userLevelConfigService.getByIdCache(user.getLevelId());
//                AssertUtil.notEmpty(userLevelConfig, "无效的等级");
                AssertUtil.isTrue(user.getTkPassword().equals(PasswordUtils.encrypt_tkPassword(entity.getTkPassword())), "提款密码错误");

                AssertUtil.notEmpty(entity.getBankCardId(), "清先选择银行卡");
                UserBankCard userBankCard = userBankCardService.getById(entity.getBankCardId());
                AssertUtil.isTrue(userId.equals(userBankCard.getUserId()), "无权限");


                BeanUtils.copyPropertiesModel(userBankCard, entity);
                entity.setType(LogicUtils.or(entity.getType(), FrameworkConst.Str.DEFAULT));
                entity.setUserId(user.getId());
                entity.setUsername(user.getUsername());
                entity.setIsDemo(user.getIsDemo());
                entity.setNo(EnumTradeType.WITHDRAW.getBillNoHead() + IdWorker.getIdStr() + "W");
                boolean save = super.save(entity);
                if (!save) {
                    return false;
                }
                UserMoneyRecord change = userMoneyUnitService.change(EnumAmountNegativeDeal.throwException, new UserMoneyChangeDTO(
                        userId,
                        EnumTradeType.WITHDRAW,
                        entity.getApplyAmount(),
                        "",
                        "",
                        entity.getNo(),
                        entity.getId()
                ));
                entity.setBeforeMoney(change.getAmountBefore());
                entity.setAfterMoney(change.getAmountAfter());

                super.updateById(entity);
                TransactionUtil.doAfter(() -> {
                    // 推送给管理端
                    if (EnumDict.PUSH_ADMIN_WITHDRAW_OPEN.getValueCacheOrDefault(true)) {
                        messageService.sendToManager(ToSocket.toAllUser(CommonConst.EntityClass.UserWithdraw, entity));
                    }
                });
                return true;
            default:
        }
        throw new BusinessException("无权限");
    }

    /**
     * 更新
     */
    @Override
    @Transactional
    public boolean updateById(UserWithdraw request) {
        switch (commonProperties.getApplication()) {
            case admin:
                UserWithdraw entityOld = this.getById(request.getId());
                // 驳回
                switch (request.getStatus()) {
                    case REJECT:
                        this.payError(entityOld, request.getRejectReason());
                        return true;
                }
                break;
            default:
        }
        throw new BusinessException("无权限");
    }
}
