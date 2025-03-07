package com.pro.common.web.security.service;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.modules.api.dependencies.service.ITranslateDateService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.enums.IEnumsService;
import com.pro.framework.api.model.GeneratorDevConfig;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.core.EnumConstant;
import com.pro.framework.core.EnumUtil;
import com.pro.framework.javatodb.model.JTDFieldInfoDbVo;
import com.pro.framework.javatodb.model.JTDTableInfoVo;
import com.pro.framework.javatodb.service.IJTDService;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class DevTranslateService {
    private CommonProperties commonProperties;
    private IEnumsService enumsService;
    private IJTDService jtdService;
    private List<ITranslateDateService> translateDateServices;

    @SuppressWarnings("CollectionAddAllCanBeReplacedWithConstructor")
    @SneakyThrows
    public String translateKeys()  {
        String platform = commonProperties.getPlatform();

        // 枚举名称 - 公共
        List<String> translateKeysClassCommon = getTranslateKeysEntity(true);
        // 枚举名称 - 定制
        List<String> translateKeysClassPlatform = getTranslateKeysEntity(false);
        translateKeysClassPlatform.removeAll(translateKeysClassCommon);
        // 类属性 - 公共
        List<String> translateKeysEnumCommon = getTranslateKeysEnum(true);
        // 类属性 - 定制
        List<String> translateKeysEnumPlatform = getTranslateKeysEnum(false);
        translateKeysEnumPlatform.removeAll(translateKeysEnumCommon);
        // 类属性 - 公共
        List<String> translateKeysEnumDataCommon = enumsService.getTranslateKeys(true, platform);
        // 类属性 - 定制
        List<String> translateKeysEnumDataPlatform = enumsService.getTranslateKeys(false, platform);

        // 实体数据名称 (字典,菜单) - 公共
        List<String> translateKeysEntityCommon = translateDateServices.stream()
                .flatMap(service -> service.getTranslateKeys(true).stream())
                .toList();
        // 实体数据名称 (字典,菜单) - 公共
        List<String> translateKeysEntityPlatform = translateDateServices.stream()
                .flatMap(service -> service.getTranslateKeys(false).stream())
                .toList();

//        Set<String> translateKeys = new LinkedHashSet<>();
        Set<String> translateKeysPlatform = new LinkedHashSet<>();
        Set<String> translateKeysCommon = new LinkedHashSet<>();
        translateKeysCommon.addAll(translateKeysClassCommon);
        translateKeysPlatform.addAll(translateKeysClassPlatform);
        translateKeysCommon.addAll(translateKeysEnumCommon);
        translateKeysPlatform.addAll(translateKeysEnumPlatform);
        translateKeysCommon.addAll(translateKeysEntityCommon);
        translateKeysPlatform.addAll(translateKeysEntityPlatform);
        translateKeysCommon.addAll(translateKeysEnumDataCommon);
        translateKeysPlatform.addAll(translateKeysEnumDataPlatform);

//        translateKeys.addAll(translateKeysCommon);
//        translateKeys.addAll(translateKeysPlatform);
        translateKeysCommon.removeIf(s -> null == s || s.isEmpty());
        translateKeysPlatform.removeIf(s -> null == s || s.isEmpty());


        String devProjectRootPath = EnumAuthDict.DEV_PROJECT_ROOT_PATH.getValueCache();
        if (StrUtil.isBlank(devProjectRootPath)) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            GeneratorDevConfig generatorConfig = mapper.readValue(new ClassPathResource("system-dev.yml").getFile(),
                    GeneratorDevConfig.class);
            devProjectRootPath = generatorConfig.getWorkspace() + File.separator + generatorConfig.getPlatformName();
        }
//        System.out.println(translateKeysClassCommon.contains("执行订单"));
//        System.out.println(translateKeysEnumCommon.contains("执行订单"));
//        System.out.println(translateKeysEntityCommon.contains("执行订单"));
        if (StrUtil.isNotBlank(devProjectRootPath)) {
            // 生成到 messages_zh_CN.properties 本地文件中
            String subPathCommon = "/parent/common/common-module-service/common-module-service-login/src/main/resources/i18n_login/messages_zh_CN.properties";
            String subPathPlatform = "/platform/" + platform + "-common/src/main/resources/i18n_platform/messages_zh_CN.properties";
            saveNewKeys(devProjectRootPath + subPathCommon, new ArrayList<>(translateKeysCommon));
            saveNewKeys(devProjectRootPath + subPathPlatform, new ArrayList<>(translateKeysPlatform));
        }

        return "<br/><br/>---------platform--------<br/>" + translateKeysPlatform.stream().map(str -> StrUtils.replaceSpecialToUnderline(str) + "=" + str).collect(Collectors.joining("<br/>")) +
                "<br/><br/>---------common--------<br/>" + translateKeysCommon.stream().map(str -> StrUtils.replaceSpecialToUnderline(str) + "=" + str).collect(Collectors.joining("<br/>"));
    }

    private static List<String> getTranslateKeysEnum(boolean isCommon) {
        String packageCommon = "com.pro." + "common";
        String packageFramework = "com.pro." + "framework";
        return EnumConstant.simpleNameClassMap.values()
                .stream().filter(c -> isCommon == (c.getPackage().getName().startsWith(packageCommon) || c.getPackage().getName().startsWith(packageFramework)))
                .flatMap(e -> {
                    Map<Serializable, String> map = EnumUtil.getNameLabelMap(e);
                    return map.values().stream();
                })
                .filter(Objects::nonNull).filter(s -> !s.isEmpty())
                .distinct().collect(Collectors.toList());
    }

    @SneakyThrows
    private static void saveNewKeys(String filePath, List<String> keys) {
        // 创建新键的映射表
        LinkedHashMap<String, String> oriKeyShortKeyMap = keys.stream().collect(Collectors.toMap(
                key -> key, StrUtils::replaceSpecialToUnderline, (v1, v2) -> v1, LinkedHashMap::new));

        // 使用 LinkedHashSet 来存储新键值对的顺序
        LinkedHashSet<String> newEntries = new LinkedHashSet<>();

        // 读取现有属性文件
        Properties properties = new Properties();
        try (FileInputStream inputStream = new FileInputStream(filePath);
             InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            return;
        }

        // 筛选出新增的键值对
        keys.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .forEach(key -> {
                    String shortKey = oriKeyShortKeyMap.get(key);
                    if (!properties.containsKey(shortKey)) {
                        newEntries.add(shortKey + "=" + key);
                    }
                });

        // 将现有的属性保留并追加新的属性到文件末尾
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            for (String entry : newEntries) {
                writer.write(entry);
                writer.newLine();
            }
        }
    }


    private List<String> getTranslateKeysEntity(boolean isCommon) {
        List<JTDTableInfoVo> tables = MultiClassRelationFactory.INSTANCE.getClassMap().values().stream()
                .map(aClass -> jtdService.readTableInfo(aClass)).filter(Objects::nonNull)
                .filter(table -> isCommon == (table.getEntityId() < 10000))
                .sorted(Comparator
                        .comparingInt(JTDTableInfoVo::getEntityId)
                        .thenComparing(JTDTableInfoVo::getEntityName)
                        .thenComparing(JTDTableInfoVo::getLabel)
                ).toList();
        return tables.stream()
                .flatMap(t ->
                        Stream.of(
                                Stream.of(t.getLabel()),
                                t.getFields().stream().map(JTDFieldInfoDbVo::getGroup),
                                t.getFields().stream().map(JTDFieldInfoDbVo::getLabel),
                                t.getFields().stream().map(JTDFieldInfoDbVo::getDescription)).flatMap(s -> s))
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

}
