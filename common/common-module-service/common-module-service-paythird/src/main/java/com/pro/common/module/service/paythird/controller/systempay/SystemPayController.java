package com.pro.common.module.service.paythird.controller.systempay;

import com.alibaba.fastjson.JSONObject;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.service.pay.model.request.UnifiedOrderRequest;
import com.pro.common.module.service.pay.service.PayChannelService;
import com.pro.common.module.service.pay.service.PayMerchantService;
import com.pro.common.module.service.pay.service.UserRechargeService;
import com.pro.common.module.service.pay.service.UserTransferService;
import com.pro.common.module.service.paythird.controller.common.NotifyIO;
import com.pro.common.module.service.paythird.controller.common.PayCommonController;
import com.pro.common.module.service.paythird.controller.common.PayoutNotifyIO;
import com.pro.common.module.service.paythird.controller.common.UnifiedOrderIO;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.common.modules.service.dependencies.modelauth.base.MessageService;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@Api(tags = "系统支付")
@RestController
@RequestMapping("pay/systempay/{MER_CODE}")
public class SystemPayController extends PayCommonController {

    @Autowired
    private PayMerchantService merchantService;
    @Autowired
    private PayChannelService channelService;
    @Autowired
    private UserRechargeService rechargeService;
    @Autowired
    private UserTransferService userTransferService;
    @Autowired
    private IUserService userService;
    @Autowired
    private MessageService messageService;
    //通道编号
    private String merCode = "SYSTEMPAY";

    @Override
    public UserRecharge unifiedOrder(String MER_CODE, UnifiedOrderRequest requestVo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRecharge userRecharge = super.unifiedOrder(MER_CODE, requestVo, request, response);
        // 线下充值 通知管理后台确认
        messageService.sendToManager(ToSocket.toUser(CommonConst.EntityClass.UserRecharge, List.of(userRecharge)));
        return null;
    }

    /**
     * payMerchant.apiUrl 为空,只下单不请求外部,不需要构造
     */
    @Override
    protected Map<String, Object> unifiedOrderBuildParams(UnifiedOrderIO.Params params) {
        return new HashMap<>();
    }

    /**
     * admin 回调 pay
     */
    @Override
    protected NotifyIO.Params notifyReadData(JSONObject data) {
        return NotifyIO.Params.builder()
                .no(data.getString("no"))
                .statusSuccess(true)
                .amount(data.getString("money")) // 实际收款金额
                .build();
    }

    /**
     * 线下代付,直接修改状态 暂时不需要走pay
     * payMerchant.withdrawUrl 为空,只下单不请求外部,不需要构造
     */
    @Override
    protected Map<String, Object> payoutBuildParams(PayoutIO.Params params) {
        return new HashMap<>(16);
    }

    @Override
    protected PayoutNotifyIO.Params payoutNotifyReadData(JSONObject data) {
        return null;
    }
}
