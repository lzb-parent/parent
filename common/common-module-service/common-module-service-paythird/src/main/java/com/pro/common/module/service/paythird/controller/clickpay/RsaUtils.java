package com.pro.common.module.service.paythird.controller.clickpay;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public class RsaUtils {

    /**
     * 验证签名
     */
    public static boolean verifySign(String srcStr, String sign, String publicKey) {
        String decryptSign = "";
        try {
            decryptSign = publicDecrypt(sign, getPublicKey(publicKey));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("公钥解密时出现异常:{}", e.getMessage());
        }
        return srcStr.equalsIgnoreCase(decryptSign);
    }

    /**
     * 得到公钥
     *
     * @param publicKey 密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static RSAPublicKey getPublicKey(String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //通过X509编码的Key指令获得公钥对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(publicKey));
        RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(x509KeySpec);
        return key;
    }

    /**
     * 公钥解密
     *
     * @param data
     * @param publicKey
     * @return
     */
    public static String publicDecrypt(String data, RSAPublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            return new String(rsaSplitCodec(cipher, Cipher.DECRYPT_MODE, java.util.Base64.getDecoder().decode(data), publicKey.getModulus().bitLength()), "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("解密字符串[" + data + "]时遇到异常", e);
        }
    }

    @SneakyThrows
    private static byte[] rsaSplitCodec(Cipher cipher, int opmode, byte[] datas, int keySize) {
        int maxBlock = 0;
        if (opmode == Cipher.DECRYPT_MODE) {
            maxBlock = keySize / 8;
        } else {
            maxBlock = keySize / 8 - 11;
        }
        @Cleanup ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] buff;
        int i = 0;
//        try{
        while (datas.length > offSet) {
            if (datas.length - offSet > maxBlock) {
                buff = cipher.doFinal(datas, offSet, maxBlock);
            } else {
                buff = cipher.doFinal(datas, offSet, datas.length - offSet);
            }
            out.write(buff, 0, buff.length);
            i++;
            offSet = i * maxBlock;
        }
//        }catch(Exception e){
//            throw new RuntimeException("加解密阀值为["+maxBlock+"]的数据时发生异常", e);
//        }
        //        IOUtils.closeQuietly(out);
        return out.toByteArray();
    }
}
