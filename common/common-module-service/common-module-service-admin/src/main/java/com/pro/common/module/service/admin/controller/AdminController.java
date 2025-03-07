package com.pro.common.module.service.admin.controller;

import com.pro.common.module.service.admin.model.db.Admin;
import com.pro.common.module.service.admin.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员管理")
@RestController
@RequestMapping("/common/admin")
@Getter
@AllArgsConstructor
public class AdminController {
    private AdminService adminService;

    @Operation(summary = "修改密码")
    @RequestMapping("/changePassword")
    public void changePassword(@RequestBody Admin entity) {
        adminService.checkOldPassword(entity, Admin::getPassword);
        adminService.updateById(entity);
    }
}
