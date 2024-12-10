package com.pro.common.module.service.admin.service;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.pay.enums.EnumRechargeState;
import com.pro.common.module.api.pay.enums.EnumRechargeType;
import com.pro.common.module.api.pay.intf.IRechargeSuccessService;
import com.pro.common.module.api.pay.model.db.PayChannel;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.api.usermoney.model.db.UserMoneyRecord;
import com.pro.common.module.api.usermoney.model.dto.UserMoneyChangeDTO;
import com.pro.common.module.api.usermoney.model.enums.EnumAmountNegativeDeal;
import com.pro.common.module.api.usermoney.model.enums.EnumTradeType;
import com.pro.common.module.api.usermoney.model.intf.IUserMoneyUnitService;
import com.pro.common.module.service.admin.dao.UserRechargeDao;
import com.pro.common.module.service.admin.model.request.UnifiedOrderRequest;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.modelauth.base.MessageService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.modules.service.dependencies.util.TransactionUtil;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.NumUtils;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class UserRechargeService extends BaseService<UserRechargeDao, UserRecharge> {
    @Autowired
    @Lazy
    private UserRechargeService _instance;
    @Autowired
    @Lazy
    private IUserService userService;
    @Autowired
    @Lazy
    private IUserMoneyUnitService userMoneyUnitService;
    @Autowired
    @Lazy
    private MessageService messageService;
    @Autowired
    @Lazy
    private ISysMsgService sysMsgService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired(required = false)
    private List<IRechargeSuccessService> rechargeSuccessService;

    /**
     * 创建支付订单
     */
    @Transactional(rollbackFor = Exception.class)
    public UserRecharge createRechargeOrder(UnifiedOrderRequest requestVo, PayChannel payChannel, PayMerchant merchant) {
        log.error("***************************** 请求【{}支付 - 生成订单：{} 】*******************************", merchant.getName(), requestVo.getPayType());
        if (null == requestVo.getUserId()) {
            throw new BusinessException("The user who placed the order cannot be empty");
        }
        // 提交金额
        BigDecimal applyAmount = ObjUtil.defaultIfNull(requestVo.getApplyAmount(), requestVo.getAmount());
        AssertUtil.isTrue(NumUtils.gt0(applyAmount), "The order amount must be a positive number");
        if (payChannel.getOnlyInteger()) {
            applyAmount = BigDecimal.valueOf(applyAmount.intValue());
        }

        // 手续费
        BigDecimal fee = applyAmount.multiply(payChannel.getFeeRate().movePointLeft(2));
        // 到账金额
        BigDecimal amount = applyAmount.subtract(fee);

        User user = userService.getById(requestVo.getUserId());
        if (null == user || !user.getEnabled()) {
            throw new BusinessException("User does not exist or has been frozen");
        }

        UserRecharge orders = UserRecharge.builder()
                .no(newNo())
                .userId(user.getId())
                .username(user.getUsername())
                .isDemo(user.getIsDemo())
                .phone(user.getPhone())
                .state(EnumRechargeState.UNPAID)
                .type(EnumRechargeType.RECHARGE_ONLINE)
                .applyMoney(applyAmount)
                .money(amount)
                .merchantCode(merchant.getCode())
                .merchantName(merchant.getName())
//                .times(0)
                .channelId(payChannel.getId())
                .payType(payChannel.getPayType())
                .coinCode(payChannel.getCoinCode())
                .exchangeRate(payChannel.getExchangeRate())
                .channelFeeRate(payChannel.getFeeRate())
                // 用户线下付款信息
                .bankName(requestVo.getBankName())
                .bankUsername(requestVo.getBankUsername())
                .bankAccount(requestVo.getBankAccount())
                .merchantNo(requestVo.getMerchantNo())
                .build();
        super.save(orders);

        TransactionUtil.doAfter(() -> {
            // 推送给管理端
            if (EnumDict.PUSH_ADMIN_RECHARGE_OPEN.getValueCacheOrDefault(true)) {
                messageService.sendToManager(ToSocket.toAllUser(CommonConst.EntityClass.UserRecharge, orders));
            }
        });
        return orders;
    }


    //
    public UserRecharge getByNo(String no) {
        return getOne(qw().lambda().eq(UserRecharge::getNo, no));
    }

    /**
     * 充值成功处理(在线充值)
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void paySuccess(Long recordId, String merchantNo, BigDecimal money, String description, String userDescription) {

        UserRecharge record = this.getById(recordId);
        if (record == null) {
            throw new BusinessException("pay orders not exist");
        }
        if (EnumRechargeState.PAID == record.getState()) {
            throw new BusinessException(I18nUtils.get("the order has paid") + record.getNo());
        }
        money = ObjectUtil.defaultIfNull(money, record.getMoney()).setScale(2, RoundingMode.DOWN);
        User user = userService.getById(record.getUserId());

        // 添加余额
        record.setMoney(money);
        UserMoneyRecord userMoneyRecord = this.userRechargeUserMoneyChange(record);

        // 更新充值订单
        UserRecharge target = UserRecharge.builder()
                .state(EnumRechargeState.PAID)
                .merchantNo(merchantNo)
                .payTime(LocalDateTime.now())
                .beforeMoney(userMoneyRecord.getAmountBefore())
                .afterMoney(userMoneyRecord.getAmountAfter())
                .money(money)
                .remark(description)
                .userRemark(userDescription)
                .build();
        target.setId(recordId);
        super.updateById(target);
        if (rechargeSuccessService!=null){
            rechargeSuccessService.forEach(x-> x.afterRechargeSuccess(user,record));
        }
        // 推送给管理端
        if (EnumDict.PUSH_ADMIN_RECHARGE_OPEN.getValueCacheOrDefault(true)) {
//            MessageRedisUtils.toSocket(new ToSocket(true, ToSocketUrl.getRechargeList, JSONUtil.toJsonStr(this.getById(target.getId()))));
            messageService.sendToManager(ToSocket.toUser(CommonConst.EntityClass.UserRecharge, this.getById(target.getId()), user.getId()));
        }

        //发送多消息通知
        sysMsgService.sendMsgAllChannelType(user, EnumSysMsgBusinessCode.RECHARGE_SUCCESS.getCode(), Collections.singletonMap("userRecharge", record), null, null, false, false, null);
    }

    private UserMoneyRecord userRechargeUserMoneyChange(UserRecharge record) {
        AssertUtil.notEmpty(record.getType(), "请先选择_", "type");
        AssertUtil.isTrue(NumUtils.ge(record.getMoney(), BigDecimal.ZERO), "请先输入_", "amount");
        EnumTradeType tradeType = EnumTradeType.valueOf(record.getType().name());
        UserMoneyRecord change = userMoneyUnitService.change(EnumAmountNegativeDeal.toNegative,
                 new UserMoneyChangeDTO(record.getUserId(), tradeType, record.getMoney(), record.getRemark(), record.getUserRemark(), record.getNo(), record.getId()));
        return change;
    }

    @Transactional(rollbackFor = Exception.class)
    public void payError(Long recordId, String merchantNo) {
        payError(recordId, merchantNo, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void payError(Long recordId, String merchantNo, String rejectReason) {
        UserRecharge record = _instance.getById(recordId);
        if (!EnumRechargeState.UNPAID.equals(record.getState())) {
            throw new BusinessException("该订单已处理");
        }
        // 更新订单
        UserRecharge target = new UserRecharge();
        target.setId(recordId);
        target.setState(EnumRechargeState.FAILED);
        target.setMerchantNo(merchantNo);
        target.setRemark(StrUtil.concat(true, record.getRemark(), rejectReason));
        super.updateById(target);
    }

    /**
     * 创建充值
     */
    @Override
    public boolean save(UserRecharge record) {
        switch (commonProperties.getApplication()) {
            case user:
            case pay:
                // 用户端走在线充值
                return false;
            case admin:
            case agent:
                if (!EnumRechargeType.DEDUCT_HANDLE.equals(record.getType())) {
                    // 管理端 用户列表 充值
                    User user = userService.getById(record.getUserId());
                    record.setUsername(user.getUsername());
                    record.setIsDemo(user.getIsDemo());
                    record.setState(EnumRechargeState.PAID);
                    record.setNo(newNo());
                    record.setPayTime(LocalDateTime.now());
                    super.save(record);
                }

                // 添加余额
                UserMoneyRecord userMoneyRecord = this.userRechargeUserMoneyChange(record);

                record.setBeforeMoney(userMoneyRecord.getAmountBefore());
                record.setAfterMoney(userMoneyRecord.getAmountAfter());
                return super.updateById(record);
            default:
                throw new BusinessException("unknown application");
        }
    }

    @Override
    public boolean updateById(UserRecharge record) {
        switch (commonProperties.getApplication()) {
            case user:
            case pay:
                // 用户端只走在线充值
                return false;
            case admin:
            case agent:
                // 管理端 充值列表 设为充值成功
                Long recordId = record.getId();
                UserRecharge old = this.getById(recordId);
                if (EnumRechargeState.UNPAID.equals(old.getState()) && EnumRechargeState.PAID.equals(record.getState())) {
                    // 改为充值成功
                    _instance.paySuccess(recordId, null, record.getMoney(), record.getRemark(), record.getUserRemark());
                    return true;
                } else {
                    return super.updateById(record);
                }
            default:
                throw new BusinessException("unknown application");
        }
    }

    private static String newNo() {
        return EnumTradeType.RECHARGE_ONLINE.getBillNoHead() + IdWorker.getIdStr() + "C";
    }
}
