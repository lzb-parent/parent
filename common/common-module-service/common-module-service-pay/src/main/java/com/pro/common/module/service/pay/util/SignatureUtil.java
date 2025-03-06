package com.pro.common.module.service.pay.util;

import cn.hutool.core.lang.Console;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 签名工具类
 */
public class SignatureUtil {

    /**
     * 生成sign，MD5 签名
     */
    public static String generateLinkedSign(LinkedHashMap<String, Object> map, String paternerKey) throws Exception {
        return generateSign(map, null, paternerKey, "key", false);
    }
    public static String generateSign(Map<String, Object> map, String paternerKey) throws Exception {
        return generateSign(map, null, paternerKey, "key", true);
    }
    public static String generateSignWithKeyName(Map<String, Object> map, String paternerKey, String keyName) throws Exception {
        return generateSign(map, null, paternerKey, keyName, true);
    }
    public static String generateSign(Map<String, Object> map, String signType, String paternerKey) throws Exception {
        return generateSign(map, signType, paternerKey, "key", true);
    }
    public static String generateSign(Map<String, Object> map, String sign_type, String paternerKey, String keyName, boolean isNeedSorted) throws Exception {
        Map<String, Object> tmap = isNeedSorted ? MapUtil.sort(map) : map;
        tmap.remove("sign");
        if(sign_type == null){
            sign_type = (String) tmap.get("sign_type");
        }
        String str = MapUtil.joinIgnoreNull(tmap, "&", "=");
        if ("SHA1".equalsIgnoreCase(sign_type)){
            String signStr = str+"&"+keyName+"="+paternerKey;
            Console.log("SHA1=>签名串：{}", signStr);
            return SecureUtil.sha1(signStr).toUpperCase();
        }else if ("SHA256".equalsIgnoreCase(sign_type)){
            String signStr = str+paternerKey;
            Console.log("SHA256=>签名串：{}", signStr);
            return SecureUtil.sha256(signStr);
        } else {//default MD5
            String signStr = str+"&"+keyName+"="+paternerKey;
            Console.log("MD5=>签名串：{}", signStr);
            return SecureUtil.md5(signStr);
        }
    }


    /**
     * mch 支付、代扣API调用签名验证
     *
     * @param map 参与签名的参数
     * @param key mch key
     * @return boolean
     */
    public static boolean validateSign(Map<String, Object> map, String key) throws Exception {
        if(map.get("sign") == null){
            return false;
        }
        String sign = generateSign(map, key);
        Console.log("本地签名sign：{}", sign);
        return ((String)map.get("sign")).equalsIgnoreCase(sign);
    }

    public static Map<String, Object> json2Map(JSONObject jo) {
        return jo.keySet().stream().collect(HashMap::new, (map, key) -> {
                Object value = jo.get(key);
                if (!StringUtils.isEmpty(value)) {
                    map.put(key, value);
                }
            }, HashMap::putAll);
    }
    public static Map<String, Object> object2Map(Object o) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(o.getClass()))
                .filter(pd -> !"class".equals(pd.getName()))
                .collect(HashMap::new, (map, pd) -> {
                    Object value = ReflectionUtils.invokeMethod(pd.getReadMethod(), o);
                    if (!StringUtils.isEmpty(value)) {
                        map.put(pd.getName(), value);
                    }
                }, HashMap::putAll);
    }
    public static Map<String, Object> object2MapWithNull(Object o) {
        return Arrays.stream(BeanUtils.getPropertyDescriptors(o.getClass()))
                .filter(pd -> !"class".equals(pd.getName()))
                .collect(HashMap::new, (map, pd) -> {
                    Object value = ReflectionUtils.invokeMethod(pd.getReadMethod(), o);
                    map.put(pd.getName(), value);
                }, HashMap::putAll);
    }

    public static String sortParam(Map<String, Object> map) {
        Map<String, Object> tmap = MapUtil.sort(map);
        tmap.remove("sign");
        String str = MapUtil.joinIgnoreNull(tmap, "&", "=");
        Console.log("排序后参数：{}", str);
        return str;
    }
    public static String sortParamWithNull(Map<String, Object> map) {
        Map<String, Object> tmap = MapUtil.sort(map);
        tmap.remove("sign");
        String str = MapUtil.join(tmap, "&", "=");
        Console.log("排序后参数：{}", str);
        return str;
    }

    public static Map<String, String> getParamsFromFormDataByNames(HttpServletRequest request){
        Map<String, String> map = new LinkedHashMap<>();
        Enumeration<String> er = request.getParameterNames();
        while (er.hasMoreElements()) {
            String name = er.nextElement();
            String value = request.getParameter(name);
            map.put(name, value);
        }
        return map;
    }
    public static String sortParamValue(Map<String, Object> map) {
        Map<String, Object> tmap = MapUtil.sort(map);
        Console.log("排序后参数：{}", tmap);
        tmap.remove("sign");
        StringBuilder str = new StringBuilder();
        for (Object value : tmap.values()) {
            if (!StringUtils.isEmpty(value)){
                str.append(value);
            }
        }
        Console.log("排序后拼接value：{}", str.toString());
        return str.toString();
    }
}
