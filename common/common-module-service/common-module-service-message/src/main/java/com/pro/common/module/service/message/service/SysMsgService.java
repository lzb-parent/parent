package com.pro.common.module.service.message.service;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.module.api.message.enums.EnumSysMsgState;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.module.service.message.service.impl.ISysMsgChannelService;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.enums.EnumPosterCode;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.exception.BusinessPosterException;
import com.pro.common.modules.api.dependencies.message.ISysMsgExtendInfoService;
import com.pro.common.modules.api.dependencies.user.model.IUserMsg;
import com.pro.common.modules.service.dependencies.util.SpringContextUtils;
import com.pro.framework.api.structure.Tuple2;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.api.util.StrUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SysMsgService implements ISysMsgService {
    @Autowired
    private SysMsgRecordService sysMsgRecordService;
    @Autowired
    private SysMsgChannelMerchantService sysMsgChannelMerchantService;
    @Autowired
    private SysMsgChannelTemplateService sysMsgChannelTemplateService;
    @Autowired(required = false)
    private ISysMsgExtendInfoService msgExtendInfoService;
//    @Autowired
//    private TaskExecutor taskExecutor;
//    @Autowired
//    private CountryService countryService;

    private void checkChannelType(EnumSysMsgChannelType channelType, String account) {
        switch (channelType) {
            case PHONE_SMS:
                String register_phone_pattern = EnumDict.REGISTER_PHONE_PATTERN.getValueCache();
                if (StrUtils.isNotBlank(register_phone_pattern)) {
                    //自定义 手机号格式校验
                    if (!account.matches(register_phone_pattern)) {
                        throw new BusinessPosterException(EnumPosterCode.register_phone_error.name(), Map.of("phone", account));
//                        String register_phone_pattern_tip = EnumDict.REGISTER_PHONE_PATTERN_TIP.getValueCache();
//                        if (StrUtils.isBlank(register_phone_pattern_tip)) {
//                            throw new BusinessException("手机号格式错误");
//                        } else {
//                            throw new BusinessException(StrUtils.emptyToDefault(I18nUtils.get(register_phone_pattern_tip), register_phone_pattern_tip) + "|" + account);
//                        }
                    }
                }
//                else {
                //默认 手机号格式校验
//                if (!(account.matches(RegUtils.PHONE) || account.matches(RegUtils.PHONE2))) {
//                    throw new BusinessException("Incorrect phone format:" + channelType + " " + account);
//                }
//                }
                break;
            case EMAIL: // 邮件短信
                if (!account.matches(CommonConst.Reg.EMAIL)) {
                    throw new BusinessException("Incorrect email format:" + channelType + " " + account);
                }
                break;
            case SITE_MESSAGE:
                break;
//            case XXX:
//                break;
            default:
                throw new BusinessException("invalid EnumSysMsgChannelType:" + channelType + " " + account);
        }
    }


    private String fullAccount(EnumSysMsgChannelType channelType, String account) {
//        switch (channelType) {
//            //站内消息无成本
//            case PHONE_SMS:
//                if (account.startsWith("+")) {
//                    account = account.substring(1);
//                }
//                if (account.startsWith("0")) {
//                    account = account.substring(1);
//                }
//                String prefix = EnumDict.SMS_PHONE_PREFIX.getValueCacheOrDefault("");
//                if (prefix.startsWith("+")) {
//                    prefix = prefix.substring(1);
//                }
//                account = prefix + account;
//                break;
//        }
        return account;
    }

    /**
     * 广播通知 如果各个消息入口总开关关闭/消息模板禁用,就跳过发送
     */
    @SneakyThrows
    @Override
    public void sendMsgAllChannelType(IUserMsg user, String businessCode, Map<String, Object> paramMap, String lang, String ip, Boolean failThrow, Boolean limitTimesFlag, EnumSysMsgChannel channel) {
        this.sendBatch(Arrays.asList(EnumSysMsgChannelType.values()), Collections.singletonList(new Tuple2<>(user, paramMap)), businessCode, user.getLang());
//        if (user == null) {
//            return;
//        }
//        Throwable ex = null;
//        lang = LogicUtils.or(lang, user.getLang());

        //站内消息 邮箱 短信
//        for (EnumSysMsgChannelType channelType : EnumSysMsgChannelType.values()) {
//            Serializable account = this.getAccount(channelType, user);
//            if (null == account || StrUtils.EMPTY.equals(account)) {
//                continue;
//            }
//            try {
//                this.sendBatch(channelType, account.toString(), businessCode, paramMap, ip, user.getId(), failThrow, lang, limitTimesFlag, channel, user, null);
//            } catch (Throwable e) {
//                if (!(e instanceof BusinessException)) {
//                    e.printStackTrace();
//                }
//                ex = e;
//            }
//        }
//        if (ex != null) {
//            throw ex;
//        }
    }

    /**
     * 大批量发送发消息
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<SysMsgRecord> send(IUserMsg user, SysMsgRecord sysMsgRecord) {
        EnumSysMsgChannelType channelType = sysMsgRecord.getChannelType();
        String businessCode = sysMsgRecord.getBusinessCode();
        return sendBatch(Collections.singletonList(channelType), Collections.singletonList(new Tuple2<>(user, sysMsgRecord.getParamMap())), businessCode, user.getLang());
    }

    /**
     * 大批量发送发消息
     */
    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<SysMsgRecord> sendBatch(List<EnumSysMsgChannelType> channelTypes, List<Tuple2<? extends IUserMsg, Map<String, ?>>> userAndParams, String businessCode, String lang) {
        if (userAndParams.isEmpty()) {
            return Collections.emptyList();
        }
        // 渠道
//        .filter(c -> c.getOpenDict().getValueCacheOrDefault(false))
//        List<EnumSysMsgChannelType> channelTypes = new ArrayList<>(EnumSysMsgChannelType.MAP.values());
        if (channelTypes.isEmpty()) {
            return Collections.emptyList();
        }
        // 渠道-商户
        List<SysMsgChannelMerchant> merchants = sysMsgChannelMerchantService.lambdaQuery().in(SysMsgChannelMerchant::getChannelType, channelTypes).eq(SysMsgChannelMerchant::getEnabled, true).list();
        if (merchants.isEmpty()) {
            return Collections.emptyList();
        }
        Map<EnumSysMsgChannel, SysMsgChannelMerchant> merchantMap = CollUtils.listToMapAllRight(merchants, SysMsgChannelMerchant::getChannel, v -> v);
        // 渠道-商户-消息模板 < 多语言 >
        List<SysMsgChannelTemplate> templates = sysMsgChannelTemplateService.lambdaQuery().in(SysMsgChannelTemplate::getChannelType, channelTypes).in(SysMsgChannelTemplate::getEnabled, true).eq(SysMsgChannelTemplate::getBusinessCode, businessCode).eq(null != lang, SysMsgChannelTemplate::getLang, lang).list();
        if (templates.isEmpty()) {
            return Collections.emptyList();
        }
        Map<String, SysMsgChannelTemplate> templateMap = CollUtils.listToMap(templates, SysMsgChannelTemplate::getLang);
//        paramMap = null == paramMap ? Collections.emptyMap() : paramMap;
//        userAndParams.get(0).getT1()
        Map<Long, IUserMsg> userMap = CollUtils.listToMapAllRight(userAndParams, t -> t.getT1().getId(), Tuple2::getT1);

        //MyLocalResolver.lang_default
//
//        account = this.fullAccount(channelType, account);
//
//        channel = template.getChannel();
        List<SysMsgRecord> records = userAndParams.stream().map(userAndParam -> {
            IUserMsg user = userAndParam.getT1();
            SysMsgChannelTemplate template = templateMap.get(user.getLang());
            if (template == null) {
                return null;
            }
            SysMsgChannelMerchant merchant = merchantMap.get(template.getChannel());
            if (merchant == null) {
                return null;
            }
            Map<String, ?> paramMap = userAndParam.getT2();
            SysMsgRecord sysMsgRecord = SysMsgRecord.builder().no(IdWorker.getIdStr() + "M").state(EnumSysMsgState.SUBMITTED).userId(user.getId()).fromUsername(user.getUsername())
                    // .overTime(LocalDateTime.now().plusMinutes(30)) // 30分钟后过期
                    //                                .ip(ip)
                    //                                .fromUserId(fromUserId)
                    //                                .fromUsername(user.getUsername())
                    //                                .account(account)
                    .businessCode(businessCode).channelType(template.getChannelType()).channel(template.getChannel())
//                    .merchant(merchantMap.get(template.getChannel()))
                    .titleTemplate(template.getTitleTemplate()).contentTemplate(template.getContentTemplate()).dialogPosterCode(template.getDialogPosterCode()).paramJson(JSONUtil.toJsonStr(paramMap))
                    // .sid()
                    // .smsCount()
                    // .fee()
                    // .unit()


                    // 临时信息
                    .template(template).paramMap(paramMap)
//                    .user(user)
                    .build();
            return sysMsgRecord;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        for (SysMsgRecord record : records) {

        }
        Map<Long, List<SysMsgRecord>> listMap = records.stream().collect(Collectors.groupingBy(SysMsgRecord::getUserId));

        // 填充扩展信息
        msgExtendInfoService.setExtendInfo(userMap, (userId, agentId, agentUsername) ->
                listMap.get(userId).forEach(record -> {
                    record.setFromAgentId(agentId);
                    record.setFromUsername(agentUsername);
                }));

        records.stream().collect(Collectors.groupingBy(SysMsgRecord::getTemplate)).forEach((template, subRecords) -> {
            EnumSysMsgChannel channel = template.getChannel();
            ISysMsgChannelService service = SpringContextUtils.getBean(channel.getSendBeanName(), ISysMsgChannelService.class);

            List<Tuple2<String, Map<String, ?>>> accountAndParams = subRecords.stream().map(r -> {
                String account = fullAccount(r.getChannelType(), getAccount(r.getChannelType(), userMap.get(r.getUserId())));
                return new Tuple2<String, Map<String, ?>>(account, CollUtils.flatMap(r.getParamMap()));
            }).collect(Collectors.toList());
            // 批量发送
            try {
                List<SysMsgRecordSendResult> sysMsgRecordSendResults = service.sendMsgs(merchantMap.get(channel), accountAndParams, template, null, null);
                // 保存结果
                for (int i = 0; i < subRecords.size(); i++) {
                    SysMsgRecord record = subRecords.get(i);
                    SysMsgRecordSendResult rs = sysMsgRecordSendResults.get(i);
                    record.setSid(rs.getMsgId());
                    record.setSmsCount(rs.getCount());
                    record.setFee(rs.getFee());
                    record.setUnit(rs.getUnit());
                    record.setState(rs.getSuccess() ? EnumSysMsgState.HAS_BEEN_SENT : EnumSysMsgState.FAILED_TO_SEND);
                    record.setStateMessage(StrUtil.sub(rs.getErrorMsg(), 0, 200));
                }
            } catch (Exception e) {
//                error = e;
//                errorMsg = e.getMessage();
                log.error("msg sendBatch error {}", JSONUtil.toJsonStr(template), e);
            }

        });
        sysMsgRecordService.saveBatch(records);
//        sysMsgRecord.setTitleTemplate(template.getTitleTemplate());
//        sysMsgRecord.setContentTemplate(template.getContentTemplate());
//        ISysMsgChannelService service = SpringContextUtils.getBean(channel.getSendBeanName(), ISysMsgChannelService.class);
//        String errorMsg;
//        Exception error = null;
//        try {
//            String senderId = null;
//            //发消息
//            SysMsgRecordSendResult result = service.sendMsg(sysMsgChannelMerchant, account, template, CollectionUtils.flatMap(paramMap), senderId, lang);
//
//            //记录
//            errorMsg = result.getErrorMsg();
//            sysMsgRecord.setSid(result.getMsgId());
//            sysMsgRecord.setSmsCount(result.getCount());
//            sysMsgRecord.setFee(result.getFee());
//            sysMsgRecord.setUnit(result.getUnit());
//        } catch (Exception e) {
//            error = e;
//            errorMsg = e.getMessage();
//        }
//
//
//        if (StrUtil.isBlank(errorMsg)) {
//            sysMsgRecord.setState(EnumSysMsgState.HAS_BEEN_SENT);
//            sysMsgRecordService.save(sysMsgRecord);
//        } else {
//            //noinspection ConstantConditions
//            String finalErrorMsg = errorMsg;
//            cfgTaskPoolExecutor.getAsyncExecutor().execute(() -> {
//                //异步保存,避免因事务回滚
//                sysMsgRecord.setState(EnumSysMsgState.FAILED_TO_SEND);
//                sysMsgRecord.setStateMessage(StrUtil.sub(finalErrorMsg, 0, 200));
//                sysMsgRecordService.save(sysMsgRecord);
//            });
//            if (null != failThrow && failThrow) {
//                throw ObjectUtil.defaultIfNull(error, new BusinessException(errorMsg));
//            } else {
//                log.error(errorMsg, error);
//            }
//        }
        return records;
    }

    private String getAccount(EnumSysMsgChannelType channelType, IUserMsg user) {
        switch (channelType) {
            case SITE_MESSAGE:
                return null == user.getId() ? null : user.getId().toString();
            case EMAIL:
                String email = user.getEmail();
                AssertUtil.isTrue(Validator.isEmail(email), "邮箱格式不正确");
                return email;
            case PHONE_SMS:
                return user.getPhone();
        }
        return null;
    }
//
//    public void send(User user, String businessCode, Map<String, Object> paramMap, String ip, EnumSysMsgChannelType channelType, Agent agent) {
//        Serializable account = this.getAccount(channelType, user);
//        if (null == account || StrUtils.EMPTY.equals(account)) {
//            return;
//        }
//        try {
//            this.send(channelType, account.toString(), businessCode, paramMap, ip, user.getId(), true, user.getLang(), false, null, user, agent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public String getMsgKey(IUserMsg user, String businessCode) {
        return user.getSysRole() + "_" + user.getId() + "_" + businessCode + "_" + user.getEmail() + "_" + user.getPhone();
    }

}
