package com.pro.common.web.security.controller;

import com.pro.common.modules.api.dependencies.enums.EnumEnv;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.web.security.service.DevTranslateService;
import com.pro.framework.api.util.AssertUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "本地开发调试接口")
@RestController
@RequestMapping("/dev")
@AllArgsConstructor
public class DevController {
    private DevTranslateService devTranslateService;
    private CommonProperties commonProperties;

    @Operation(summary = "开发接口增量加载翻译", description = "实体类名,属性名,基础站点文案单词,存入到messages_zh_CN.properties")
    @GetMapping(value = "/reloadTranslateKeys")
    public String translateKeys(@Parameter(hidden = true) ILoginInfo loginInfo) {
        AssertUtil.isTrue(EnumEnv.dev.equals(commonProperties.getEnv()), "只有本地启动执行才有意义");
        return devTranslateService.translateKeys();
    }

//    @SneakyThrows
//    @Operation(summary = "生成代码")
//    @GetMapping(value = "/generateCode")
//    public String generateCode(@Parameter(hidden = true) ILoginInfo loginInfo) {
////        AbsGenerator
//        return "";
//    }
}
