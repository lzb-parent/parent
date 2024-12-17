//package com.pro.common.module.api.common.service.auth;
//
//import com.pro.common.module.api.common.model.db.AuthRoute;
//import com.pro.common.modules.api.dependencies.enums.EnumSysRole;
//import com.pro.common.modules.service.dependencies.modelauth.base.AuthService;
//import com.pro.common.modules.service.dependencies.modelauth.base.TableAuthData;
//import org.springframework.stereotype.Service;
//
//import java.util.Arrays;
//import java.util.List;
// // AuthRouteService里面做了定制处理了 不需要这里的附加处理了
//@Service
//public class AuthRouteAuthService extends AuthService<AuthRoute> {
//    @Override
//    public List<TableAuthData<AuthRoute>> getConfigs() {
//        return List.of(
//                TableAuthData.query(o->EnumSysRole.USER.equals(o.getSysRole()), List.of(EnumSysRole.USER),null),
//                TableAuthData.query(o->EnumSysRole.AGENT.equals(o.getSysRole()), List.of(EnumSysRole.AGENT),null),
//                TableAuthData.query(o->EnumSysRole.ADMIN.equals(o.getSysRole()), List.of(EnumSysRole.ADMIN),null)
//        );
//    }
//}
