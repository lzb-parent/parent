package com.pro.common.modules.service.dependencies.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpUtil;
import com.pro.framework.api.util.JSONUtils;
import com.pro.framework.api.util.StrUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class IPUtils {
    public static List<String> provinces_extra = Arrays.asList("香港", "澳门", "台湾");
    public static List<String> provinces = Arrays.asList("中国", "北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆");

    public static final String getIpAddress(HttpServletRequest request) {

        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            if (ip == null) {
                ip = request.getRemoteAddr();
            }
        }
        return ip;
    }

    /**
     * 百度ip地址信息
     */
    public static BaiDuIpInfo getBaiDuIpAddressInfo(String ip) {
        log.debug("get ip info==>{}", ip);
        String url = "https://sp0.baidu.com/8aQDcjqpAAV3otqbppnN2DJv/api.php?query=" + ip + "&resource_id=6006";
        try {
            String result = HttpUtil.get(url, 2000);
            if (StrUtils.isNotBlank(result)) {
                return JSONUtils.fromString(result, BaiDuIpInfo.class);
            }
        } catch (Exception e) {
            // 基本就是超时
            log.error("get ip error: {}", e.getMessage());
//            e.printStackTrace();
        }
        return null;
    }


//    public static IPAddressInfo getIpInfo(String ip) {
//        log.debug("get ip info==>{}", ip);
//        try {
//            IPInfo ipInfo = IPInfoUtils.getIpInfo(ip);
//            IPAddressInfo ipAddressInfo = JSONUtils.fromString(ipInfo, IPAddressInfo.class);
//            return ipAddressInfo;
//        } catch (Exception e) {
//            // e.printStackTrace();
//        }
//        return null;
//    }

    @Data
    public static class BaiDuIpInfo {

        List<LocationInfo> data;
        Integer status;

        public String getCountry() {
            return isChina() ? "中国" : getLocation();
        }

        public String getProvince() {
            if (isChina()) {
                String location = getLocation();
                if (location.contains("省")) {
                    return location.substring(0, location.indexOf("省") + 1);
                }
            }
            return "";
        }

        public String getCity() {
            if (isChina()) {
                String location = getLocation();
                if (location.contains("省") && location.contains("市")) {
                    return location.substring(location.indexOf("省") + 1, location.indexOf("市") + 1);
                }
            }
            return "";
        }

        public String getDistrict() {
            return "";
        }

        public boolean isChina() {
            try {
                String location = getLocation();
                return IPUtils.provinces_extra.stream().noneMatch(location::contains)
                        && IPUtils.provinces.stream().anyMatch(location::contains);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        private String getLocation() {
            if (CollUtil.isNotEmpty(data)) {
                LocationInfo info = data.get(0);
                if (null != info) {
                    return info.getLocation();
                }
            }
            return "";
        }

        public String address() {
            return getCountry() + getProvince() + getCity() + getDistrict();
        }

        @Data
        class LocationInfo {
            String location;
        }
    }

    @Data
    public static class IPAddressInfo {
        private String country;
        private String province;
        private String address;
        private String isp;
        private boolean overseas;
        private double lat;
        private double lng;


        public boolean isChina() {
            try {
                String location = getProvince();
                return IPUtils.provinces_extra.stream().noneMatch(location::contains)
                        && IPUtils.provinces.stream().anyMatch(location::contains);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        public String getAddressInfo() {
            return getCountry() + getProvince() + getIsp() + "(" + getLng() + "," + getLat() + ")";
        }

    }


}

