package com.pro.common.module.service.paythird.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static final Logger log = LoggerFactory.getLogger(RSAUtils.class);

    //加密算法RSA
    private static final String KEY_ALGORITHM = "RSA";
    //RSA最大加密明文大小
    private static final int MAX_ENCRYPT_BLOCK = 117;
    //RSA最大解密密文大小
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static final String CHARSET = "UTF-8";

    public static Map<String,String> genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024);
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(java.util.Base64.getEncoder().encode(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(java.util.Base64.getEncoder().encode((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        Map<String,String> retMap = new HashMap<>();
        retMap.put("pubKey",publicKeyString);
        retMap.put("priKey",privateKeyString);
        return retMap;
    }

    /**
     * 得到私钥
     * @param privateKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPrivateKey getPrivateKey(String privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过PKCS#8编码的Key指令获得私钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(privateKey));
        RSAPrivateKey key = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8KeySpec);
        return key;
    }

    /**
     * 得到公钥
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     *
     * @param signSrc  源
     * @param cipherText  待解密数据
     * @param publickey   公钥
     * @return
     * @throws Exception
     */
    public static boolean verifySignByPub(String signSrc,String cipherText,String publickey){
        Boolean verifySign = false;
        try {
            String decryptSign = decryptByPublic(cipherText, getPublicKey(publickey));
            if (signSrc.equalsIgnoreCase(decryptSign)) {
                verifySign = true;
            }
        } catch (Exception e){
            log.error("公钥解密时出现异常:{}",e.getMessage());
        }
        return verifySign;
    }

    /**
     * 私钥加密
     * @param data
     * @param privateKey
     * @return
     */
    public static String encryptByPrivate(String data, RSAPrivateKey privateKey){
        try{
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] bytes = rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), privateKey.getModulus().bitLength());
//            Base64.encodeBase64URLSafeString
            return java.util.Base64.getEncoder().encodeToString(bytes);
        }catch(Exception e){
            throw new RuntimeException("私钥加密字符串[" + data + "]时异常", e);
        }
    }

    /**
     * 私钥加密
     * @param data 源
     * @param privateKey 私钥
     * @return
     */
    public static String encryptByPrivate(String data, String privateKey){
        try{
            //获取私钥
            RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);
            return Base64.getEncoder().encodeToString(rsaSplitCodec(cipher, Cipher.ENCRYPT_MODE, data.getBytes(CHARSET), rsaPrivateKey.getModulus().bitLength()));
        }catch(Exception e){
            throw new RuntimeException("私钥加密字符串[" + data + "]时异常：{}", e);
        }
    }

    /**
     * 公钥解密
     * @param data 源
     * @param publicKey 公钥
     * @return
     */
    public static String decryptByPublic(String data, String publicKey){
        try{
            RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            System.out.println(cipher.toString());
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, java.util.Base64.getDecoder().decode(data), rsaPublicKey.getModulus().bitLength()), "UTF-8");
        }catch(Exception e){
            throw new RuntimeException("公钥解密字符串[" + data + "]时异常", e);
        }
    }

    /**
     * 公钥解密
     * @param data
     * @param publicKey
     * @return
     */
    public static String decryptByPublic(String data, RSAPublicKey publicKey){
        try{
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, java.util.Base64.getDecoder().decode(data), publicKey.getModulus().bitLength()), "UTF-8");
        }catch(Exception e){
            throw new RuntimeException("公钥解密字符串[" + data + "]时异常", e);
        }
    }

    /**
     * 数据分段加密
     * @param cipher
     * @param opmode
     * @param datas
     * @param keySize
     * @return
     * @throws Exception
     */
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize)throws Exception{
        int maxBlock = 0;
        if(opmode == Cipher.DECRYPT_MODE){
            maxBlock = keySize / 8;
        }else{
            maxBlock = keySize / 8 - 11;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
        byte[] resultDatas = null;
        try{
            while(datas.length > offSet){
                if(datas.length-offSet > maxBlock){
                    buff = cipher.doFinal(datas, offSet, maxBlock);
                }else{
                    buff = cipher.doFinal(datas, offSet, datas.length-offSet);
                }
                out.write(buff, 0, buff.length);
                i++;
                offSet = i * maxBlock;
            }
            resultDatas = out.toByteArray();
        }catch(Exception e){
            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
        }finally {
            out.close();
        }
        return resultDatas;
    }

    public static void main(String[] args)throws Exception {
        Map<String, String> retMap = RSAUtils.genKeyPair();
        String pubKey = retMap.get("pubKey");
        String priKey = retMap.get("priKey");

//        pubKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC4hvnaYkXJAOzUMPa5aEu8iDFSSsPDW7zX9H6lQ1XZuHxz0pU0xRu/RasE6fGVOZnm9bMWCHdbS11Vb2yTducv6gCxquewn+6i6Ov4JaL0UM8HqV4Ge796H82Nom36qdoFyAYWtRbYx+/9MTgC6dTHeM+SRF97bcirn+IMsPorQQIDAQAB";

//        priKey = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALiG+dpiRckA7NQw9rloS7yIMVJKw8NbvNf0fqVDVdm4fHPSlTTFG79FqwTp8ZU5meb1sxYId1tLXVVvbJN25y/qALGq57Cf7qLo6/glovRQzwepXgZ7v3ofzY2ibfqp2gXIBha1FtjH7/0xOALp1Md4z5JEX3ttyKuf4gyw+itBAgMBAAECgYEApTCICb1B9dAzMz3mEVLRwiQ16xJtL703UChbG70s5KxoFROmaMgvr8pBcDOyBkG7ievYd1f66aqNEkeebmDaUh69uBthXAB1XXJIl2ZeChfgjBVjIUXI9Q2L/MP2mDZME287rirdpa5CUheTUm08rObsmOtAkpcd1p7aFe7fyPECQQDtkGAspY2l0dx5DuU7rnTcCrlqcCrwF2Y3ftnHj4JvKGJLORI7pXJwe6H2I+TfgSEtxC4w9vX1Le6hWkICJ8pdAkEAxtju2jHI+h17gTeGhiXNkRvslUSeKSRDOzsCRnvI6QEW+DUyoPNYfg2/fZJTfYWQCFITSXXxrpZbxfa6tnn+NQJBANMTssA4oLCy1Igjef4bNe3FAZXW7++/eqzYYrvzgNsvMJbRsKeaiyHyV6eRoOwNmeNGKhyLcWJVW2+1+Gxd2mkCQQCklUF9T9+tRWHIHhE2T2vKQ3aaReUy4FTVXY4QkLxGPySn5EA3f7MrAm4QnWCFcFs6x9Q25WxYuRLU09bO2kJRAkAyjl+YXd/6FF2PlIN6Xe1DTsjPnztx9hnEm80URcxofBeIdhk70eqUY8v16Cc6PXUrJuwtRGIBw4ehCZzQVBRY";

        System.out.println("生成公钥\n"+pubKey);
        System.out.println("公钥长度\n"+pubKey.length());

        try {
            RSAPublicKey publicKey = RSAUtils.getPublicKey(pubKey);
            if(publicKey != null){
                System.out.println("校验公钥合法性\n"+true);
            }
        }catch (Exception e){
            System.out.println("校验公钥合法性\n"+false);
        }

        System.out.println("生成私钥\n"+priKey);
        System.out.println("私钥长度\n"+priKey.length());


        String message = "TPB100101VNDVNMgoodsgm7611000000679751617695248072https://google.com100000.00https://google.comtest@gmail.com13122336688zhangsan90m";
        System.out.println("明文\n"+message);
        System.out.println("明文长度\n"+message.length());

        String cipherText = RSAUtils.encryptByPrivate(message, priKey);
        System.out.println("私钥加密后密文\n"+cipherText);
        cipherText = URLDecoder.decode(cipherText, "UTF-8");
        System.out.println("URL解码后"+cipherText+"\n");

        cipherText = URLEncoder.encode(cipherText,"UTF-8");
        System.out.println("URL编码后"+cipherText+"\n");


        String plainText = RSAUtils.decryptByPublic(cipherText, pubKey);
        System.out.println("公钥解密后明文\n"+plainText);

        boolean verifySign = RSAUtils.verifySignByPub(message, cipherText, pubKey);
        System.out.println("验签结果\n"+verifySign);
    }
}
