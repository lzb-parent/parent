package com.pro.common.module.service.paythird.controller.nwaipay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.http.ContentType;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pro.common.module.api.pay.model.db.PayChannel;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.service.pay.model.request.UnifiedOrderRequest;
import com.pro.common.module.service.pay.model.request.UserTransferRequest;
import com.pro.common.module.service.pay.service.PayChannelService;
import com.pro.common.module.service.pay.service.PayMerchantService;
import com.pro.common.module.service.pay.service.UserRechargeService;
import com.pro.common.module.service.pay.service.UserTransferService;
import com.pro.common.module.service.paythird.controller.common.*;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.modules.service.dependencies.util.IPUtils;
import com.pro.framework.api.util.LogicUtils;
import com.pro.framework.api.util.StrUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
@RestController
@Tag(name = "nwaipay支付")
@RequestMapping("pay/nwaipay/{MER_CODE}")
public class NwaipayController extends PayCommonController {
    // http://localhost:8088/pay/nwaipay/NWAIPAY_NWAIPAY/unifiedOrder?amount=100&payType=GCASH_P&returnUrl=http://www.baidu.com&userId=1

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

    private String signName = "sign";

    //通道编号
    private String merCode = "NWAIPAY";
    // private String signAppendKeyName = null;
    private EnumSignUpLow signUpLow = EnumSignUpLow.lower;

    // 使用率 JSON > FORM_URLENCODED (表单)
    private ContentType postContentType = ContentType.FORM_URLENCODED;
    private List<String> signColumnFilter = Arrays.asList(
            "parter",
            "value",
            "type",
            "orderid",
            "notifyurl",
            "callbackurl",

            "order_no",
            "name",
            "account_number",
            "money",
            "notify_url",

            "opstate",
            "ovalue",
            "",
            "",
            "",
            ""
    );


    @Operation(summary = "下单")
//    @GetMapping("unifiedOrder")
    @Transactional(rollbackFor = Exception.class)
    public UserRecharge unifiedOrder(@PathVariable String MER_CODE, UnifiedOrderRequest requestVo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("***************************** 请求【" + MER_CODE + "支付：{} 】***************]" + "***************", requestVo.getPayType());
        // 支付商户信息
        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        if (null == merchant || !merchant.getEnabled()) {
            throw new BusinessException("无效商户");
        }
        // 支付渠道信息
        PayChannel channel = channelService.getByPayType(requestVo.getPayChannelId(), merchant.getCode(), requestVo.getPayType(), requestVo.getCardType());
        if (null == channel || !channel.getEnabled()) {
            throw new BusinessException("无效商户支付渠道");
        }
        // 创建支付订单
        UserRecharge record = rechargeService.createRechargeOrder(requestVo, channel, merchant);

        // 回调地址
        String notifyUrl = EnumAuthDict.PAY_NOTIFY_URL.getValueCache() + "pay/" + getMerCode().toLowerCase() + "/" + MER_CODE + "/notify";

        String mchKey = LogicUtils.or(merchant.getPublicKey(), merchant.getMchKey(), signName);

        Date now = new Date();
        // 参数
        BigDecimal money = record.getMoney();
        if (channel.getOnlyInteger()) {
            money = money.setScale(0, RoundingMode.DOWN);
        }
        UnifiedOrderIO.Params orderParams = UnifiedOrderIO.Params.builder()
                .payMerchant(merchant)
                .userId(record.getUserId())
                .phone(record.getPhone())
                .username(record.getUsername())
                .notifyUrl(notifyUrl)
                .returnUrl(URLDecoder.decode(requestVo.getReturnUrl(), StandardCharsets.UTF_8))
                .mchId(merchant.getMchId())
                .mchKey(mchKey)
//                .channelCode(channel.getChannelCode())
                .payType(channel.getPayType())
                .no(record.getNo())
                .money(money)
                .ip(IPUtils.getIpAddress(request))
                .now_long(now.getTime())
                .now_yyyy_MM_dd_HH_mm_ss(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"))
                .now_yyyyMMddHHmmss(DateUtil.format(now, "yyyyMMddHHmmss"))
                .countryCurrency(EnumAuthDict.SYSTEM_COUNTRY_CURRENCY.getValueCache())
                .build();
        Map<String, Object> params = this.unifiedOrderBuildParams(orderParams);

        String signName = this.unifiedOrderGetSignName();
        String sign = this.buildSignPost(params, mchKey, signName);
        params.put(signName, sign);

        String paramsJson = JSON.toJSONString(params);
        Console.log("下单请求地址：{}", merchant.getApiUrl());
        Console.log(MER_CODE + "下单请求参数：{}", paramsJson);
        String resultStr = NWHttp.Get(merchant.getApiUrl(), params);
        Console.log(MER_CODE + "下单返回结果：{}", resultStr);
        if (StrUtils.isBlank(resultStr)) {
            throw new BusinessException("pay result empty");
        }
        JSONObject jo = JSON.parseObject(resultStr);
        UnifiedOrderIO.Result result = this.unifiedOrderReadResult(jo);
        if (!result.getSuccess()) {
            throw new BusinessException(I18nUtils.get("pay error") + ":" + result.getErrorMsg());
        } else {
            response.sendRedirect(result.getPayUrl());
        }
        return record;
    }

    @Operation(summary = "代付")
//    @PostMapping("payout")
    @SneakyThrows
    public R<PayoutIO.Result> payout(@PathVariable String MER_CODE, @RequestBody UserTransferRequest requestVo, HttpServletRequest request, HttpServletResponse response) {
        log.error("***************************** 请求【" + MER_CODE + "代付：{} 】*******************************", requestVo.getPayType());
        Date now = new Date();

        // 支付商户信息
        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        if (null == merchant || !merchant.getEnabled()) {
            throw new BusinessException("无效商户");
        }

        // 回调地址
        String notifyUrl = EnumAuthDict.PAY_NOTIFY_URL.getValueCache() + "pay/" + getMerCode().toLowerCase() + "/" + MER_CODE + "/payout/notify";

        String mchKey = LogicUtils.or(merchant.getPayoutMchKey(), merchant.getPublicKey(), merchant.getMchKey());

        PayoutIO.Params orderParams = PayoutIO.Params.builder()
                .payMerchant(merchant)
                .notifyUrl(notifyUrl)
                .payoutMchId(LogicUtils.or(merchant.getPayoutMchId(), merchant.getMchId()))
                .payoutMchKey(mchKey)
                .channelCode(requestVo.getChannelCode())
                .payType(requestVo.getPayType())
                .no(requestVo.getNo())
                .amount(requestVo.getMoney())
                .now_long(now.getTime())
                .now_yyyyMMddHHmmss(DateUtil.format(now, "yyyy-MM-dd HH:mm:ss"))
                .bankAccount(requestVo.getBankAccount())
                .bankId(LogicUtils.or(requestVo.getBankId(), requestVo.getBankName()))
                .bankName(requestVo.getBankName())
                .bankUsername(LogicUtils.or(requestVo.getBankUsername(), StrUtils.EMPTY).trim())
                .bankBranchName(LogicUtils.or(requestVo.getBankBranchName(), StrUtils.EMPTY).trim())
                .bankPhone(requestVo.getBankPhone())
                .bankAddress(requestVo.getBankAddress())
                .email(requestVo.getEmail())
                .cardType(requestVo.getCardType())
                .idCard(requestVo.getIdCard())
                .upi(requestVo.getBankAccount2())//印度ifsc
                .ifsc(requestVo.getBankAccount1())//印度ifsc
                .cci(requestVo.getBankAccount1())//秘鲁CCI
                .wallet(LogicUtils.or(requestVo.getWallet(), requestVo.getBankAccount2()))
                .username(requestVo.getUsername())
                .countryCurrency(EnumAuthDict.SYSTEM_COUNTRY_CURRENCY.getValueCache())
                .ip(IPUtils.getIpAddress(request))
                .build();

        Map<String, Object> params = this.payoutBuildParams(orderParams);

        String signName = this.payoutGetSignName();
        String sign = this.buildPayoutSignPost(params, mchKey, signName);
        params.put(signName, sign);

        Console.log("代付请求地址：{}", merchant.getWithdrawUrl());
        Console.log(MER_CODE + "代付请求参数：{}", JSON.toJSONString(params));

        String resultStr = NWHttp.Get(merchant.getWithdrawUrl(), params);

        Console.log(MER_CODE + "代付返回结果：{}", resultStr);
        if (StrUtils.isBlank(resultStr)) {
            throw new BusinessException("payout result empty");
        }
        if (!JSONUtil.isJson(resultStr)) {
            throw new BusinessException(resultStr);
        }
        JSONObject jo = JSON.parseObject(resultStr);
        PayoutIO.Result result = this.payoutReadResult(jo);
        if (!result.getSuccess()) {

            return R.ok(PayoutIO.Result.fail(result.getErrorMsg()));
        }

        return R.ok(PayoutIO.Result.success(null));
    }


    //代收下单 构造参数
    @Override
    public Map<String, Object> unifiedOrderBuildParams(UnifiedOrderIO.Params params) {
        return MapBuilder.<String, Object>create()
                .put("parter", params.getMchId())
                .put("value", params.getMoney())
                .put("type", params.getPayType())
                .put("orderid", params.getNo())
                .put("notifyurl", params.getNotifyUrl())
                .put("ip", params.getIp())
                .put("callbackurl", params.getReturnUrl())
                .build()
                ;
    }

    //代收下单 返回转化
    @Override
    public UnifiedOrderIO.Result unifiedOrderReadResult(JSONObject result) {
        boolean success = "200".equals(result.getString("code"));
        return UnifiedOrderIO.Result.builder()
                .success(success)
                .errorMsg(result.getString("info"))
                .payUrl(success ? result.getJSONObject("param").getString("payment_url") : null)
                .build();
    }

    //代收通知 参数转化
    @Override
    public NotifyIO.Params notifyReadData(JSONObject data) {
        return NotifyIO.Params.builder()
                .no(data.getString("orderid"))
                .amount(data.getString("ovalue"))
                .statusPending(false)
                .statusSuccess("1".equals(data.getString("opstate")))
//                .payNo(data.getString("platOrderNum"))
                .build()
                ;
    }

    //代收通知 返回
    @Override
    public String notifyBuildReturn(Boolean success) {
        return success ? "success" : "fail";
    }

    //代付下单 构造参数
    @Override
    public Map<String, Object> payoutBuildParams(PayoutIO.Params params) {
        return MapBuilder.<String, Object>create()
                .put("parter", params.getPayoutMchId())
                .put("order_no", params.getNo())
                .put("type", params.getBankId())
                .put("name", LogicUtils.or(params.getBankUsername(), params.getUsername()))
                .put("account_number", params.getBankAccount())
                .put("money", params.getAmount().setScale(0, RoundingMode.DOWN))
//                .put("money", params.getAmount().setScale(2, RoundingMode.DOWN))
                .put("notify_url", params.getNotifyUrl())
                .build()
                ;
    }

    //代付下单 返回转化
    @Override
    public PayoutIO.Result payoutReadResult(JSONObject result) {
        boolean success = "200".equals(result.getString("code"));
        return PayoutIO.Result.builder()
                .success(success)
                .errorMsg(result.getString("info"))
                .build();
    }

    //代付通知 参数转化
    @Override
    public PayoutNotifyIO.Params payoutNotifyReadData(JSONObject data) {
        return PayoutNotifyIO.Params.builder()
                .no(data.getString("orderid"))
                .amount(data.getString("ovalue"))
                .statusPending(false)
//                .statusPending("0".equals(data.getString("opstate")))
                .statusSuccess("1".equals(data.getString("opstate")))
                .statusFail("0".equals(data.getString("opstate")))
//                .payNo(data.getString("platOrderNum"))
                .build()
                ;
    }

    //代付通知 返回
    @Override
    public String payoutNotifyBuildReturn(Boolean success) {
        return success ? "success" : "fail";
    }
}
