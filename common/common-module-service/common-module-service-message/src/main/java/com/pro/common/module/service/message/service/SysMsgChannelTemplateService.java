package com.pro.common.module.service.message.service;

import cn.hutool.core.collection.CollUtil;
import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.enums.EnumSysMsgChannelType;
import com.pro.common.module.api.message.model.db.SysMsgChannelTemplate;
import com.pro.common.module.service.message.dao.SysMsgChannelTemplateDao;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.mybatisplus.BaseService;
import com.pro.framework.mybatisplus.wrapper.MyWrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息(短信,邮件)模板
 */
@Service
@Slf4j
public class SysMsgChannelTemplateService extends BaseService<SysMsgChannelTemplateDao, SysMsgChannelTemplate> {
    public SysMsgChannelTemplate getOne(
            String businessCode,
            EnumSysMsgChannelType channelType,
            String lang, EnumSysMsgChannel channel) {
        SysMsgChannelTemplate one = getByLang(businessCode, channelType, lang, channel);
        if (one == null) {
            one = getByLang(businessCode, channelType, null, channel);
        }
        return one;
    }

    private SysMsgChannelTemplate getByLang(String businessCode, EnumSysMsgChannelType channelType, String lang, EnumSysMsgChannel channel) {
        return lambdaQuery()
                .eq(SysMsgChannelTemplate::getBusinessCode, businessCode)
                .eq(SysMsgChannelTemplate::getChannelType, channelType)
                .eq(null != channel, SysMsgChannelTemplate::getChannel, channel)
                .eq(StrUtils.isNotBlank(lang), SysMsgChannelTemplate::getLang, lang)
                .eq(SysMsgChannelTemplate::getEnabled, true)
                .orderByAsc(SysMsgChannelTemplate::getSort)
                .last("limit 1")
                .one();
    }

    public List<SysMsgChannelTemplate> list(List<String> codeList, SysMsgChannelTemplate param) {
        return this.list(MyWrappers.lambdaQuery(param).in(CollUtil.isNotEmpty(codeList), SysMsgChannelTemplate::getBusinessCode, codeList));
    }
}
