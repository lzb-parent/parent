//package com.pro.common.module.service.paythird.controller.clickpay;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.security.KeyFactory;
//import java.security.PublicKey;
//import java.security.spec.X509EncodedKeySpec;
//import java.util.Base64;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import javax.crypto.Cipher;
//
//import com.google.gson.Gson;
//
//public class Main {
//    public static void main(String[] args) {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//            String jsonInput = reader.lines().collect(Collectors.joining());
//            Gson gson = new Gson();
//            Map<String, Object> res = gson.fromJson(jsonInput, HashMap.class);
//
//            String platSign = (String) res.remove("platSign");
//
//            String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDFJ/AmUV4Z8udG8aOBUt/kEwc/DbxF5Gtfw6Y00NHQ4Pz2X2x9IxjUZxn2dnFxmrmhqKNlfwXOqyejhBzi0pSHyGoI4XP9IEfZGO6YkSb9DCY1ZxX8fDl2G+tPCbWYTVO4JutFmzTWgk1Uhhu6L9dlOMUHvZf3/6czA/a9C7azXwIDAQAB";
//            String decryptSign = publicKeyDecrypt(platSign, publicKeyStr);
//
//            Map<String, Object> params = new HashMap<>(res);
//            Map<String, Object> sortedParams = params.entrySet()
//                    .stream()
//                    .sorted(Map.Entry.comparingByKey())
//                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
//
//            StringBuilder paramsStr = new StringBuilder();
//            for (Map.Entry<String, Object> entry : sortedParams.entrySet()) {
//                paramsStr.append(entry.getValue());
//            }
//
//            if (paramsStr.toString().equals(decryptSign)) {
//                if ("2".equals(res.get("status"))) {
//                    System.out.println("success");
//                } else {
//                    System.out.println("fail");
//                }
//            } else {
//                System.out.println("fail");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String publicKeyDecrypt(String data, String publicKeyStr) throws Exception {
//        byte[] keyBytes = Base64.getDecoder().decode(publicKeyStr);
//        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
//        PublicKey publicKey = keyFactory.generatePublic(spec);
//
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, publicKey);
//
//        byte[] dataBytes = Base64.getDecoder().decode(data);
//        StringBuilder decryptedData = new StringBuilder();
//
//        for (int i = 0; i < dataBytes.length; i += 128) {
//            int chunkSize = Math.min(128, dataBytes.length - i);
//            byte[] chunk = new byte[chunkSize];
//            System.arraycopy(dataBytes, i, chunk, 0, chunkSize);
//            byte[] decryptedChunk = cipher.doFinal(chunk);
//            decryptedData.append(new String(decryptedChunk));
//        }
//
//        return decryptedData.toString();
//    }
//}
