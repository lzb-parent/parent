//package com.pro.common.web.controller.user.controller;
//
//import cn.hutool.core.util.ObjectUtil;
//import com.pro.common.module.api.system.model.enums.EnumDict;
//import com.pro.common.module.api.user.intf.IUserService;
//import com.pro.common.module.api.user.model.db.User;
//import com.pro.common.module.api.user.service.UserService;
//import com.pro.common.module.api.usermoney.model.db.UserMoney;
//import com.pro.common.module.api.usermoney.model.intf.IUserMoneyUnitService;
//import com.pro.common.modules.api.dependencies.R;
//import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
//import com.pro.common.web.controller.user.model.request.TeamRequest;
//import com.pro.common.web.controller.user.model.response.TeamChildInfo;
//import com.pro.common.web.controller.user.model.response.UserChildVo;
//import com.pro.framework.api.util.CollUtils;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import io.swagger.v3.oas.annotations.Operation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Tag(name = "用户接口")
//@RestController
//@RequestMapping("user")
//public class UserController {
//    @Autowired
//    IUserService userService;
//    @Autowired
//    IUserMoneyUnitService userMoneyService;
//    //    @Autowired
////    IUserMoneyUnitService userMoneyUnitService;
////    @Autowired
////    IUserLevelService userLevelService;
////    @Autowired
////    private UserIntegralService userIntegralService;
////    @Autowired
////    private IUserBillService userBillService;
//    public static final UserChildVo EMPTY_CHILD = new UserChildVo();
//
//    @Operation(summary = "团队报表")
//    @PostMapping("team/data")
//    public R<?> teamData(@Parameter(hidden = true) ILoginInfo loginInfo, @RequestBody TeamRequest requestVo) {
//        int level = EnumDict.USER_TEAM_LEVELS.getValueCacheOrDefault(3);
//        Integer requestLevel = requestVo.getLevel();
//        if (null != requestLevel) {
//            level = Math.min(level, requestLevel);
//        }
//        Long userId = loginInfo.getId();
//        List<UserChildVo> childList = userService.getAllChildList(userId, level);
//        if (null != requestLevel) {
//            childList = childList.stream().filter(t -> t.getLevel() == requestLevel).collect(Collectors.toList());
//        }
//
//        User user = userService.getById(userId);
//        Map<Long, UserChildVo> userMap = CollUtils.listToMap(childList, UserChildVo::getUserId);
//        userMap.put(user.getId(), UserChildVo.builder().userId(userId).username(user.getUsername()).build());
//        childList.forEach(u -> u.setPusername(ObjectUtil.defaultIfNull(userMap.getOrDefault(u.getPid(), EMPTY_CHILD).getUsername(), user.getUsername())));
//
//        List<UserMoney> list = userMoneyService.listByUserIds(childList.stream().map(UserChildVo::getUserId).collect(Collectors.toSet()), false);
//        Map<Long, UserMoney> moneyMap = list.stream().collect(Collectors.toMap(UserMoney::getUserId, o -> o));
//        List<TeamChildInfo> teamList = childList.stream().map(o -> {
//            UserMoney userMoney = moneyMap.get(o.getUserId());
//            if (userMoney != null) {
//                return TeamChildInfo.builder()
//                        .level(o.getLevel())
//                        .userId(o.getUserId())
//                        .username(o.getUsername())
//                        .createTime(o.getCreateTime())
//                        .pusername(o.getPusername())
//                        .balance(userMoney.getAmount())
//                        .totalRechargeMoney(userMoney.getTotalRechargeMoney())
//                        .totalTkMoney(userMoney.getTotalTkMoney())
//                        .build();
//            }
//            return TeamChildInfo.builder().build();
//        }).sorted(Comparator.comparing(TeamChildInfo::getCreateTime).reversed()).collect(Collectors.toList());
//        return R.ok(teamList);
//    }
//
//}
