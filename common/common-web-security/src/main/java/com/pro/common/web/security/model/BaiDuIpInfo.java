//package com.pro.common.web.security.model;
//
//import com.pro.common.modules.service.dependencies.util.IPUtils;
//import lombok.Data;
//
//import java.util.List;
//
//@Data
//public class BaiDuIpInfo {
//
//    List<LocationInfo> data;
//    Integer status;
//
//    public String getCountry() {
//        return isChina() ? "中国" : getLocation();
//    }
//
//    public String getProvince() {
//        if (isChina()) {
//            String location = getLocation();
//            if (location.contains("省")) {
//                return location.substring(0, location.indexOf("省") + 1);
//            }
//        }
//        return "";
//    }
//
//    public String getCity() {
//        if (isChina()) {
//            String location = getLocation();
//            if (location.contains("省") && location.contains("市")) {
//                return location.substring(location.indexOf("省") + 1, location.indexOf("市") + 1);
//            }
//        }
//        return "";
//    }
//
//    public String getDistrict() {
//        return "";
//    }
//
//    public boolean isChina() {
//        try {
//            String location = getLocation();
//            return IPUtils.provinces_extra.stream().noneMatch(location::contains)
//                    && IPUtils.provinces.stream().anyMatch(location::contains);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    private String getLocation() {
//        if (null != data && !data.isEmpty()) {
//            LocationInfo info = data.get(0);
//            if (null != info) {
//                return info.getLocation();
//            }
//        }
//        return "";
//    }
//
//    public String address() {
//        return getCountry() + getProvince() + getCity() + getDistrict();
//    }
//
//    @Data
//    class LocationInfo {
//        String location;
//    }
//}
