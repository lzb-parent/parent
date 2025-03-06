package com.pro.common.modules.api.dependencies.user.intf;

import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.ILoginInfoPrepare;

public interface InviteCodeService {
    ILoginInfoPrepare getByCode(String code);
}
