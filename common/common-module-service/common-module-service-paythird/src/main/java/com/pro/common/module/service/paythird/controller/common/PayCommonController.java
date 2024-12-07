package com.pro.common.module.service.paythird.controller.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pro.common.module.api.pay.enums.EnumRechargeState;
import com.pro.common.module.api.pay.enums.EnumTransferState;
import com.pro.common.module.api.pay.model.db.PayChannel;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.pay.model.db.UserTransfer;
import com.pro.common.module.api.pay.model.dto.MchParams;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.api.user.intf.IUserService;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.module.service.admin.model.request.UnifiedOrderRequest;
import com.pro.common.module.service.admin.model.request.UserTransferRequest;
import com.pro.common.module.service.admin.service.PayChannelService;
import com.pro.common.module.service.admin.service.PayMerchantService;
import com.pro.common.module.service.admin.service.UserRechargeService;
import com.pro.common.module.service.admin.service.UserTransferService;
import com.pro.common.module.service.paythird.controller.IPayController;
import com.pro.common.module.service.paythird.util.SignatureUtil;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.modules.service.dependencies.util.IPUtils;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.LogicUtils;
import com.pro.framework.api.util.StrUtils;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// @Api(tags = "XXXPAY支付")
// @RestController
// @RequestMapping("pay/xxxpay/{MER_CODE}")
@Slf4j
@Getter
public abstract class PayCommonController implements IPayController<Object, Object> {

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

    // 使用率 JSON > FORM_URLENCODED (表单)
    private ContentType postContentType = ContentType.JSON;

    // 签名
    // 默认方式 key1=value1&key2=value2&key=秘钥 MD5小写
    private String signName = "sign";               //签名字段名
    private String payoutSignName = "sign";               //签名字段名
    private String signAppendKeyName = "key";       //签名拼接密钥的名称，null代表直接拼接密钥
    private EnumSignUpLow signUpLow = null;         //签名最后转大小写
    private List<String> signColumnFilter = null;   //签名过滤字段 null代表所有都参与验签
    private List<String> signColumnExclude = null;   //签名排除字段 null代表所有都参与验签

    //商户编号
    protected abstract String getMerCode();

    //构造下单签名sign
    @SneakyThrows
    protected String buildSignPost(Map<String, Object> params, String key, String signName) {
        return buildSign(params, key, signName);
    }

    //构造通知签名sign(后验证)
    @SneakyThrows
    protected String buildSignNotify(Map<String, Object> params, String key) {
        return buildSign(params, key, LogicUtils.or(notifyGetSignName(), signName));
    }

    //构造代付签名sign
    @SneakyThrows
    protected String buildPayoutSignPost(Map<String, Object> params, String key, String signName) {
        return buildSign(params, key, signName);
    }

    //代收下单 构造参数
    protected abstract Map<String, Object> unifiedOrderBuildParams(UnifiedOrderIO.Params params);

    //代收下单 返回转化
    protected UnifiedOrderIO.Result unifiedOrderReadResult(JSONObject result) {
        boolean success = "0".equals(result.getString("code"));
        return UnifiedOrderIO.Result.builder()
                .success(success)
                .errorMsg(result.getString("message"))
                .payUrl(success ? result.getJSONObject("data").getString("payUrl") : null)
                .build();
    }

    //代收通知 参数转化
    protected abstract NotifyIO.Params notifyReadData(JSONObject data);

    //代收通知 返回
    protected String notifyBuildReturn(Boolean success) {
        return success ? "success" : "fail";
    }

    //代付下单 构造参数
    protected abstract Map<String, Object> payoutBuildParams(PayoutIO.Params params);

    //代付下单 返回转化
    protected PayoutIO.Result payoutReadResult(JSONObject result) {
        boolean success = "0".equals(result.getString("code"));
        return PayoutIO.Result.builder()
                .success(success)
                .errorMsg(result.getString("message"))
                .build();
    }

    //代付通知 参数转化
    protected abstract PayoutNotifyIO.Params payoutNotifyReadData(JSONObject data);

    //代付通知 对参数中sign签名验证
    protected boolean checkSignNotify(Map<String, Object> map, JSONObject jo, String key, String mchKey, String signName, HttpServletRequest request, PayMerchant merchant) {
        return jo.getString(signName).equals(this.buildSignNotify(map, mchKey));
    }

    protected boolean checkPayoutSignNotify(Map<String, Object> map, JSONObject jo, String mchKey, String signName, HttpServletRequest request, PayMerchant merchant) {
        return jo.getString(signName).equals(this.buildSignNotify(map, mchKey));
    }

    //代付通知 返回
    protected String payoutNotifyBuildReturn(Boolean success) {
        return success ? "success" : "fail";
    }


    // http://localhost:8088/pay/inrpay/INRPAY_INRPAY/unifiedOrder?amount=100&payType=BANK&returnUrl=http://www.baidu.com&userId=1
    @ApiOperation("下单")
    @GetMapping("unifiedOrder")
//    @Transactional(rollbackFor = Exception.class)
    public UserRecharge unifiedOrder(@PathVariable String MER_CODE, UnifiedOrderRequest requestVo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("***************************** 请求【" + MER_CODE + "支付：{} 】***************]" + "***************", requestVo.getPayType());
        // 支付商户信息
        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        AssertUtil.isTrue(null != merchant && merchant.getEnabled(), "无效商户");
        // 支付渠道信息
        PayChannel channel = channelService.getByPayType(requestVo.getPayChannelId(), merchant.getCode(), requestVo.getPayType(), requestVo.getCardType());
        AssertUtil.isTrue(null != channel && channel.getEnabled(), "无效商户支付渠道");
        // ILoginInfo loginInfo
        User user = userService.getById(requestVo.getUserId());
        if (user == null) {
            user = userService.newInstant();
        }
        // 创建支付订单
        UserRecharge record = rechargeService.createRechargeOrder(requestVo, channel, merchant);

        // 线下充值无地址,不需要请求
        String url = merchant.getApiUrl();
        if (merchant.getOfflineFlag() || StrUtils.isBlank(url)) {
            return record;
        }
        // 回调地址
        String notifyUrl = EnumDict.PAY_NOTIFY_URL.getValueCache() + "pay/" + getMerCode().toLowerCase() + "/" + MER_CODE + "/notify";

        String mchKey = LogicUtils.or(merchant.getPublicKey(), merchant.getMchKey(), signName);

        Date now = new Date();
        // 参数
        BigDecimal money = record.getMoney();
        if (channel.getOnlyInteger()) {
            money = money.setScale(0, RoundingMode.DOWN);
        }
        UnifiedOrderIO.Params orderParams = UnifiedOrderIO.Params.builder()
                .payMerchant(merchant)
                .userId(user.getId())
                .phone(user.getPhone())
                .email(user.getEmail())
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
                .countryCurrency(EnumDict.SYSTEM_COUNTRY_CURRENCY.getValueCache())
                .build();
        Map<String, Object> params = this.unifiedOrderBuildParams(orderParams);

        String signName = this.unifiedOrderGetSignName();
        String sign = this.buildSignPost(params, mchKey, signName);
        params.put(signName, sign);

        String paramsJson = JSON.toJSONString(params);
        Console.log("下单请求地址：{}", url);
        Console.log(MER_CODE + "下单请求参数：{}", paramsJson);
        String resultStr = this.post(url, params, orderParams, false);
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

    public String unifiedOrderGetSignName() {
        return this.getSignName();
    }

    public String notifyGetSignName() {
        return this.getSignName();
    }

    public String notifyGetPayoutSignName() {
        return this.getPayoutSignName();
    }

    public String payoutGetSignName() {
        return this.getSignName();
    }

    public String payoutNoitfyGetSignName() {
        return this.getSignName();
    }

    @Override
    @ApiOperation("支付回调")
    @RequestMapping(value = "notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    public synchronized String notify(
            @PathVariable String MER_CODE,
            @RequestParam(required = false) Map<String, Object> map,
            @RequestBody(required = false) String requestBody,
            HttpServletRequest request) {
        log.info("************************************************************" + MER_CODE + "支付回调，content-type:{}", request.getContentType());
        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        map = this.read(map, requestBody);
        String jsonStr = JSON.toJSONString(map);
        Console.log("完整返回参数：{}", jsonStr);
        JSONObject jo = JSON.parseObject(jsonStr);

        NotifyIO.Params params = this.notifyReadData(jo);
        if (params.getStatusPending() != null && params.getStatusPending()) {
            return "Pending";
        }

        merchantService.checkNotifyIp(merchant.getWhitelist(), request);
        String no = params.getNo();
        String signName = notifyGetSignName();
        // 验签
        if (this.checkSignNotify(map, jo, LogicUtils.or(merchant.getPlatPublicKey(), merchant.getMchKey()), merchant.getMchKey(), signName, request, merchant)) {
            String payNo = params.getPayNo();
            BigDecimal amount = new BigDecimal(params.getAmount());
            UserRecharge record = rechargeService.getByNo(no);
            if (null == record || EnumRechargeState.UNPAID != record.getState()) {
                Console.log("订单不存在或已处理：{}", no);
            } else if (params.getStatusSuccess()) {
//                // 计算金额差距
                BigDecimal diffAmount = record.getMoney().subtract(amount).abs();
                if (diffAmount.compareTo(BigDecimal.ONE) > 0) {
                    Console.log("差额过大：{}", diffAmount);
                    return "diffAmount to large";
                }
                try {
                    rechargeService.paySuccess(record.getId(), payNo, new BigDecimal(params.getAmount()), null, null);
                    log.info("*******************************************" + MER_CODE + "支付成功");
                } catch (Exception e) {
                    Console.log(MER_CODE + "交易成功，处理失败：{}", e.getMessage());
                }
            } else {
                rechargeService.payError(record.getId(), payNo);
                Console.log(MER_CODE + "支付交易失败：{}", no);
            }
            return this.notifyBuildReturn(true);
        } else {
            Console.log(MER_CODE + "支付验签失败：{}", no);
            return "sign error";
        }
    }


    @ApiOperation("代付")
    @PostMapping("payout")
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
        String notifyUrl = EnumDict.PAY_NOTIFY_URL.getValueCache() + "pay/" + getMerCode().toLowerCase() + "/" + MER_CODE + "/payout/notify";

        String mchKey = LogicUtils.or(merchant.getPayoutMchKey(), merchant.getPublicKey(), merchant.getMchKey());

        PayoutIO.Params orderParams = PayoutIO.Params.builder()
                .payMerchant(merchant)
                .notifyUrl(notifyUrl)
                .payoutMchId(LogicUtils.or(merchant.getPayoutMchId(), merchant.getMchId()))
                .payoutMchKey(mchKey)
                .channelCode(requestVo.getChannelCode())
                .payType(LogicUtils.or(requestVo.getPayType()))
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
                .countryCurrency(EnumDict.SYSTEM_COUNTRY_CURRENCY.getValueCache())
                .ip(IPUtils.getIpAddress(request))
                .build();

        Map<String, Object> params = this.payoutBuildParams(orderParams);

        String signName = this.payoutGetSignName();
        String sign = this.buildPayoutSignPost(params, mchKey, signName);
        params.put(signName, sign);

        String url = merchant.getWithdrawUrl();
        if (merchant.getOfflineFlag() && StrUtils.isBlank(url)) {
            return R.ok(PayoutIO.Result.success(null));
        }
        Console.log("代付请求地址：{}", url);
        Console.log(MER_CODE + "代付请求参数：{}", JSON.toJSONString(params));
        String resultStr = this.post(url, params, orderParams, true);
        Console.log(MER_CODE + "代付返回结果：{}", resultStr);
        if (StrUtils.isBlank(resultStr)) {
            throw new BusinessException("payout result empty");
        }
        if (!JSONUtil.isJson(resultStr)) {
            throw new BusinessException(resultStr);
        }
        JSONObject jo = JSON.parseObject(resultStr);
        PayoutIO.Result result = this.payoutReadResult(jo);
//        if (!result.getSuccess()) {
//            return Response.error(-1, result.getErrorMsg());
//        }
        return R.ok(result);
    }

    @Override
    @ApiOperation("代付回调")
    @RequestMapping(value = "payout/notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    public synchronized String payoutNotify(
            @PathVariable String MER_CODE,
            @RequestParam(required = false) Map<String, Object> map,
            @RequestBody(required = false) String requestBody,
            HttpServletRequest request) {
        log.info("************************************************************" + MER_CODE + "代付回调，content-type:{}", request.getContentType());
        map = this.read(map, requestBody);
        String jsonStr = JSON.toJSONString(map);
        Console.log("完整返回参数：{}", jsonStr);
        JSONObject jo = JSON.parseObject(jsonStr);

        PayoutNotifyIO.Params params = this.payoutNotifyReadData(jo);
        if (params.getStatusPending() != null && params.getStatusPending()) {
            return "Pending";
        }

        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        merchantService.checkNotifyIp(merchant.getWhitelist(), request);
        String no = params.getNo();

        String signName = notifyGetPayoutSignName();
        // 验签
        if (this.checkPayoutSignNotify(map, jo, LogicUtils.or(merchant.getPayoutMchKey(), merchant.getPlatPublicKey(), merchant.getMchKey()), signName, request, merchant)) {
            String payNo = params.getPayNo();
            String remarks = params.getMessage();
            UserTransfer record = userTransferService.getByNo(no);
            if (null == record || record.getState() != EnumTransferState.SUBMITTED) {
                Console.log(MER_CODE + "订单不存在或已处理：{}", no);
            } else {
                if (params.getStatusFail()) {
                    Console.log(MER_CODE + "代付失败，no：{} {}", no, remarks);
                    userTransferService.payError(record.getId(), payNo, params.getMessage());
                }
                if (params.getStatusSuccess()) {
                    try {
                        userTransferService.paySuccess(record.getId(), payNo);
                        log.info("*******************************************" + MER_CODE + "代付成功");
                    } catch (Exception e) {
                        Console.log(MER_CODE + "代付成功，处理失败，no：{} - {}", no, e.getMessage());
                    }
                }
            }
            return payoutNotifyBuildReturn(true);
        } else {
            Console.log(MER_CODE + "代付验签失败：{}", no);
            return "sign error";
        }
    }

    private Map<String, Object> read(Map<String, Object> map, String requestBody) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (StrUtils.isNotBlank(requestBody) && JSONUtil.isJson(requestBody)) {
            JSONObject jo = JSON.parseObject(requestBody);
            map.putAll(jo);
        }
        return map;
    }

    private String post(String url, Map<String, Object> params, MchParams mchParams, boolean showError) {
        String result = "";
        //noinspection
        ContentType postContentType = this.getPostContentType();
        try {
            HttpRequest httpRequest = HttpUtil.createPost(url);
            switch (postContentType) {
                case FORM_URLENCODED:
                    httpRequest.form(params);
                    break;
                case JSON:
                    httpRequest.body(JSON.toJSONString(params));
                    break;
                default:
                    throw new BusinessException("unaccept contentType " + postContentType);
            }
            addHeaders(httpRequest, mchParams);
//            Console.log("下单请求头：{}", httpRequest.headers());
            result = httpRequest.execute().body();
        } catch (Exception e) {
            log.error("request error", e);
            throw new BusinessException("request error | " + (showError ? url : "") + " | " + e.getMessage());
        }
        return result;
    }

    protected void addHeaders(HttpRequest httpRequest, MchParams params) {

    }


    @SneakyThrows
    public String buildSign(Map<String, Object> params, String key, String signName) {
        Map<String, Object> paramsNew = new HashMap<>(params);
        paramsNew.remove("sign");
        paramsNew.remove(signName);
        paramsNew.values().removeIf(v -> null == v || "".equals(v));
        List<String> signColumnFilter = this.getSignColumnFilter();
        if (signColumnFilter != null) {
            paramsNew = paramsNew.keySet().stream().filter(signColumnFilter::contains).collect(Collectors.toMap(k -> k, paramsNew::get));
        }
        List<String> signColumnExclude = this.getSignColumnExclude();
        if (signColumnExclude != null) {
            paramsNew = paramsNew.keySet().stream().filter(o -> !signColumnExclude.contains(o)).collect(Collectors.toMap(k -> k, paramsNew::get));
        }
        Console.log("待签名参数：{}", paramsNew);
        String sign;
        String signAppendKeyName = this.getSignAppendKeyName();
        if (signAppendKeyName != null) {
            sign = SignatureUtil.generateSignWithKeyName(paramsNew, key, signAppendKeyName);
        } else {
            sign = SignatureUtil.generateSign(paramsNew, key);
        }
        EnumSignUpLow signUpLow = this.getSignUpLow();
        if (null != signUpLow) {
            switch (signUpLow) {
                case upper:
                    sign = sign.toUpperCase();
                    break;
                case lower:
                    sign = sign.toLowerCase();
                    break;
            }
        }
        Console.log("签名结果：{}", sign);
        return sign;
    }
}
