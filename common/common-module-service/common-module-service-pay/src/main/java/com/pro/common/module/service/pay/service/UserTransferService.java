package com.pro.common.module.service.pay.service;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.type.TypeReference;
import com.pro.common.module.api.message.enums.EnumSysMsgBusinessCode;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.pay.enums.EnumTransferState;
import com.pro.common.module.api.pay.enums.EnumWithdrawStatus;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.db.PayoutChannel;
import com.pro.common.module.api.pay.model.db.UserTransfer;
import com.pro.common.module.api.pay.model.db.UserWithdraw;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.api.usermoney.model.db.UserMoney;
import com.pro.common.module.api.usermoney.model.intf.IUserMoneyUnitService;
import com.pro.common.module.service.pay.dao.UserTransferDao;
import com.pro.common.module.service.pay.model.request.UserTransferRequest;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.BeanUtils;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.mybatisplus.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 转账记录 服务实现类
 */
@Slf4j
@Service
public class UserTransferService extends BaseService<UserTransferDao, UserTransfer> {
    @Autowired
    @Lazy
    private UserTransferService _instance;
    @Autowired
    private IUserMoneyUnitService userMoneyUnitService;
    @Autowired
    private UserWithdrawService userWithdrawService;
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private ISysMsgService sysMsgService;
    @Autowired
    private IUserService userService;
    @Autowired
    private PayMerchantService payMerchantService;
    @Autowired
    private PayoutChannelService payoutChannelService;
    @Autowired
    private ThreadPoolTaskExecutor taskScheduler;

    public UserTransfer getByNo(String no) {
        return getOne(qw().lambda().eq(UserTransfer::getNo, no));
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized void paySuccess(Long recordId, String payNo) {
        UserTransfer record = this.getById(recordId);
        if (record == null) {
            log.error("转账订单不存在");
            throw new BusinessException("转账订单不存在");
        }
        if (record.getState() == EnumTransferState.SUCCESS || record.getState() == EnumTransferState.FAILED) {
            log.error("该转账已处理，不能重复操作！：" + record.getNo());
            throw new BusinessException("该转账已处理，不能重复操作！：" + record.getNo());
        }
        Map<String, UserMoney> userMoneyMap = userMoneyUnitService.listEntity(record.getUserId());
        UserMoney userMoney = userMoneyMap.get(record.getType());

        UserTransfer target = new UserTransfer();
        target.setState(EnumTransferState.SUCCESS);
        target.setPayNo(payNo);
        target.setBeforeMoney(userMoney.getAmount().add(record.getMoney()));
        target.setAfterMoney(userMoney.getAmount());
        target.setUpdateTime(LocalDateTime.now());
        target.setId(recordId);
        _instance.updateById(target);

        // 自动更新提现记录为成功
        userWithdrawService.updateSuccess(record.getNo());
    }

    //
    @Transactional(rollbackFor = Exception.class)
    public void payError(Long recordId, String payNo, String errorMessage) {
        UserTransfer record = this.getById(recordId);
        if (record == null) {
            log.error("转账订单不存在");
            throw new BusinessException("转账订单不存在");
        }
        if (record.getState() == EnumTransferState.SUCCESS || record.getState() == EnumTransferState.FAILED) {
            log.error("该转账已处理，不能重复操作！：" + record.getNo());
            throw new BusinessException("该转账已做退款处理，不能重复操作！：" + record.getNo());
        }
        UserTransfer target = new UserTransfer();
        target.setId(recordId);
        target.setPayNo(payNo);
        target.setState(EnumTransferState.FAILED);
        target.setStatus(EnumWithdrawStatus.REJECT);
        target.setErrorMessage(errorMessage);
        _instance.updateById(target);

        try {
            // 自动更新提现记录为失败，并退款
            UserWithdraw userWithdraw = this.getByNo(record.getNo());
//            userService.getById(userWithdraw.)
            userWithdrawService.payError(userWithdraw, "Transfer error,try again later");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发起代付
     */
    @Override
    @Transactional
    public boolean save(UserTransfer entity) {
        return this.saveBatch(Collections.singleton(entity));
    }

    @Override
    @Transactional
    synchronized public boolean saveBatch(Collection<UserTransfer> userTransfers) {
        switch (commonProperties.getApplication()) {
            case admin:
                if (userTransfers.isEmpty()) {
                    return false;
                }
                Map<Long, UserWithdraw> userWithdrawMap = userWithdrawService.getMap(qw -> qw.in(UserWithdraw::getId, CollUtils.propSet(userTransfers, UserTransfer::getUserWithdrawId)), UserWithdraw::getId);
                Map<Long, User> userMap = userService.getMap("id", CollUtils.propSet(userWithdrawMap.values(), UserWithdraw::getUserId));
                Map<Long, PayoutChannel> payoutChannelMap = payoutChannelService.getMap(qw -> qw.in(PayoutChannel::getId, CollUtils.propSet(userTransfers, UserTransfer::getPayoutChannelId)), PayoutChannel::getId);
                Map<String, PayMerchant> payMerchantMap = payMerchantService.getMap(qw -> qw.in(PayMerchant::getCode, CollUtils.propSet(payoutChannelMap.values(), PayoutChannel::getMerchantCode)), PayMerchant::getCode);

                List<UserWithdraw> userWithdrawsStateError = userWithdrawMap.values().stream().filter(userWithdraw -> !EnumWithdrawStatus.CHECKING.equals(userWithdraw.getStatus())).collect(Collectors.toList());
                AssertUtil.isTrue(userWithdrawsStateError.isEmpty(), "订单状态异常", userWithdrawsStateError.stream().map(UserWithdraw::getNo).collect(Collectors.joining(",")));

                Collection<UserTransfer> userTransfersSuccess = new ArrayList<>();
//                Collection<UserWithdraw> userWithdrawsSuccess = new ArrayList<>();
                String errorMsg = null;
                for (UserTransfer userTransfer : userTransfers) {
                    PayoutChannel payoutChannel = payoutChannelMap.get(userTransfer.getPayoutChannelId());
                    AssertUtil.isTrue(null != payoutChannel && payoutChannel.getEnabled(), "代付通道未开启" , userTransfer.getPayoutChannelId());
                    PayMerchant payMerchant = payMerchantMap.get(payoutChannel.getMerchantCode());
                    AssertUtil.isTrue(null != payMerchant && payMerchant.getEnabled(), "代付商户未开启", payoutChannel.getMerchantCode());
                    UserWithdraw userWithdraw = userWithdrawMap.get(userTransfer.getUserWithdrawId());
                    Boolean offlineFlag = payMerchant.getOfflineFlag();

                    userTransfer.setMerchantCode(payMerchant.getCode());
                    userTransfer.setPayoutChannelName(payoutChannel.getName());
                    userTransfer.setMoney(userTransfer.getApplyAmount().multiply(payoutChannel.getExchangeRate()));
                    // 线下代付,直接成功
                    // 线上代付,post
                    PayoutIO.Result result = offlineFlag ? PayoutIO.Result.success("") : this.payoutPost(userTransfer, payoutChannel);
                    // 代付成功
                    if (result.getSuccess()) {
                        BeanUtils.copyPropertiesModel(userWithdraw, userTransfer);
                        userTransfer.setOfflineFlag(offlineFlag);
                        userTransfer.setOfflineFlag(offlineFlag);
                        userTransfer.setStatus(offlineFlag ? EnumWithdrawStatus.PASS : EnumWithdrawStatus.TRANSFERRING);
                        userTransfer.setState(offlineFlag ? EnumTransferState.SUCCESS : EnumTransferState.SUBMITTED);
                        userTransfersSuccess.add(userTransfer);
//                        userWithdrawsSuccess.add(userWithdraw);
                    }
                    // 一个失败就中断后续的(之前成功的保存)
                    else {
                        errorMsg = "Three-party channel error: " + result.getErrorMsg();
                        break;
                    }
                }
                if (userTransfersSuccess.size() < userTransfers.size()) {
                    //  新的线程 批量保存
                    taskScheduler.execute(() -> this.saveSuccess(userMap, userTransfersSuccess));
                    throw new BusinessException(errorMsg);
                } else {
                    this.saveSuccess(userMap, userTransfersSuccess);
                }
                return true;
            default:
                throw new BusinessException("无权限");
        }
    }

    private void saveSuccess(Map<Long, User> userMap, Collection<UserTransfer> userTransfersSuccess) {
        if (userTransfersSuccess.isEmpty()) {
            return;
        }
        // 保存
        super.saveBatch(userTransfersSuccess);

        List<Long> successWithdrawId = userTransfersSuccess.stream().filter(t -> EnumWithdrawStatus.PASS.equals(t.getStatus())).map(UserTransfer::getUserWithdrawId).collect(Collectors.toList());
        List<Long> waitingWithdrawId = userTransfersSuccess.stream().filter(t -> !EnumWithdrawStatus.PASS.equals(t.getStatus())).map(UserTransfer::getUserWithdrawId).collect(Collectors.toList());
        if (!successWithdrawId.isEmpty()) {
            userWithdrawService.lambdaUpdate()
                    .set(UserWithdraw::getStatus, EnumWithdrawStatus.PASS)
                    .eq(UserWithdraw::getStatus, EnumWithdrawStatus.CHECKING)
                    .in(UserWithdraw::getId, successWithdrawId)
                    .update();
        }
        if (!waitingWithdrawId.isEmpty()) {
            userWithdrawService.lambdaUpdate()
                    .set(UserWithdraw::getStatus, EnumWithdrawStatus.TRANSFERRING)
                    .eq(UserWithdraw::getStatus, EnumWithdrawStatus.CHECKING)
                    .in(UserWithdraw::getId, waitingWithdrawId)
                    .update();
        }
        userTransfersSuccess.forEach(userTransfer -> sysMsgService.sendMsgAllChannelType(userMap.get(userTransfer.getUserId()), EnumSysMsgBusinessCode.WITHDRAW_SUCCESS.getCode(), Collections.singletonMap("userTransfer", userTransfer), null, null, false, false, null));
    }

    private PayoutIO.Result payoutPost(UserTransfer userTransfer, PayoutChannel payoutChannel) {
        UserTransferRequest requestVo = new UserTransferRequest();
        org.springframework.beans.BeanUtils.copyProperties(userTransfer, requestVo);
        requestVo.setBankId(userTransfer.getBankCode());
        requestVo.setPayType(payoutChannel.getPayType());
        requestVo.setChannelCoinCode(payoutChannel.getCoinCode());
//        requestVo.setChannelCode(channel.getChannelCode());
//        requestVo.setChannelCoinCode(channel.getCoinCode());
//        requestVo.setMoney(money); // 传送转化后的金额

        String merchantCode = userTransfer.getMerchantCode();
        String payoutUrlAppend = merchantCode.toLowerCase() + "/payout";
        if (merchantCode.contains("_")) {
            payoutUrlAppend = "/" + merchantCode.split("_")[0].toLowerCase() + "/" + merchantCode.toLowerCase() + "/payout";
        }
        String url = EnumAuthDict.PAY_URL.getValueCache() + "/pay" + payoutUrlAppend;
        try {
            String resultStr = HttpUtil.post(url, JSON.toJSONString(requestVo));
            if (JSONUtil.isTypeJSON(resultStr)) {
                return JSONUtils.fromString(resultStr, new TypeReference<R<PayoutIO.Result>>() {
                }).getData();
            }
            return PayoutIO.Result.fail(url + "异常|" + resultStr);
        } catch (Exception e) {
            log.error("代付失败: {} {}", url, e.getMessage(), e);
            return PayoutIO.Result.fail(url + "异常|" + e.getMessage());
        }
    }

//    @SneakyThrows
//    public static void main(String[] args) {
////        PayoutIO.Result data = JSONUtils.fromString("{\"code\":0,\"data\":{\"success\":false,\"errorMsg\":\"accountEmail is null or empty\"},\"timestamp\":\"1719928445346\"}", new TypeReference<R<PayoutIO.Result>>() {
////        }).getData();
//        R<PayoutIO.Result> t = new ObjectMapper().readerFor(new TypeReference<R<PayoutIO.Result>>() {
//        }).readValue("{\"code\":0,\"data\":{\"success\":false,\"errorMsg\":\"accountEmail is null or empty\"},\"timestamp\":\"1719928445346\"}");
//        System.out.println(t);
//    }
}
