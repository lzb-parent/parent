package com.pro.common.web.security.model.request;

import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.user.model.db.User;
import lombok.Data;

@Data
public class UserSendMsgRequest {
    private User user;
    private SysMsgRecord sysMsgRecord;
}
