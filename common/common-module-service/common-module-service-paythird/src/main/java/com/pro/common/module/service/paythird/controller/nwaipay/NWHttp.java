package com.pro.common.module.service.paythird.controller.nwaipay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class NWHttp {
    public static String Get(String url, Map<String,Object> payload) throws IOException {
        String getParam = payload
                .keySet()
                .stream()
                .map(key -> key + "=" + URLEncoder.encode(String.valueOf(payload.get(key)), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        if (!getParam.isEmpty()){
            getParam = "?"+getParam;
        }
        URL obj = new URL(url+getParam);
//        System.out.println(url+getParam);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", "NWHttpClient");
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        StringBuffer response = new StringBuffer();
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
        }finally {
            in.close();
        }
        return  response.toString();
    }
}
