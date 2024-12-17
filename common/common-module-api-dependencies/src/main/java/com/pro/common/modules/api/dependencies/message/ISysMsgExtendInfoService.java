package com.pro.common.modules.api.dependencies.message;

import com.pro.common.modules.api.dependencies.user.model.IUserMsg;
import com.pro.framework.api.structure.TriConsumer;

import java.util.Map;

public interface ISysMsgExtendInfoService {
    void setExtendInfo(Map<Long, IUserMsg> userMap, TriConsumer<Long, Long, String> consumer);

}
