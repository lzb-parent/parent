package com.pro.common.web.security.model.request;

import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.modules.api.dependencies.user.model.UserMsg;
import lombok.Data;

@Data
public class UserSendMsgRequest {
    private UserMsg user;
    private SysMsgRecord sysMsgRecord;
}
