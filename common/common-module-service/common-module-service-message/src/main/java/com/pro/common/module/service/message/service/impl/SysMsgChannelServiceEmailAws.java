//package com.pro.common.module.service.message.service.impl;
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
//import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
//import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
//import com.pro.common.module.service.message.util.AwsMailUtils;
//import com.pro.framework.api.structure.Tuple2;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 小船出海短信
// */
//@Slf4j
//@Service("sysMsgChannelServiceEmailAws")
//public class SysMsgChannelServiceEmailAws implements ISysMsgChannelService {
//
//    // public static final EnumMessageChannel ENUM_MESSAGE_CHANNEL = EnumMessageChannel.EMAIL;
////    @Autowired
////    private TranslationService translationService;
//
//
//    @Override
//    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
//        return accountAndParams.stream().map(accountAndParam -> {
//            Map<String, ?> paramMap = accountAndParam.getT2();
//            String title = StrUtil.format(template.getTitleTemplate(), paramMap);
//            String content = StrUtil.format(template.getContentTemplate(), paramMap);
//            return send(merchant, title, content, accountAndParam.getT1(), paramMap, lang);
//        }).collect(Collectors.toList());
//    }
//
//    private SysMsgRecordSendResult send(SysMsgChannelMerchant merchant, String title, String content, String account, Map<String, ?> paramMap, String lang) {
//        try {
////            String code = ObjectUtil.defaultIfNull(paramMap.get("code"), "").toString();
////            // 匹配文件模版关键字
////            String emailKeywords = EnumDict.EMAIL_TEMPLATE_KEYWORD.getValueCache();
////            String template = translationService.getByCode(emailKeywords, lang);
////            if (StringUtils.noEmpty(template)) {
////                template = StrUtil.format(template, code);
////            } else {
////                template = "<html><head></head><body><div style='width: 400px;margin: 0 auto;'><img src='" + EnumDict.EMAIL_HEADER_LOGO.getValueCache() + "' width='100%'/><br/>" +
//////                                "<h4>尊敬的用户, 您好！您的验证码是：123456。</h4><br/><div>这是您私人的邮箱验证码，注意隐私安全，请勿随意发给别人，如果不是本人操作，请忽略！！！</div>" +
////                        I18nUtils.get("邮件验证码模版", code) +
////                        "</div></body></html>";
////            }
//            JSONObject cfg = StrUtil.isBlank(merchant.getOtherJson()) ? null : JSONUtil.parseObj(merchant.getOtherJson());
//            AwsMailUtils.sendEmail(
//                    merchant.getApiKey(),
//                    merchant.getApiPwd(),
//                    null == cfg ? null : cfg.getStr("from"),
//                    account, // 收件人邮箱
//                    title,
//                    content);
//            return SysMsgRecordSendResult.SUCCESS;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return SysMsgRecordSendResult.error(e.getMessage());
//        }
//    }
//}
