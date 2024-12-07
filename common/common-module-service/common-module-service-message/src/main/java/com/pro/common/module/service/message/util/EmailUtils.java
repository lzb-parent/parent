//package com.pro.common.module.service.message.util;
//
//import com.pro.base.exception.BusinessException;
//import com.pro.common.enums.EnumDict;
//import com.pro.common.utils.I18nUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Component;
//
///**
// * 小船出海短信
// */
//@Slf4j
//@Component
//public class EmailUtils {
//
//    private static JavaMailSender emailSender;
//    @Autowired(required = false)
//    public void setEmailSender(JavaMailSender emailSender) {
//        EmailUtils.emailSender = emailSender;
//    }
//
//    public static void sendEmail(String toEmail, String title, String content) {
//        if (!EnumDict.EMAIL_OPEN.getBoolean()) {
//            throw new BusinessException(I18nUtils.get("邮件服务未启用"));
//        }
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject(title);
//        message.setText(content);
//        if (emailSender == null) {
//            throw new BusinessException(I18nUtils.get("邮件配置异常"));
//        }
//        emailSender.send(message);
//    }
//
//}
