package com.pro.common.module.service.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pro.common.module.api.pay.model.db.UserRecharge;

public interface UserRechargeDao extends BaseMapper<UserRecharge> {

//    BigDecimal getTodayFirstMoney(@Param("userId") Long userId);
//
//    BillCountData countFirstRecharge(@Param("agentIdList") List<Long> agentIdList,
//                                     @Param("userIdList") List<Long> userIdList,
//                                     @Param("times") Integer times,
//                                     @Param("start") String start,
//                                     @Param("end") String end);
//
//    List<Long> getFirstRechargeUserIdList(@Param("agentId") Long agentId,
//                                          @Param("start") String start,
//                                          @Param("end") String end);
//
//    void clearInvalidOrder(@Param("time") LocalDateTime time);
//
//    IPage<UserRecharge> getAdminList(Page page, @Param("vo") UserRechargeQuery query);
//
//    BigDecimal countFee(@Param("agentIdList") List<Long> agentIdList,
//                        @Param("userIdList") List<Long> userIdList,
//                        @Param("start") String start,
//                        @Param("end") String end);
//
//    Integer countBankAccount(@Param("bankAccount") String bankAccount, @Param("userId") Long userId);
//
//    List<Long> getRechargeTimesUserIdList(@Param("agentIdList") List<Long> agentIdList,
//                                          @Param("start") String start,
//                                          @Param("end") String end,
//                                          @Param("rechargeTimes") Integer rechargeTimes);
}
