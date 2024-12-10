package com.pro.common.module.api.user.service;

import com.pro.common.modules.api.dependencies.user.intf.InviteCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOtherService {

    protected static List<InviteCodeService> inviteCodeServices;

    @Autowired
    public void setInviteCodeServices(List<InviteCodeService> inviteCodeServices) {
        UserOtherService.inviteCodeServices = inviteCodeServices;
    }
}
