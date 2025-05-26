package com.pro.common.module.service.paythird.controller.clickpay;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.http.ContentType;
import com.alibaba.fastjson.JSONObject;
import com.pro.common.module.api.pay.model.db.PayMerchant;
import com.pro.common.module.api.pay.model.dto.PayoutIO;
import com.pro.common.module.service.pay.service.PayMerchantService;
import com.pro.common.module.service.paythird.controller.common.NotifyIO;
import com.pro.common.module.service.paythird.controller.common.PayCommonController;
import com.pro.common.module.service.paythird.controller.common.PayoutNotifyIO;
import com.pro.common.module.service.paythird.controller.common.UnifiedOrderIO;
import com.pro.common.module.service.paythird.util.SignatureUtil;
import com.pro.framework.api.util.LogicUtils;
import com.pro.framework.api.util.StrUtils;
import io.swagger.annotations.Api;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.math.RoundingMode;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Slf4j
@Getter
@RestController
@Api(tags = "clickpay支付")
@RequestMapping("pay/clickpay/{MER_CODE}")
public class ClickPayController extends PayCommonController {
    private static final String MAIL = "click@gmail.com";
    private static final String BANK_NAME = "Bank Name";
    private static final String PHONE = "10000000";
    // http://localhost:8088/pay/clickpay/CLICKPAY_CLICKPAY/unifiedOrder?amount=10000&payType=UPI&returnUrl=http://www.baidu.com&userId=1


    //通道编号
    private String merCode = "CLICKPAY";

    // 使用率 JSON > FORM_URLENCODED (表单)
    private ContentType postContentType = ContentType.JSON;
    @Autowired
    private PayMerchantService merchantService;


    @Override
    //代收下单签名：将所有非空参数字段按 ASCII 升序排序。按键的顺序连接值以生成字符串StringA。
    //StringA使用商户私钥（ ）通过RSA加密queryString获取签名值sign。
    public String buildSign(Map<String, Object> params, String key, String signName) {
        params.values().removeIf(StrUtils::empty);
        params.remove("sign");
        String signString = SignatureUtil.sortParamValue(params);
        Console.log("待签名参数：{}", signString);
        Console.log("私钥key：{}", key);
        String sign = RsaUtils.encryptByPrivate(signString, key);
        Console.log("签名：{}", sign);
        return sign;
    }

    // 代收回调验证签名

    @Override
    public boolean checkSignNotify(Map<String, Object> map, JSONObject jo, String key, String mchKey, String signName, HttpServletRequest request, PayMerchant merchant) {
        map.values().removeIf(StrUtils::empty);
        String sign = (String) map.get("platSign");
        log.info("sign：{}", sign);
        map.remove("platSign");
        String srcStr = SignatureUtil.sortParamValue(map);
        log.info("原始串：{}", srcStr);
        // 这里是merchant.getPlatPublicKey()是公钥
        log.info("公钥: {}", merchant.getPlatPublicKey());
        return RsaUtils.verifySign(srcStr, sign, merchant.getPlatPublicKey());
    }


    @Override
    protected boolean checkPayoutSignNotify(Map<String, Object> map, JSONObject jo, String mchKey, String signName, HttpServletRequest request, PayMerchant merchant) {
        map.values().removeIf(StrUtils::empty);
        String sign = (String) map.get("platSign");
        log.info("sign：{}", sign);
        map.remove("platSign");
        String srcStr = SignatureUtil.sortParamValue(map);
        log.info("原始串：{}", srcStr);
        log.info("key: {}", merchant.getPlatPublicKey());
        // 这里merchant.getPlatPublicKey()是公钥
        log.info("公钥: {}", merchant.getPlatPublicKey());
        return RsaUtils.verifySignByPub(srcStr, sign, merchant.getPlatPublicKey());
    }

    //代收下单 构造参数
    @Override
    public Map<String, Object> unifiedOrderBuildParams(UnifiedOrderIO.Params params) {
        return MapBuilder.<String, Object>create()
                .put("merchantCode", params.getMchId())//商户编码
                .put("method", params.getPayType())//支付类型编码
                .put("orderNum", params.getNo())//商户订单号
                .put("payMoney", params.getMoney().setScale(2, RoundingMode.UP).toString())
                .put("productDetail", "clickpay")
                .put("name", params.getUsername())
                .put("email", params.getEmail())
                .put("currency", params.getCountryCurrency())//币种编码
                .put("notifyUrl", params.getNotifyUrl().trim())//通知地址
                .put("redirectUrl", params.getReturnUrl())//支付成功跳转地址
                .put("phone", params.getPhone())
                .put("dateTime", params.getNow_yyyyMMddHHmmss())
                .build()
                ;
    }

    //代收下单 返回转化
    @Override
    public UnifiedOrderIO.Result unifiedOrderReadResult(JSONObject result) {
        boolean success = "SUCCESS".equals(result.getString("platRespCode"));
        return UnifiedOrderIO.Result.builder()
                .success(success)
                .errorMsg(result.getString("platRespMessage"))
                .payUrl(success ? result.getString("payData") : null)//支付链接
                .build();
    }

    //代收通知 参数转化
    @Override
    public NotifyIO.Params notifyReadData(JSONObject data) {
        String status = data.getString("code");
        return NotifyIO.Params.builder()
                .statusSuccess("00".equals(status))
                .no(data.getString("orderNum"))//商户订单号
                .payNo(data.getString("platOrderNum"))//三方订单号
                .amount(data.getString("payMoney"))//实付金额
                .build()
                ;
    }

    //代收通知 返回
    @Override
    public String notifyBuildReturn(Boolean success) {
        return success ? "SUCCESS" : "FAIL";
    }


    //代付下单 构造参数
    @Override
    public Map<String, Object> payoutBuildParams(PayoutIO.Params params) {
        return MapBuilder.<String, Object>create()
                .put("merchantCode", params.getMchId())//商户编码
                .put("orderNum", params.getNo())//商户订单号
                .put("money", params.getAmount().setScale(2, RoundingMode.DOWN).toString())
                .put("feeType", params.getPayType())
                .put("dateTime", params.getNow_yyyyMMddHHmmss())
                .put("name", params.getBankUsername())
                .put("number", StrUtils.or(params.getBankAccount(), params.getUpi()))
                .put("bankLinked", params.getIfsc())
                .put("bankCode", "IFSC")
                .put("bankName", LogicUtils.or(params.getBankName(), BANK_NAME))
                .put("accountEmail", LogicUtils.or(params.getEmail(), MAIL))
                .put("accountMobile", LogicUtils.or(params.getBankPhone(), PHONE))
                .put("description", "payout")
                .put("currency", params.getCountryCurrency())//币种编码
                .put("notifyUrl", params.getNotifyUrl().trim())//通知地址
                .build();
    }

    @Override
    protected String buildPayoutSignPost(Map<String, Object> createMap, String merKey, String signName) {
        List<String> paramNameList = new ArrayList<>();
        for (String key : createMap.keySet()) {
            paramNameList.add(key);
        }
        Collections.sort(paramNameList);  // 排序key
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < paramNameList.size(); i++) {
            String key = paramNameList.get(i);
            stringBuilder.append(createMap.get(key));  // 拼接参数
        }
        String keyStr = stringBuilder.toString();  // 得到待加密的字符串
        System.out.println("keyStr:" + keyStr);
        String signedStr = "";
        try {
            signedStr = privateEncrypt(keyStr, getPrivateKey(merKey));  // 私钥加密
        } catch (Exception e) {
            System.out.println(e);
        }
        return signedStr;
//        return super.buildPayoutSignPost(createMap, key, signName);
    }

//    /**
//     * 得到私钥
//     *
//     * @param privateKey 密钥字符串（经过base64编码）
//     * @throws Exception
//     */
//    public static RSAPrivateKey getPrivateKey(String privateKey) {
//        //通过PKCS#8编码的Key指令获得私钥对象
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(privateKey));
//        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
//        return key;
//    }
    /**
     * 得到私钥
     *
     * @param privateKey 密钥字符串（经过base64编码）
     * @return RSAPrivateKey 私钥对象
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws Exception {
        // 通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(privateKey));
        return (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
    }

    /**
     * 私钥加密
     *
     * @param data
     * @param privateKey
     * @return
     */

    public static String privateEncrypt(String data, RSAPrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return java.util.Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes("UTF-8"), privateKey.getModulus().bitLength()));
        } catch (Exception e) {
            throw new RuntimeException("加密字符串[" + data + "]时遇到异常", e);
        }
    }

    //    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize){
//        int maxBlock = 0;
//        if(opmode == Cipher.DECRYPT_MODE){
//            maxBlock = keySize / 8;
//        }else{
//            maxBlock = keySize / 8 - 11;
//        }
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        int offSet = 0;
//        byte[] buff;
//        int i = 0;
//        try{
//            while(datas.length > offSet){
//                if(datas.length-offSet > maxBlock){
//                    buff = cipher.doFinal(datas, offSet, maxBlock);
//                }else{
//                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
//                }
//                out.write(buff, 0, buff.length);
//                i++;
//                offSet = i * maxBlock;
//            }
//        }catch(Exception e){
//            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
//        }
//        byte[] resultDatas = out.toByteArray();
//        IOUtils.closeQuietly(out);
//        return resultDatas;
//    }
    public static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = (opmode == Cipher.DECRYPT_MODE) ? (keySize / 8) : (keySize / 8 - 11);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offSet = 0;
            byte[] buff;
            while (datas.length > offSet) {
                int blockSize = Math.min(datas.length - offSet, maxBlock);
                buff = cipher.doFinal(datas, offSet, blockSize);
                out.write(buff, 0, buff.length);
                offSet += blockSize;
            }
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("加解密阀值为[" + maxBlock + "]的数据时发生异常", e);
        }
    }

    //代付下单 返回转化
    @Override
    public PayoutIO.Result payoutReadResult(JSONObject result) {
        boolean success = "SUCCESS".equals(result.getString("platRespCode"));
        return PayoutIO.Result.builder()
                .success(success)
                .errorMsg(result.getString("platRespMessage"))
                .build();
    }

    //代付通知 参数转化
    @Override
    public PayoutNotifyIO.Params payoutNotifyReadData(JSONObject data) {
        String status = data.getString("status");
        PayoutNotifyIO.Params params = PayoutNotifyIO.Params.builder()
                .no(data.getString("orderNum"))//商户订单号
                .amount(data.getString("money"))
                .statusSuccess("2".equals(status))
                .statusFail(!"2".equals(status))
                .payNo(data.getString("platOrderNum"))//三方订单号
                .message(data.getString("statusMsg"))//三方订单号
                .build();
        return params;
    }

    //代付通知 返回
    @Override
    public String payoutNotifyBuildReturn(Boolean success) {
        return success ? "success" : "fail";
    }
}
