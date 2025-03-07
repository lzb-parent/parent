package com.pro.common.web.security.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import io.swagger.v3.oas.annotations.Parameter;
import com.pro.common.modules.api.dependencies.service.ITestService;
import com.pro.common.modules.api.dependencies.user.model.UserMsg;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.modules.service.dependencies.util.upload.FileUploadUtils;
import com.pro.common.web.security.model.request.UserSendMsgRequest;
import com.pro.common.web.security.service.OtherService;
import com.pro.common.web.security.websocket.MyWebSocketHandlerDecoratorFactory;
import com.pro.framework.api.IReloadService;
import com.pro.framework.api.cache.ICacheManagerCenter;
import com.pro.framework.api.clazz.ClassCaches;
import com.pro.framework.api.enums.IEnumsService;
import com.pro.framework.api.util.AssertUtil;
import com.pro.framework.api.util.BeanUtils;
import com.pro.framework.api.util.LogicUtils;
import com.pro.framework.api.util.NumberCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Tag(name = "其他开放接口")
@RestController
@RequestMapping("/common")
@Slf4j
public class CommonCommonController {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private List<IReloadService> reloadServices;
    @Autowired
    private IEnumsService enumsService;
    @Autowired(required = false)
    private List<ITestService> testServices;
    @Autowired
    private ISysMsgService sysMsgService;
    @Autowired
    private OtherService otherService;
    @Autowired
    private ICacheManagerCenter cacheManagerCenter;


    @Operation(summary = "重载站点所有可重载信息")
    @GetMapping("/reload")
    public R<?> reload() {
        ClassCaches.clear();
        reloadServices.stream().sorted(Comparator.comparing(IReloadService::getSort)).forEach(IReloadService::reload);
        return R.ok();
    }

    @Operation(summary = "执行测试方法(仅非生产环境可用)")
    @GetMapping("/test")
    public R<?> test() {
        if (testServices != null) {
            testServices.forEach(service -> {
                log.warn("test {} ", service.getClass());
                service.test();
            });
        }
//        messageService.sendToManager(ToSocket.toUser(CommonConst.EntityClass.UserRecharge, "111", 1L));
        return R.ok();
    }

    @Operation(summary = "获取枚举字典列表", description = "包括当前语言的翻译单词")
    @GetMapping("/dictEnumFull/{simpleClassNames}")
    public R<Map<String, List<Map<String, Object>>>> dictEnumFull(@PathVariable String simpleClassNames) {
        boolean isAdmin = EnumApplication.admin.equals(commonProperties.getApplication());
        boolean isAdminChinese = isAdmin && EnumAuthDict.ADMIN_LANG_CHINESE.getValueCacheOrDefault(true);
        Map<String, List<Map<String, Object>>> map = Arrays.stream(simpleClassNames.split(",")).collect(Collectors.toMap(c -> c,
                simpleClassName -> {
                    //noinspection rawtypes
                    Class<? extends Enum> eClass = enumsService.getEnumClass(simpleClassName);
                    AssertUtil.notEmpty(eClass, "class not exist:{}", simpleClassName);
                    List<Map<String, Object>> fullList = enumsService.getFullList(eClass);
                    // 翻译label
                    fullList.forEach(e -> e.put("label", doTranslate(isAdmin, isAdminChinese, (String) e.get("label"), (String) e.get("code"))));
                    return fullList;
                }));
        return R.ok(map);
    }


    @Operation(summary = "发送短信")
    @PostMapping("/sendCode")
    public R<?> sendCode(@Parameter(hidden = true) ILoginInfo loginInfo, @RequestBody UserSendMsgRequest request) {
        UserMsg userMsg = new UserMsg();
        BeanUtils.copyPropertiesModel(loginInfo, userMsg);

        SysMsgRecord sysMsgRecord = request.getSysMsgRecord();
        String code = RandomUtil.randomNumbers(6);

        String key = sysMsgService.getMsgKey(userMsg, sysMsgRecord.getBusinessCode());

        cacheManagerCenter.put(CommonConst.CacheKey.SmsCode, key, code, 10, TimeUnit.MINUTES);
        sysMsgRecord.setParamMap(Collections.singletonMap("code", code));

        userMsg.setLang(LocaleContextHolder.getLocale().toLanguageTag());
        List<SysMsgRecord> sysMsgRecords = sysMsgService.send(userMsg, sysMsgRecord);
        if (sysMsgRecords.isEmpty()) {
            throw new BusinessException("消息未发送_请检查消息配置");
        }
        return R.ok();
    }

    @Operation(summary = "用户端上传")
    @PostMapping("/uploadPre")
    public R<List<String>> upload(
            @RequestParam(name = "file") MultipartFile[] files,
            @RequestParam(name = "module") String module,
            @RequestParam(name = "sign", required = false) String sign) {
        List<String> list = FileUploadUtils.uploadFiles(files, module, sign);
        // 拼接图片prefix
        list = list.stream().map(o -> StrUtil.addPrefixIfNot(o, FileUploadUtils.FILE_PREPEND)).collect(Collectors.toList());
        return R.ok(list);
    }

    @Operation(summary = "管理端自定义上传")
    @PostMapping("/uploadAdmin")
    public List<String> uploadAdmin(@RequestParam(value = "file") MultipartFile[] files, @RequestParam(value = "module") String module) {
        switch (commonProperties.getApplication()){
            case admin:
            case agent:
                break;
            default:
                throw new BusinessException("暂无权限");
        }
        return otherService.uploadAdmin(files, module);
    }

    @Operation(summary = "用户端获取图形验证码")
    @PostMapping("getCaptcha")
    @SneakyThrows
    public void generate(HttpServletRequest request, HttpServletResponse response) {
        // 定义图形验证码的长、宽、验证码字符数、干扰线宽度 createCircleCaptcha
//        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        // 纯数字验证码（基于hutool工具类封装）
        NumberCaptcha captcha = new NumberCaptcha(200, 100, 4);
        // 将验证码存入session缓存
        request.getSession().setAttribute(CommonConst.Str.SYSTEM_CAPTCHA, captcha.getCode());
        ServletOutputStream outputStream = response.getOutputStream();
        captcha.write(outputStream);
        outputStream.flush();
    }


    // "/loginUserIds"
    @Operation(summary = "查询当前在线用户id列表")
    @GetMapping(CommonConst.Str.path_loginUserIds)
    public R<String> loginUserIds() {
        Map<Long, ILoginInfo> map = MyWebSocketHandlerDecoratorFactory.loginUserMap;
        return R.ok(map.values().stream().map(o -> o.getId().toString()).collect(Collectors.joining(",")));
    }

    /**
     * 用户端 显示当地语言
     * 后台   优先显示中文
     */
    private static String doTranslate(boolean isAdmin, Boolean isAdminChinese, String labelChinese, String code) {
        if (labelChinese == null) {
            return null;
        }
        String labelTranslate = I18nUtils.get(labelChinese);
        // 后台
        if (isAdmin) {
            if (isAdminChinese) {
                return labelChinese;
            } else {
                return LogicUtils.or(labelTranslate, labelChinese);
            }
        } else {
            // 用户端
            return StrUtil.blankToDefault(labelTranslate, code);
        }
    }
}
