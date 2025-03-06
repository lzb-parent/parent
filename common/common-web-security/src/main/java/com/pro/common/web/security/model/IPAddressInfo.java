//package com.pro.common.web.security.model;
//
//import com.pro.common.modules.service.dependencies.util.IPUtils;
//import lombok.Data;
//
//@Data
//public class IPAddressInfo {
//    private String country;
//    private String province;
//    private String address;
//    private String isp;
//    private boolean overseas;
//    private double lat;
//    private double lng;
//
//
//    public boolean isChina() {
//        try {
//            String location = getProvince();
//            return IPUtils.provinces_extra.stream().noneMatch(location::contains)
//                    && IPUtils.provinces.stream().anyMatch(location::contains);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public String getAddressInfo() {
//        return getCountry() + getProvince() + getIsp() + "("+getLng()+","+getLat()+")";
//    }
//
//}
