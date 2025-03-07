package com.pro.common.module.service.usermoney.controller;

import com.pro.common.module.api.usermoney.model.db.UserMoneyWait;
import com.pro.common.module.api.usermoney.model.db.UserMoneyWaitRecord;
import com.pro.common.module.api.usermoney.model.dto.UserMoneyWaitChangeDTO;
import com.pro.common.module.service.usermoney.service.UserMoneyWaitRecordService;
import com.pro.common.module.service.usermoney.service.UserMoneyWaitService;
import com.pro.common.module.service.usermoney.service.UserMoneyWaitUnitService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "数额变化")
@RestController
@RequestMapping("/userMoneyWait")
@Getter
public class UserMoneyWaitController extends AmountEntityBaseController<UserMoneyWait, UserMoneyWaitRecord, UserMoneyWaitChangeDTO, UserMoneyWaitUnitService> {
    @Autowired
    private UserMoneyWaitUnitService amountEntityUnitService;
    @Autowired
    private UserMoneyWaitService amountEntityService;
    @Autowired
    private UserMoneyWaitRecordService amountEntityRecordService;
    // 待结算 余额           http://127.0.0.1:8094/userMoneyWait/getInfo?1=1&type=invite
    // 待结算 帐变           http://127.0.0.1:8094/userMoneyWait/change?1=1&type=invite&amount=100&userId=1&upDown=up&orderId=1&orderNo=1
    // 待结算 取消订单(反帐变) http://127.0.0.1:8094/userMoneyWait/cancelByOrders?1=1&orderId=1
    // 待结算 帐变记录        http://127.0.0.1:8094/userMoneyWait/getRecordList?1=1&type=invite&orderNo=1
//    @Operation(summary = "取消待结算记录")
//    @RequestMapping("/cancelByOrders")
//    public R<List<UserMoneyWaitRecord>> cancelByOrders(UserMoneyWaitRecord record) {
//        List<UserMoneyWaitRecord> userMoneyWaitRecords = amountEntityUnitService.changeStateByOrders(record);
//        return R.ok(userMoneyWaitRecords);
//    }
}
