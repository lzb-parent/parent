//package com.pro.common.module.service.message.util;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.pro.common.enums.EnumDict;
//import com.pro.common.service.AuthDictService;
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.autoconfigure.mail.MailProperties;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.mail.MailException;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.JavaMailSenderImpl;
//import org.springframework.mail.javamail.MimeMessagePreparator;
//
//import java.io.InputStream;
//import java.util.Map;
//import java.util.Properties;
//
//@Configuration(proxyBeanMethods = false)
//@EnableConfigurationProperties(MailProperties.class)
//@DependsOn("springContextUtils")
//@Slf4j
//public class EmailConfig {
//
//    @Bean
//    JavaMailSenderImpl mailSender(MailProperties properties, AuthDictService dictService) {
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//
//        return updateSender(sender, properties,dictService);
//    }
//
//    public static JavaMailSenderImpl updateSender(JavaMailSenderImpl sender, MailProperties properties, AuthDictService dictService) {
//        log.info("更新邮件配置");
//        properties.setHost(ObjectUtil.defaultIfBlank(EnumDict.EMAIL_HOST.getValueCache(), properties.getHost()));
//        properties.setPort(ObjectUtil.defaultIfNull(EnumDict.EMAIL_PORT.getInteger(), properties.getPort()));
//        properties.setProtocol(ObjectUtil.defaultIfBlank(EnumDict.EMAIL_PROTOCOL.getValueCache(), properties.getProtocol()));
//        properties.setUsername(ObjectUtil.defaultIfBlank(EnumDict.EMAIL_USERNAME.getValueCache(), properties.getUsername()));
//        properties.setPassword(ObjectUtil.defaultIfBlank(EnumDict.EMAIL_PASSWORD.getValueCache(), properties.getPassword()));
//        properties.getProperties().put("mail.smtp.starttls.enable", "true");
//        applyProperties(properties, sender);
//        return sender;
//    }
//
//    @Data
//    public static class JavaMailSenderImplRefresh implements JavaMailSender {
//        public JavaMailSenderImplRefresh() {
//            sender = new JavaMailSenderImpl();
//        }
//
//        JavaMailSenderImpl sender;
//
//        @Override
//        public javax.mail.internet.MimeMessage createMimeMessage() {
//            return sender.createMimeMessage();
//        }
//
//        @Override
//        public javax.mail.internet.MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
//            return sender.createMimeMessage(contentStream);
//        }
//
//        @Override
//        public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
//            sender.send(mimeMessagePreparator);
//        }
//
//        @Override
//        public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
//            sender.send(mimeMessagePreparators);
//        }
//
//        @Override
//        public void send(javax.mail.internet.MimeMessage... mimeMessages) throws MailException {
//            sender.send(mimeMessages);
//        }
//
//        @Override
//        public void send(javax.mail.internet.MimeMessage mimeMessage) throws MailException {
//            sender.send(mimeMessage);
//        }
//
//        @Override
//        public void send(SimpleMailMessage simpleMessage) throws MailException {
//            sender.send(simpleMessage);
//        }
//
//        @Override
//        public void send(SimpleMailMessage... simpleMessages) throws MailException {
//            sender.send(simpleMessages);
//        }
//    }
//
//    private static void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
//        sender.setHost(properties.getHost());
//        if (properties.getPort() != null) {
//            sender.setPort(properties.getPort());
//        }
//
//        sender.setUsername(properties.getUsername());
//        sender.setPassword(properties.getPassword());
//        sender.setProtocol(properties.getProtocol());
//        if (properties.getDefaultEncoding() != null) {
//            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
//        }
//
//        if (!properties.getProperties().isEmpty()) {
//            sender.setJavaMailProperties(asProperties(properties.getProperties()));
//        }
//    }
//
//    private static Properties asProperties(Map<String, String> source) {
//        Properties properties = new Properties();
//        properties.putAll(source);
//        return properties;
//    }
//}
