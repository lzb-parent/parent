package com.pro.common.web.security.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.message.intf.ISysMsgService;
import com.pro.common.module.api.message.model.db.SysMsgRecord;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.modules.api.dependencies.CommonConst;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.message.ToSocket;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.classes.IConfigClass;
import com.pro.common.modules.api.dependencies.model.classes.ISimpleInfo;
import com.pro.common.modules.api.dependencies.service.ITestService;
import com.pro.common.modules.api.dependencies.user.model.UserMsg;
import com.pro.common.modules.service.dependencies.modelauth.base.MessageService;
import com.pro.common.modules.api.dependencies.auth.UserDataQuery;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.web.security.model.request.UserSendMsgRequest;
import com.pro.common.modules.service.dependencies.util.upload.FileUploadUtils;
import com.pro.common.web.security.websocket.MyWebSocketHandlerDecoratorFactory;
import com.pro.framework.api.IReloadService;
import com.pro.framework.api.cache.ICacheManagerCenter;
import com.pro.framework.api.clazz.ClassCaches;
import com.pro.framework.api.database.OrderItem;
import com.pro.framework.api.database.TimeQuery;
import com.pro.framework.api.database.page.PageInput;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.enums.IEnumsService;
import com.pro.framework.api.util.*;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import com.pro.framework.mybatisplus.CRUDService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Api(tags = "通用接口")
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
    @Autowired
    private MessageService messageService;
    @Autowired(required = false)
    private List<ITestService> testServices;
    @Autowired
    private CRUDService<?> crudService;
    // 实体 权限service
    @Autowired
    private IEntityProperties entityProperties;
    @Autowired
    private ISysMsgService sysMsgService;
    @Autowired
    private ICacheManagerCenter cacheManagerCenter;

    @ApiOperation(value = "发送短信")
    @PostMapping("/sendCode")
    public R<?> sendCode(ILoginInfo loginInfo, @RequestBody UserSendMsgRequest request) {
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

    @ApiOperation(value = "站点所有可重载信息-重载")
    @GetMapping("/reload")
    public R<?> reload() {
        ClassCaches.clear();
        reloadServices.stream().sorted(Comparator.comparing(IReloadService::getSort)).forEach(IReloadService::reload);
        return R.ok();
    }

    @ApiOperation(value = "站点所有可测试方法-测试")
    @GetMapping("/test")
    public R<?> test() {
        if (testServices != null) {
            testServices.forEach(service -> {
                log.warn("test {} ", service.getClass());
                service.test();
            });
        }
        messageService.sendToManager(ToSocket.toAllUser(CommonConst.EntityClass.UserRecharge, "111"));
        return R.ok();
    }

    @ApiOperation(value = "枚举转字典列表")
    @RequestMapping("/dictEnumFull/{simpleClassNames}")
    public R<Map<String, List<Map<String, Object>>>> dictEnumFull(@PathVariable String simpleClassNames) {

        boolean isAdmin = EnumApplication.admin.equals(commonProperties.getApplication());
        boolean isAdminChinese = isAdmin && EnumDict.ADMIN_LANG_CHINESE.getValueCacheOrDefault(true);
        Map<String, List<Map<String, Object>>> map = Arrays.stream(simpleClassNames.split(",")).collect(Collectors.toMap(c -> c,
                simpleClassName -> {
                    Class<? extends Enum> eClass = enumsService.getEnumClass(simpleClassName);
                    AssertUtil.notEmpty(eClass, "class not exist:{}", simpleClassName);
                    List<Map<String, Object>> fullList = enumsService.getFullList(eClass);
                    // 翻译label
                    fullList.forEach(e -> e.put("label", doTranslate(isAdmin, isAdminChinese, (String) e.get("label"), (String) e.get("code"))));
                    return fullList;
                }));
        return R.ok(map);
    }


    @ApiOperation(value = "上传")
    @PostMapping("/uploadPre")
    public List<String> upload(
            @RequestParam(name = "file") MultipartFile[] files,
            @RequestParam(name = "module") String module,
            @RequestParam(name = "sign", required = false) String sign) {
        List<String> list = FileUploadUtils.uploadFiles(files, module, sign);
        // 拼接图片prefix
        list = list.stream().map(o -> StrUtil.addPrefixIfNot(o, FileUploadUtils.FILE_PREPEND)).collect(Collectors.toList());
        return list;
    }

    @RequestMapping("/uploadAdmin")
    public List<String> upload(@RequestParam(value = "file") MultipartFile[] files,
                               @RequestParam(value = "module") String module) {
        String host = EnumDict.FILE_UPLOAD_DOMAIN.getValueCache();
        String msg = null;
        if (StrUtils.isNotBlank(host)) {
            String body = null;
            try {
                HttpRequest post = HttpUtil.createPost(host + "/common/uploadPre");
                MultipartFile file = files[0];
                post.form("file", new InputStreamResource(file.getInputStream(), file.getOriginalFilename()));
                post.form("module", module);
                post.form("sign", FileUploadUtils.calSign(file.getName()));
                log.error("sign::" + FileUploadUtils.calSign(file.getName()));
                HttpResponse httpResponse = post.executeAsync();
                body = httpResponse.body();

//                JsonElement element = JSON_PARSER.parse(body);
                if (JSONUtil.isJsonObj(body)) {
                    JSONObject rs = JSONUtil.parseObj(body);
                    String code = rs.getStr("code");
                    if ("500".equals(code)) {
                        throw new BusinessException(rs.getStr("msg"));
                    }
                }
                return JSONUtil.parseArray(body).toList(String.class);
            } catch (Exception e) {
                msg = body + "|" + e.getMessage();
                msg = msg.replace("Connection refused: connect", "文件服务器[" + host + "]无法连接，请确认设置.");
            }
        } else {
            msg = "文件服务器未配置.";
        }
        throw new BusinessException("文件上传失败|{}", msg);
    }

    @ApiOperation(value = "图形验证码")
    @RequestMapping("getCaptcha")
    public void generate(HttpServletRequest request, HttpServletResponse response) {
        // 定义图形验证码的长、宽、验证码字符数、干扰线宽度
//        ShearCaptcha captcha = CaptchaUtil.createShearCaptcha(200, 100, 4, 4);
        // 圆圈干扰验证码
//        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 0);
        // 纯数字验证码（基于hutool工具类封装）
        NumberCaptcha captcha = new NumberCaptcha(200, 100, 4);
        // 将验证码存入session缓存
        request.getSession().setAttribute(CommonConst.Str.SYSTEM_CAPTCHA, captcha.getCode());
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            captcha.write(outputStream);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    private static String doTranslate(boolean isAdmin, Boolean isAdminChinese, String labelChinese) {
//        return doTranslate(isAdmin, isAdminChinese, labelChinese, null);
//    }

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

    // "/loginUserIds"
    @ApiOperation(value = "当前在线用户id列表")
    @GetMapping(CommonConst.Str.path_loginUserIds)
    public R<String> loginUserIds() {
        Map<Long, ILoginInfo> map = MyWebSocketHandlerDecoratorFactory.loginUserMap;
        return R.ok(map.values().stream().map(o -> o.getId().toString()).collect(Collectors.joining(",")));
    }

    @ApiOperation(value = "通用少量公开信息列表查询")
    @GetMapping("/selectListsSimple/{entityClassNames}")
    public R<Map<String, List<?>>> selectListsSimple(@PathVariable String entityClassNames,
                                                     PageInput pageInput,
                                                     @RequestParam Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        return R.ok(Arrays.stream(entityClassNames.split(",")).collect(Collectors.toMap(c -> c, c -> selectListsSimpleOne(pageInput, c, paramMap, timeQuery, query))));
    }

    private static final List<String> SIMPLE_PROPS = Arrays.asList("id", "code", "name", "username", "label", "langCode", "cname", "enabled", "sort", "sysRole");

    private List<?> selectListsSimpleOne(PageInput pageInput, String entityClassName, Map<String, Object> paramMap, TimeQuery timeQuery, UserDataQuery query) {
        Class<?> beanClass = getBeanClass(entityClassName);
        AssertUtil.isTrue(IConfigClass.class.isAssignableFrom(beanClass) || ISimpleInfo.class.isAssignableFrom(beanClass), "error permission");
        // 用户端,配置类,默认正序
//        PageInput pageInput = new PageInput();
        if (IConfigClass.class.isAssignableFrom(beanClass)) {
            if (CollUtil.isEmpty(pageInput.getOrders())) {
                List<OrderItem> orders = Arrays.asList(new OrderItem("sort", true), new OrderItem("id", true));
                pageInput.setOrders(orders);
            }
        }
        return crudService.selectList(entityClassName, paramMap, timeQuery, null, new ArrayList<>(CollUtil.intersection(ClassCaches.getFieldNamesByClass(beanClass), SIMPLE_PROPS)), null, null, pageInput.getOrders());
    }

    private <T> Class<T> getBeanClass(String classSimpleName) {
        Class<T> tClass = (Class<T>) entityProperties.getEntityClassReplaceMap().computeIfAbsent(classSimpleName, c -> MultiClassRelationFactory.INSTANCE.getEntityClass(StrUtils.firstToLowerCase(classSimpleName)));
        AssertUtil.notEmpty(tClass, "entity not exist: " + classSimpleName);
        return tClass;
    }
}
