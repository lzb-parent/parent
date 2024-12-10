
package com.pro.common.module.service.paythird.controller.qepay;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.pro.common.module.api.pay.enums.EnumRechargeState;
import com.pro.common.module.api.pay.enums.EnumTransferState;
import com.pro.common.module.api.pay.model.db.PayChannel;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.pay.model.db.UserTransfer;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.api.system.model.enums.EnumDict;
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
import com.pro.framework.api.util.LogicUtils;
import com.pro.framework.api.util.StrUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "qepay支付")
@RestController
@RequestMapping("pay/qepaynew/{MER_CODE}")
@Slf4j
public class QePayNewController implements IPayController<RechargeResult, WithdrawResult> {
    private static final String _MER_CODE = "QEPAYNEW";

    @Autowired
    private PayMerchantService merchantService;
    @Autowired
    private PayChannelService channelService;
    @Autowired
    private UserRechargeService rechargeService;
    @Autowired
    private UserTransferService userTransferService;

    // http://localhost:8088/pay/qepaynew/QEPAYNEW_8PAY/unifiedOrder?amount=100&payType=2600&returnUrl=http://www.baidu.com&userId=1
    @Override
    @ApiOperation("下单")
    @GetMapping("unifiedOrder")
    @Transactional(rollbackFor = Exception.class)
    public UserRecharge unifiedOrder(@PathVariable String MER_CODE, UnifiedOrderRequest requestVo, HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.error("***************************** 请求【" + _MER_CODE + "支付：{} 】*******************************", requestVo.getPayType());

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
        String notifyUrl = EnumDict.PAY_NOTIFY_URL.getValueCache() + "pay/" + _MER_CODE.toLowerCase() + "/" + MER_CODE + "/notify";

        // 参数
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("version", "1.0");
        params.put("mch_id", merchant.getMchId());
        params.put("notify_url", notifyUrl);
        params.put("mch_order_no", record.getNo());
        params.put("pay_type", channel.getPayType());
        params.put("trade_amount", record.getMoney().intValue());
        params.put("order_date", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        params.put("goods_name", MER_CODE);
        if (!StrUtils.isNotBlank(channel.getBankCode())) {
            params.put("bank_code", channel.getBankCode());
        } else if ("IDN".equals(EnumDict.SYSTEM_COUNTRY.getValueCache())) { // 印尼
            params.put("bank_code", channel.getBankCode());
//            params.put("bank_code", StrUtils.blankToDefault(channel.getPayType(), "BCA"));
        } else if ("NGA".equals(EnumDict.SYSTEM_COUNTRY.getValueCache())) {
            params.put("bank_code", "NGR044");
        } else if ("021".equals(channel.getPayType())) { // 网银直联 bank_code填写ACB
            params.put("bank_code", "ACB");
        } else if ("200".equals(channel.getPayType()) || "220".equals(channel.getPayType())) {
            params.put("bank_code", "B2C");
        }
        // 去除空值参数
        params = params.entrySet().stream()
                .filter(entry -> entry.getValue() != null && !StrUtils.EMPTY.equals(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String sign = getSign(params, merchant.getMchKey());
        params.put("sign", sign);
        params.put("sign_type", "MD5");
        log.error("下单请求参数：{}", params);

        String apiUrl = merchant.getApiUrl();
        Console.log("下单请求域名：{}", apiUrl);
        String result = HttpUtil.post(apiUrl, params);
        Console.log("下单返回结果：{}", result);
        if (StrUtils.isNotBlank(result)) {
            throw new BusinessException("pay result empty");
        }
        JSONObject jo = JSON.parseObject(result);
        if (!"SUCCESS".equals(jo.getString("respCode"))) {
            throw new BusinessException(I18nUtils.get("pay error") + ":" + jo.getString("tradeMsg"));
        } else {
            response.sendRedirect(jo.getString("payInfo"));
        }
        return record;
    }

    @Override
    @ApiOperation("支付回调")
//    @RequestMapping(value = "notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    public synchronized String notify(
            @PathVariable String MER_CODE,
            RechargeResult result,
            HttpServletRequest request) {
        log.info("************************************************************" + _MER_CODE + "支付回调，content-type:{}", request.getContentType());
        log.error("完整返回参数：{}", result);

        // order_status 订单状态: 订单状态，1成功
        Integer order_status = result.getTradeResult();

        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        merchantService.checkNotifyIp(merchant.getWhitelist(), request);

        String no = result.getMchOrderNo();
        // 验签
        Map<String, Object> map = SignatureUtil.object2Map(result);
        if (result.getSign().equals(getSign(map, merchant.getMchKey()))) {
            UserRecharge record = rechargeService.getByNo(no);
            if (null == record || EnumRechargeState.UNPAID != record.getState()) {
                log.error("订单不存在或已处理：{}", no);
                return "success";
            }
            if (1 == order_status) {
                // 计算金额差距
                BigDecimal diffAmount = record.getMoney().subtract(result.getAmount()).abs();
                if (diffAmount.compareTo(BigDecimal.ONE) > 0) {
                    log.error("差额过大：{}", diffAmount);
                    return null;
                }
                try {
                    rechargeService.paySuccess(record.getId(), result.getOrderNo(), null, null, null);
                    log.info("*******************************************" + _MER_CODE + "支付成功");
                } catch (Exception e) {
                    log.error("交易成功，处理失败：{}", e.getMessage());
                }
            } else {
                rechargeService.payError(record.getId(), result.getOrderNo());
                log.error(_MER_CODE + "支付交易失败：{}", no);
            }
            return "success";
        } else {
            log.error(_MER_CODE + "支付验签失败：{}", no);
            return "sign error";
        }
    }

    @Override
    @ApiOperation("代付")
    @PostMapping("payout")
    public R<PayoutIO.Result> payout(@PathVariable String MER_CODE, @RequestBody UserTransferRequest requestVo, HttpServletRequest request, HttpServletResponse response) {
        log.error("***************************** 请求【" + _MER_CODE + "代付：{} 】*******************************", requestVo.getPayType());

        // 支付商户信息
        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        if (null == merchant || !merchant.getEnabled()) {
            throw new BusinessException("无效商户");
        }

        // 回调地址
        String notifyUrl = EnumDict.PAY_NOTIFY_URL.getValueCache() + "pay/" + _MER_CODE.toLowerCase() + "/" + MER_CODE + "/payout/notify";

        // 参数
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("mch_id", merchant.getPayoutMchId());
        params.put("mch_transferId", requestVo.getNo());
        params.put("transfer_amount", requestVo.getMoney().intValue());
        params.put("apply_date", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        params.put("back_url", notifyUrl);
        params.put("receive_name", requestVo.getBankUsername());
        params.put("receive_account", requestVo.getBankAccount());
        params.put("bank_code", LogicUtils.or(requestVo.getBankId(), "MAY"));

        String country = EnumDict.SYSTEM_COUNTRY.getValueCache();
        if ("IND".equals(country)) {
            params.put("remark", requestVo.getBankAccount1()); // 印度代付必填 IFSC 码
            params.put("bank_code", "IDPT0001"); // 印度IDPT0001，巴西BRL001；通过统一的银行代码方式传入
        } else if ("COL".equals(country)) { // 哥伦比亚
            params.put("remark", requestVo.getIdCard());
            params.put("receive_account", LogicUtils.or(requestVo.getBankAccount(), requestVo.getBankAccount1()));
        } else if ("ECU".equals(country)) { // 厄尔瓜多
            params.put("remark", requestVo.getIdCard());
        } else if ("BRA".equals(country)) {
            params.put("bank_code", "PIXPAY");
            params.put("document_id", requestVo.getBankAccount2());
            params.put("document_type", requestVo.getCardType());
            params.put("receive_account", requestVo.getBankAccount1());
        } else if ("ZAF".equals(country)) { // 南非
            params.put("bank_code", LogicUtils.or(requestVo.getBankId(), "ZAR10010"));
        } else if ("PHL".equals(country)) { // 菲律宾
            params.put("receive_account", LogicUtils.or(requestVo.getBankAccount1(), requestVo.getBankAccount()));
            params.put("bank_code", "GCASH");
        } else if ("GHA".equals(country)) { // 加纳
            params.put("receiver_telephone", LogicUtils.or(requestVo.getBankPhone()));
            params.put("bank_code", "GHSMTN");
            params.put("document_id", requestVo.getIdCard());
            params.put("document_type", requestVo.getCardType());
        } else if ("BGD".equals(country)) { // 孟加拉
            params.put("receive_account", requestVo.getWallet());
            params.put("bank_code", "BDT25000f012"); // BKash
        } else if ("PER".equals(country)) { // 秘鲁
            params.put("remark", requestVo.getBankAccount1());
        } else if ("EGY".equals(country)) { // 埃及
            params.put("bank_code", "Vodafone");
            params.put("receive_account", LogicUtils.or(requestVo.getWallet(), requestVo.getBankAccount()));
            params.put("receive_telephone", LogicUtils.or(requestVo.getBankPhone(), requestVo.getUsername()));
        }

        String sign = getSign(params, merchant.getPayoutMchKey());
        params.put("sign", sign);
        params.put("sign_type", "MD5");
        Console.log(_MER_CODE + "代付请求参数：{}", params);
        String result = HttpUtil.post(merchant.getWithdrawUrl(), params);
        Console.log(_MER_CODE + "代付返回结果：{}", result);
        if (StrUtils.isNotBlank(result)) {
            throw new BusinessException("payout result empty");
        }
        JSONObject jo = JSON.parseObject(result);
        if (!"SUCCESS".equals(jo.getString("respCode"))) {
            return R.ok(PayoutIO.Result.fail(jo.getString("errorMsg")));
        }
        return R.ok(PayoutIO.Result.success(null));
    }

    @Override
    @ApiOperation("代付回调")
//    @RequestMapping(value = "payout/notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    public synchronized String payoutNotify(
            @PathVariable String MER_CODE,
            WithdrawResult result,
            HttpServletRequest request) {
        log.info("************************************************************" + _MER_CODE + "代付回调，content-type:{}", request.getContentType());
        log.error("完整返回参数：{}", result);

        // 0正在处理中 1转账成功 2转账失败
        Integer status = result.getTradeResult();
        if (status == 0) {
            return "Pedding";
        }

        PayMerchant merchant = merchantService.getByCode(MER_CODE);
        merchantService.checkNotifyIp(merchant.getWhitelist(), request);

        String no = result.getMerTransferId();

        // 验签
        Map<String, Object> map = SignatureUtil.object2Map(result);
        if (result.getSign().equals(getSign(map, merchant.getPayoutMchKey()))) {
            UserTransfer record = userTransferService.getByNo(no);
            if (null == record || record.getState() != EnumTransferState.SUBMITTED) {
                Console.log("订单不存在或已处理：{}", no);
            } else if (status != 1) {
                log.error("qepay代付失败，no：{}", no);
                userTransferService.payError(record.getId(), result.getTradeNo(), result.getRespCode());
            } else {
                try {
                    userTransferService.paySuccess(record.getId(), result.getTradeNo());
                    log.info("*******************************************qepay代付成功");
                } catch (Exception e) {
                    log.error(_MER_CODE + "代付成功，处理失败，no：{} - {}", no, e.getMessage());
                }
            }
            return "success";
        } else {
            log.error(_MER_CODE + "代付验签失败：{}", no);
            return "sign error";
        }
    }

    @SneakyThrows
    private String getSign(Map<String, Object> data, String key) {
        data.remove("sign_type");
        data.remove("signType");
        //签名规则：（请使⽤md5加密）
        //设所有发送或者接收到的数据为集合 M(sign除外)，将集合 M 内⾮空参 数值的参数按照参数名 ASCII 码从⼩到⼤排序(字典序)，使⽤ URL 键 值对的格式(即 key1=value1&key2=value2...)拼接成字符串。最后拼接秘 钥 （&key=秘钥）
        log.error(_MER_CODE + "待签名参数：{}", data);
        String sign = SignatureUtil.generateSign(data, key);
        log.error(_MER_CODE + "签名结果：{}", sign);
        return sign;
    }

}
