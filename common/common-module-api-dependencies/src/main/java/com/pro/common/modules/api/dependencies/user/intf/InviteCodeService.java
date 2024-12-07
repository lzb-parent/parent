package com.pro.common.modules.api.dependencies.user.intf;

import com.pro.common.modules.api.dependencies.model.ILoginInfo;

public interface InviteCodeService {
    ILoginInfo getByCode(String code);
}
