package com.pro.common.module.service.message.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.service.message.service.SysMsgChannelMerchantService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.modules.service.dependencies.util.SpringContextUtils;
import com.pro.framework.api.IReloadService;
import com.pro.framework.api.util.AssertUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 小船出海短信
 */
@Slf4j
@Component
public class EmailService implements IReloadService {
    //    @Autowired(required = false)
//    private EmailService INSTANT;
    private Map<Long, JavaMailSender> javaMailSenderMap = new ConcurrentHashMap<>();
    @Autowired
    private SysMsgChannelMerchantService sysMsgChannelMerchantService;


    public void sendEmail(SysMsgChannelMerchant merchant, String toEmail, String title, String content) {
        JavaMailSender emailSender = this.getEmailSender(merchant);
//        AssertUtil.notEmpty(emailSender, "邮件未配置");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(title);
        message.setText(content);
        emailSender.send(message);
    }


    //    @Cacheable(CommonConst.CacheKey.EmailSender)
    public JavaMailSender getEmailSender(SysMsgChannelMerchant merchant) {
        return javaMailSenderMap.getOrDefault(merchant.getId(), this.loadEmailSender(merchant));
    }

    private JavaMailSender loadEmailSender(SysMsgChannelMerchant merchant) {
//        AssertUtil.notEmpty(merchant, "邮件未配置");
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        SysMsgChannelMerchant param = new SysMsgChannelMerchant();
        param.setChannelType(EnumSysMsgChannelType.EMAIL);
        param.setEnabled(true);
//        SysMsgChannelMerchant merchant = sysMsgChannelMerchantService.getById(merchantId);
//        SysMsgChannelMerchant merchant = sysMsgChannelMerchantService.getFirst(param, SysMsgChannelMerchant::getSort);
//        {
//            "protocol":"smtp.gmail.com",
//            "host":"465",
//            "port":"smtp"
//        }
        JSONObject cfg = JSONUtil.parseObj(merchant.getOtherJson());
        // 设置邮件服务器属性
        sender.setProtocol(cfg.getStr("protocol"));
        sender.setHost(cfg.getStr("host"));
        sender.setPort(cfg.getInt("port"));
        sender.setUsername(merchant.getApiKey());
        sender.setPassword(merchant.getApiPwd());

        sender.setDefaultEncoding("UTF-8");
        // 设置其他属性
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.starttls.enable", "true");
        sender.setJavaMailProperties(javaMailProperties);
        return sender;
    }

    @Override
    public void reload() {
        javaMailSenderMap.clear();
    }
}
