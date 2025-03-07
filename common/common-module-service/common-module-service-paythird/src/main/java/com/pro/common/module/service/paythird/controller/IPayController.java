package com.pro.common.module.service.paythird.controller;

import cn.hutool.core.util.TypeUtil;
import com.pro.common.module.api.pay.model.db.UserRecharge;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.service.pay.model.request.UnifiedOrderRequest;
import com.pro.common.module.service.pay.model.request.UserTransferRequest;
import com.pro.common.modules.api.dependencies.R;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.StrUtils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

public interface IPayController<RechargeResult, WithdrawResult> {

    @Operation(summary = "下单")
    @GetMapping("unifiedOrder")
    UserRecharge unifiedOrder(@PathVariable String MER_CODE, UnifiedOrderRequest requestVo, HttpServletRequest request, HttpServletResponse response) throws IOException;

    @Operation(summary = "支付回调")// 旧的灵活方式
    default String notify(
            @PathVariable String MER_CODE,
            RechargeResult result,
            HttpServletRequest request) {
        return "empty";
    }

    @Operation(summary = "支付回调")// 新的统一方式
    @RequestMapping(value = "notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    default String notify(
            @PathVariable String MER_CODE,
            @RequestParam(required = false) Map<String, Object> map,
            @RequestBody(required = false) String requestBody,
            HttpServletRequest request) {
        if (StrUtils.isBlank(requestBody)) {
            requestBody = JSONUtils.toString(map);
        }
        Class<RechargeResult> typeArgument = (Class<RechargeResult>) TypeUtil.getTypeArgument(getClass(), 0);
        RechargeResult rechargeResult = JSONUtils.fromString(requestBody, typeArgument);
        return notify(MER_CODE, rechargeResult, request);
    }

    @Operation(summary = "代付")
    @PostMapping("payout")
    R<PayoutIO.Result> payout(@PathVariable String MER_CODE, @RequestBody UserTransferRequest requestVo, HttpServletRequest request, HttpServletResponse response);

    @Operation(summary = "代付回调")// 旧的灵活方式
//    @RequestMapping(value = "payout/notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    default String payoutNotify(
            @PathVariable String MER_CODE,
            WithdrawResult result,
            HttpServletRequest request) {
        return "empty";
    }

    @Operation(summary = "代付回调")// 新的统一方式
    @RequestMapping(value = "payout/notify", produces = "application/json;charset=UTF-8", method = {RequestMethod.GET, RequestMethod.POST})
    default String payoutNotify(
            @PathVariable String MER_CODE,
            @RequestParam(required = false) Map<String, Object> map,
            @RequestBody(required = false) String requestBody,
            HttpServletRequest request) {
        if (StrUtils.isBlank(requestBody)) {
            requestBody = JSONUtils.toString(map);
        }
        Class<WithdrawResult> typeArgument = (Class<WithdrawResult>) TypeUtil.getTypeArgument(getClass(), 1);
        WithdrawResult withdrawResult = JSONUtils.fromString(requestBody, typeArgument);
        return payoutNotify(MER_CODE, withdrawResult, request);
    }
}
