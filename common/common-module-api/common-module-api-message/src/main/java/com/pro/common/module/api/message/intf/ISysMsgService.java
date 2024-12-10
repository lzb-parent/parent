package com.pro.common.module.api.message.intf;

import com.pro.common.module.api.message.enums.EnumSysMsgChannel;
import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.user.model.db.User;
import com.pro.common.modules.api.dependencies.user.model.IUserMsg;
import lombok.SneakyThrows;

import java.util.List;
import java.util.Map;

public interface ISysMsgService {
    @SneakyThrows
    void sendMsgAllChannelType(IUserMsg user, String businessCode, Map<String, Object> paramMap, String lang, String ip, Boolean failThrow, Boolean limitTimesFlag, EnumSysMsgChannel channel);

    List<SysMsgRecord> sendByUser(IUserMsg user, SysMsgRecord sysMsgRecord);
}
