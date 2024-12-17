//package com.pro.common.module.service.user.service.authservice;
//
//import com.pro.common.module.service.user.model.db.User;
//import com.pro.common.modules.service.dependencies.modelauth.base.AuthService;
//import com.pro.common.modules.service.dependencies.modelauth.base.TableAuthData;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class UserAuthService extends AuthService<User> {
//    @Override
//    public List<TableAuthData<User>> getConfigs() {
//        // 关闭访问,特殊类,只能通过定制接口访问
//        return List.of(TableAuthData.unAuthAll());
//    }
//}
