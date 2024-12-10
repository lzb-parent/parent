package com.pro.common.module.service.message.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.model.db.SysMsgChannelMerchant;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.message.model.vo.SysMsgRecordSendResult;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.common.modules.service.dependencies.modelauth.base.MessageService;
import com.pro.framework.api.message.IBaseMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.pro.framework.api.structure.Tuple2;

/**
 * 推送系统通知
 */
@Slf4j
@Service("sysMsgChannelServiceSiteMessage")
public class SysMsgChannelServiceSiteMessage implements ISysMsgChannelService {

    //    @Autowired
//    private SysNoticeService noticeService;
//    @Autowired
//    private UserService userService;
    @Autowired
    private MessageService messageService;

    @Override
    public List<SysMsgRecordSendResult> sendMsgs(SysMsgChannelMerchant merchant, List<Tuple2<String, Map<String, ?>>> accountAndParams, SysMsgChannelTemplate template, String senderId, String lang) {
//        String dialogPosterCode = template.getDialogPosterCode();
//        List<Long> userIds = accountAndParams.stream().map(accountAndParam -> Long.valueOf(accountAndParam.getT1())).collect(Collectors.toList());
//        Map<Long, User> userMap = userService.getMainInfo(userIds, null);

        // 保存通知记录
//        List<SysNotice> notices = accountAndParams.stream().map(accountAndParam -> {
//            Map<String, ?> paramMap = accountAndParam.getT2();
//            Long userId = Long.valueOf(accountAndParam.getT1());
//            String username = userMap.get(userId).getUsername();
////            Map<String, Object> paramMap = JSONUtil.parseObj(JSONUtil.toJsonStr(accountAndParam.getT2()));
//            String title = StrUtil.format(template.getTitleTemplate(), paramMap);
//            String content = StrUtil.format(template.getContentTemplate(), paramMap);
//
//            SysNotice sysNotice = new SysNotice(userId, username, title, null, null, StringUtils.or(template.getNoticeType(), EnumNoticeType.MESSAGE));
//            if (template.getContentTemplateRichTextFlag()) {
//                sysNotice.setContent(content);
//            } else {
//                sysNotice.setDescription(content);
//                sysNotice.setContent(content);
//            }
//            sysNotice.setDialogPosterCode(dialogPosterCode);
//            sysNotice.setParamMap(paramMap);
//            sysNotice.setLang(template.getLang());
//            return sysNotice;
//        }).collect(Collectors.toList());
//        noticeService.saveBatch(notices);

        // 推送socket
//        List<Tuple2<Long, String>> userIdAndMessages = accountAndParams.stream().map(accountAndParam -> {
//            SysNotice sysNotice = new SysNotice();
//            sysNotice.setDialogPosterCode(dialogPosterCode);
//            sysNotice.setParamMap(accountAndParam.getT2());
//            sysNotice.setLang(template.getLang());
//            return new Tuple2<>(Long.valueOf(accountAndParam.getT1()), JSONUtil.toJsonStr(sysNotice));
//        }).collect(Collectors.toList());
        for (Tuple2<String, Map<String, ?>> accountAndParam : accountAndParams) {
            SysMsgRecord record = SysMsgRecord.builder()
                    .titleTemplate(template.getTitleTemplate())
                    .contentTemplate(template.getContentTemplate())
                    .paramMap(accountAndParam.getT2())
                    .dialogPosterCode(template.getDialogPosterCode())
                    .build();
            messageService.sendToUser(ToSocket.toUser(record.getClass().getSimpleName(), record, Long.valueOf(accountAndParam.getT1())));
        }
        return accountAndParams.stream().map(userId -> SysMsgRecordSendResult.SUCCESS).collect(Collectors.toList());
    }
}
