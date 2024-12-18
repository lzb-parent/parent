package com.pro.common.web.security.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.pro.common.module.api.system.model.enums.EnumDict;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.ILoginInfo;
import com.pro.common.modules.api.dependencies.model.classes.*;
import com.pro.common.modules.api.dependencies.service.ITranslateDateService;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.web.security.model.request.JTDTableInfoRequest;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.model.GeneratorDevConfig;
import com.pro.framework.api.util.ClassUtils;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.enums.EnumConstant;
import com.pro.framework.enums.EnumUtil;
import com.pro.framework.javatodb.constant.JTDConst;
import com.pro.framework.javatodb.enums.EnumPositionAlign;
import com.pro.framework.javatodb.enums.EnumUIArea;
import com.pro.framework.javatodb.model.JTDFieldInfoDbVo;
import com.pro.framework.javatodb.model.JTDTableInfoVo;
import com.pro.framework.javatodb.model.UITableInfo;
import com.pro.framework.javatodb.service.IJTDService;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pro.framework.javatodb.constant.JTDConst.EnumAdminButton.*;

@Slf4j
@RestController
@RequestMapping("/commonTable")
public class CommonTableInfoController {
    @Autowired
    private CommonProperties commonProperties;
    @Autowired
    private IEntityProperties entityProperties;

    @Autowired
    private IJTDService jtdService;

    @Autowired
    private List<ITranslateDateService> translateDateServices;

    public static final String NULL = "NULL";
    public static final String CURRENT_TIMESTAMP = "CURRENT_TIMESTAMP";
    private static final List<String> fieldNamesSortEnd = Arrays.asList("enabled", "remark", "sort", "isDemo", "createTime");
    private static final Set<String> fieldNamesIgnore = new HashSet<>(Arrays.asList("deleted", "updateTime"));
    private static final Set<String> fieldNamesIgnoreForm = new HashSet<>(Arrays.asList("createTime", "isDemo"));

    @SneakyThrows
    @ApiOperation(value = "生成代码")
    @GetMapping(value = "/generateCode")
    public String generateCode(ILoginInfo loginInfo) {
//        AbsGenerator
        return "";
    }

    @SneakyThrows
    @ApiOperation(value = "增量加载 后端 messages_zh_CN.properties 待添加的键值")
    @GetMapping(value = "/reloadTranslateKeys")
    public String translateKeys(ILoginInfo loginInfo) {
//        checkPermission(loginInfo);

        // 枚举名称 - 公共
        List<String> translateKeysClassCommon = getTranslateKeysEntity(true);
        // 枚举名称 - 定制
        List<String> translateKeysClassPlatform = getTranslateKeysEntity(false);
        translateKeysClassPlatform.removeAll(translateKeysClassCommon);
        // 类属性 - 公共
        List<String> translateKeysEnumCommon = getTranslateKeysEnum(true, commonProperties.getPlatform());
        // 类属性 - 定制
        List<String> translateKeysEnumPlatform = getTranslateKeysEnum(false, commonProperties.getPlatform());
        translateKeysEnumPlatform.removeAll(translateKeysEnumCommon);

        // 实体数据名称 (字典,菜单) - 公共
        List<String> translateKeysEntityCommon = translateDateServices.stream().flatMap(service -> service.getKeyValueMap(true).values().stream()).collect(Collectors.toList());
        // 实体数据名称 (字典,菜单) - 公共
        List<String> translateKeysEntityPlatform = translateDateServices.stream().flatMap(service -> service.getKeyValueMap(false).values().stream()).collect(Collectors.toList());

        Set<String> translateKeys = new LinkedHashSet<>();
        translateKeys.addAll(translateKeysClassCommon);
        translateKeys.addAll(translateKeysClassPlatform);
        translateKeys.addAll(translateKeysEnumCommon);
        translateKeys.addAll(translateKeysEnumPlatform);
        translateKeys.addAll(translateKeysEntityCommon);
        translateKeys.addAll(translateKeysEntityPlatform);

        String devProjectRootPath = EnumDict.DEV_PROJECT_ROOT_PATH.getValueCache();
        if (StrUtil.isBlank(devProjectRootPath)) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            GeneratorDevConfig generatorConfig = mapper.readValue(new ClassPathResource("system-dev.yml").getFile(), GeneratorDevConfig.class);
            devProjectRootPath = generatorConfig.getWorkspace() + "/" + generatorConfig.getPlatformName();
        }
        System.out.println(translateKeysClassCommon.contains("执行订单"));
        System.out.println(translateKeysEnumCommon.contains("执行订单"));
        System.out.println(translateKeysEntityCommon.contains("执行订单"));
        if (StrUtil.isNotBlank(devProjectRootPath)) {
            // 生成到 messages_zh_CN.properties 本地文件中
            String subPathCommon = "/parent/common/common-module-service/common-module-service-login/src/main/resources/i18n_login/messages_zh_CN.properties";
            String subPathPlatform = "/platform/" + commonProperties.getPlatform() + "-common/src/main/resources/i18n_platform/messages_zh_CN.properties";
            saveNewKeys(devProjectRootPath + subPathCommon, translateKeysClassCommon);
            saveNewKeys(devProjectRootPath + subPathPlatform, translateKeysClassPlatform);
            saveNewKeys(devProjectRootPath + subPathCommon, translateKeysEnumCommon);
            saveNewKeys(devProjectRootPath + subPathPlatform, translateKeysEnumPlatform);
            saveNewKeys(devProjectRootPath + subPathCommon, translateKeysEntityCommon);
            saveNewKeys(devProjectRootPath + subPathPlatform, translateKeysEntityPlatform);
        }

        return String.join("<br/>", translateKeys);
    }

    private static List<String> getTranslateKeysEnum(boolean isCommon, String platform) {
        String packagePlatform = "com.pro." + platform;
        return EnumConstant.simpleNameClassMap.values()
                .stream().filter(c -> isCommon != c.getPackage().getName().startsWith(packagePlatform))
                .flatMap(e -> {
                    Map<Serializable, String> map = EnumUtil.getNameLabelMap(e);
                    return map.values().stream();
                })
                .filter(Objects::nonNull).filter(s -> !s.isEmpty())
                .distinct().collect(Collectors.toList());
    }

    @SneakyThrows
    private static void saveNewKeys(String filePath, List<String> keys) {
        Properties properties = new Properties();

        // 使用 UTF-8 编码读取现有的 properties 文件
        @Cleanup FileInputStream inputStream = new FileInputStream(filePath);
        properties.load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        // 添加新的键值对
        keys.stream()
                .filter(key -> !properties.containsKey(key))
                .forEach(key -> properties.setProperty(key, key));

        // 使用 UTF-8 编码保存文件
        @Cleanup OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8);
        properties.store(writer, "Updated Properties File");
    }

    private List<String> getTranslateKeysEntity(boolean isCommon) {
        List<JTDTableInfoVo> tables = MultiClassRelationFactory.INSTANCE.getClassMap().values().stream()
                .map(aClass -> jtdService.readTableInfo(aClass)).filter(Objects::nonNull)
                .filter(table -> isCommon == (table.getEntityId() < 10000))
                .sorted(Comparator
                        .comparingInt(JTDTableInfoVo::getEntityId)
                        .thenComparing(JTDTableInfoVo::getEntityName)
                        .thenComparing(JTDTableInfoVo::getLabel)
                ).collect(Collectors.toList());
        return tables.stream().flatMap(t ->
                Stream.of(
                        Stream.of(t.getLabel()),
                        t.getFields().stream().map(JTDFieldInfoDbVo::getGroup),
                        t.getFields().stream().map(JTDFieldInfoDbVo::getLabel),
                        t.getFields().stream().map(JTDFieldInfoDbVo::getDescription)).flatMap(s -> s)).filter(Objects::nonNull).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
    }

    @SneakyThrows
    @ApiOperation(value = "查询表格信息")
    @GetMapping(value = "/simpleInfo/{entityClassName}")
    public R<JTDTableInfoVo> simpleInfo(ILoginInfo loginInfo, @PathVariable(required = false) String entityClassName) {
        checkPermission(loginInfo);

        JTDTableInfoVo tableInfo = jtdService.readTableInfo(MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName));
        List<JTDFieldInfoDbVo> fields = tableInfo.getFields();
        tableInfo.setTableName(StrUtil.toCamelCase(tableInfo.getTableName()));
        fields.forEach(field ->
                {
                    field.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                    Class<?> entityClass = field.getEntityClass();
                    if (null != entityClass && !Object.class.equals(entityClass)) {
                        field.setEntityName(StrUtils.firstToLowerCase(entityClass.getSimpleName()));
                    }
                }
        );
        return R.ok(tableInfo);
    }

//    private static final Set<Class> classes = new HashSet(Arrays.asList(User.class

    /// /            ,         UserLevel.class, UserMoney.class)
//    );
    @SneakyThrows
    @ApiOperation(value = "查询表格信息")
    @GetMapping("/info/{entityClassName}")
    public R<UITableInfo> info(ILoginInfo loginInfo, @PathVariable(required = false) String entityClassName, JTDTableInfoRequest request) {
        checkPermission(loginInfo);
        Class<?> clazz = entityProperties.getEntityClassReplaceMap().computeIfAbsent(StrUtils.firstToUpperCase(entityClassName), c -> MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName));
        if (clazz == null) {
            return R.ok();
        }
        JTDTableInfoVo tableInfo = jtdService.readTableInfo(clazz);

        List<JTDFieldInfoDbVo> fields = tableInfo.getFields();
        // 字段名改驼峰
        //noinspection UnnecessaryLocalVariable
        String tableName = entityClassName;
        tableInfo.setTableName(tableName);
        tableInfo.setLabel(I18nUtils.get(tableInfo.getLabel()));
        fields.forEach(field ->
                {
                    field.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                    field.setEntityName(field.getEntityName());
                    field.setDefaultValue(field.getDefaultValue().replaceAll("'", ""));
                    Class<?> entityClass = field.getEntityClass();
                    if (null != entityClass && !Object.class.equals(entityClass)) {
                        String entityName = StrUtils.firstToLowerCase(entityClass.getSimpleName());
                        field.setEntityName(entityName);
                    } else {
                        field.setEntityName(null);
                        field.setEntityClassTargetProp(null);
                        field.setEntityClassLabel(null);
                        field.setEntityClassKey(null);
                        field.setEntityClassTargetProp(null);
                    }
                }
        );
        fields = fields.stream()
                .filter(f -> !f.getUiType().equals(JTDConst.EnumFieldUiType.hide))
                .filter(vo -> !fieldNamesIgnore.contains(vo.getFieldName()))
                .sorted(Comparator.comparing(vo -> fieldNamesSortEnd.indexOf(vo.getFieldName())))
                .collect(Collectors.toList());
        List<JTDFieldInfoDbVo> fieldsSort = fields.stream().filter(vo -> null != vo.getSort() && vo.getSort() >= 0).collect(Collectors.toList());
        List<JTDFieldInfoDbVo> fieldsNoSort = fields.stream().filter(vo -> null == vo.getSort() || vo.getSort() < 0).collect(Collectors.toList());
        List<String> fieldNames = fieldsNoSort.stream().map(JTDFieldInfoDbVo::getFieldName).filter(n -> !"id".equals(n)).collect(Collectors.toList());
        fieldsSort.forEach(fieldSort -> fieldNames.add(fieldSort.getSort(), fieldSort.getFieldName()));


        UITableInfo jtdTableInfoVo = new UITableInfo();
        UITableInfo.TableConfigOne tableConfigOne = new UITableInfo.TableConfigOne();
        BeanUtil.copyProperties(tableInfo, tableConfigOne);
        tableConfigOne.setFieldNames(fieldNames);

        String urlTemplate = request.getUrlTemplate();
        tableConfigOne.setGetOneUrl(getUrl(urlTemplate, "selectOne", tableName));
        tableConfigOne.setInsertUrl(getUrl(urlTemplate, "insert", tableName));
        tableConfigOne.setUpdateUrl(getUrl(urlTemplate, "update", tableName));
        tableConfigOne.setGetPageUrl(getUrl(urlTemplate, "selectPage", tableName));
        tableConfigOne.setDeleteUrl(getUrl(urlTemplate, "delete", tableName));
        tableConfigOne.setExportUrl(getUrl(urlTemplate, "export", tableName));

        if (IUserOrderClass.class.isAssignableFrom(clazz)) {
            tableConfigOne.setAdminButtons(new JTDConst.EnumAdminButton[]{query, edit});
        } else if (IUserDataClass.class.isAssignableFrom(clazz)) {
            tableConfigOne.setAdminButtons(new JTDConst.EnumAdminButton[]{query, add, edit, delete});
        } else if (IConfigClass.class.isAssignableFrom(clazz) || IAdminClass.class.isAssignableFrom(clazz)) {
            tableConfigOne.setAdminButtons(new JTDConst.EnumAdminButton[]{query, add, edit, copy, delete});
        } else {
            // 默认仅查询
            tableConfigOne.setAdminButtons(new JTDConst.EnumAdminButton[]{query});
        }
        // 填充区域(表头信息)
        HashMap<EnumUIArea, UITableInfo.TableConfigOne> areasTable = new HashMap<>(8);
        for (EnumUIArea uiArea : EnumUIArea.values()) {
            areasTable.put(uiArea, this.getUIAreaTable(tableInfo, uiArea, tableConfigOne));
        }

        jtdTableInfoVo.setTableConfigs(areasTable);

        jtdTableInfoVo.setFieldConfigsMap(
                fields.stream().collect(Collectors.toMap(JTDFieldInfoDbVo::getFieldName, configField -> {
                    // 填充区域(字段信息)
                    HashMap<EnumUIArea, UITableInfo.FieldConfigOne> areasField = new HashMap<>(8);
                    for (EnumUIArea uiArea : EnumUIArea.values()) {
                        areasField.put(uiArea, this.getUIAreaField(uiArea, configField));
                    }
                    return areasField;
                }))
        );

        return R.ok(jtdTableInfoVo);
    }

    private static Boolean trueOrNull(Boolean bool) {
        return null == bool ? null : (bool ? true : null);
    }

    private void checkPermission(ILoginInfo loginInfo) {
        switch (commonProperties.getApplication()) {
            case admin:
            case agent:
                if (loginInfo == null || loginInfo.getId() == null) {
                    throw new BusinessException("error permission");
                }
                break;
            default:
                throw new BusinessException("error permission");
        }
    }

    private static String getUrl(String urlTemplate, String option, String tableName) {
        return StrUtil.format(urlTemplate, Map.of("option", option, "entityName", tableName));
    }


    private Object readDefaultValue(JTDConst.EnumFieldUiType uiType, String defaultValue) {
        if (!defaultValue.isEmpty()) {
            if (defaultValue.startsWith("'") || defaultValue.startsWith("\"")) {
                defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
            }
            if (NULL.equals(defaultValue)) {
                return null;
            }
            if (!defaultValue.isEmpty()) {
                //noinspection
                switch (uiType) {
                    case number:
                        return new BigDecimal(defaultValue);
                    case bool:
                        return FrameworkConst.Str.TRUE.equals(defaultValue) || FrameworkConst.Num.TRUE.toString().equals(defaultValue);
                    case datetime:
                        if (CURRENT_TIMESTAMP.equals(defaultValue)) {
                            return null;
                        }
                    default:
                        return defaultValue;
                }
            }
        }
        return null;
    }


    /**
     *
     */
    private UITableInfo.TableConfigOne getUIAreaTable(JTDTableInfoVo tableInfo, EnumUIArea uiArea, UITableInfo.TableConfigOne infoOri) {
//        ObjectUtil.clone(tableConfigOne)
        UITableInfo.TableConfigOne infoNew = new UITableInfo.TableConfigOne();
//        String tableName = tableInfo.getTableName();
        Map<String, JTDFieldInfoDbVo> fieldMap = CollUtils.listToMap(tableInfo.getFields(), JTDFieldInfoDbVo::getFieldName);
        // 排序
        List<String> fieldNamesOri = infoOri.getFieldNames();
        Set<String> fieldUserDataId = fieldNamesOri.stream().filter(fieldName -> {
            JTDFieldInfoDbVo field = fieldMap.get(fieldName);
            Class<?> entityClass = field.getEntityClass();
            return null != entityClass && IUserClass.class.isAssignableFrom(entityClass) && Objects.equals(field.getEntityClassTargetProp(), "id");
        }).collect(Collectors.toSet());
        List<String> fieldNamesNotId = fieldNamesOri.stream().filter(fieldName -> !fieldUserDataId.contains(fieldName)).collect(Collectors.toList());
        List<String> fieldNames;
        boolean isEdit;
        switch (uiArea) {
            case search:
                List<JTDConst.EnumFieldUiType> uiTypes = List.of(JTDConst.EnumFieldUiType.text, JTDConst.EnumFieldUiType.select);
                fieldNames = fieldNamesNotId.stream().filter(fieldName -> uiTypes.contains(fieldMap.get(fieldName).getUiType())).collect(Collectors.toList());
                isEdit = true;
                break;
            case form:
                // 如果有关联实体 只显示id选择框,不显示其他属性
                fieldNames = fieldNamesOri.stream().filter(fieldName -> {
                    String entityClassTargetProp = fieldMap.get(fieldName).getEntityClassTargetProp();
                    return null == fieldMap.get(fieldName).getEntityClass() || Objects.equals(entityClassTargetProp, "code") || Objects.equals(entityClassTargetProp, "id");
                }).collect(Collectors.toList());
                isEdit = true;
                infoOri.setLabelPosition(EnumPositionAlign.left);
                break;
            case table:
                fieldNames = fieldNamesNotId;
                isEdit = false;
                break;
//            case tableColumn:
            case base:
                return infoOri;
//                break;
//                return null;
            default:
                throw new BusinessException("unknown uiArea " + uiArea);
        }

        infoNew.setFieldNames(fieldNames);
        infoNew.setIsEdit(isEdit);
        return infoNew;
    }

    private static final Integer defaultMainLength = 200;
    private static final JTDConst.EnumFieldNullType defaultNotNull = JTDConst.EnumFieldNullType.not_null;
    private static final JTDConst.EnumFieldUiType defaultUiType = JTDConst.EnumFieldUiType.text;

    /**
     *
     */
    private UITableInfo.FieldConfigOne getUIAreaField(EnumUIArea uiArea, JTDFieldInfoDbVo configField) {
        UITableInfo.FieldConfigOne infoCopy = new UITableInfo.FieldConfigOne();
//        String defaultValue = configField.getDefaultValue();
        String fieldName = configField.getFieldName();
        JTDConst.EnumFieldUiType uiType = configField.getUiType();
        switch (uiArea) {
            case search:
                switch (uiType) {
                    case text:
                    case richText:
                    case textarea:
                        infoCopy.setMainLength(null);// 默认值null,表示200
                        infoCopy.setUiType(null);// 默认值null,表示text
                        break;
                }
                break;
//            case tableColumn:
            case form:
                if (fieldNamesIgnoreForm.contains(fieldName)) {
                    return null;
                }
//                infoCopy.setClearable(false);
//                infoCopy.setDefaultValue(defaultDefaultValue.equals(defaultValue) ? null : defaultValue);
                break;
            case table:
                switch (fieldName) {
                    case "sort":
                        infoCopy.setWidth(80);
                        break;
                    case "enabled":
                        infoCopy.setWidth(70);
                        infoCopy.setAlign(EnumPositionAlign.center);
                        break;
                    case "createTime":
                        infoCopy.setWidth(98);
                        break;
                    case "remark":
                        infoCopy.setWidth(80);
                        break;
                    case "state":
                        infoCopy.setWidth(110);
                        break;
                }
                break;
            case base:
//                UITableInfo.FieldConfigOne fieldConfigOne = new UITableInfo.FieldConfigOne();
                infoCopy.setFieldName(configField.getFieldName());
                infoCopy.setLabel(StrUtils.or(I18nUtils.get(configField.getLabel()), configField.getLabel()));
                infoCopy.setUiType(defaultUiType.equals(configField.getUiType()) ? null : configField.getUiType());
                infoCopy.setMainLength(defaultMainLength.equals(configField.getMainLength()) ? null : configField.getMainLength());
                infoCopy.setDecimalLength(configField.getDecimalLength());
                infoCopy.setCharset(configField.getCharset());
                infoCopy.setRenameFrom(configField.getRenameFrom());
                infoCopy.setJavaTypeEnumClass(configField.getJavaTypeEnumClass());
                infoCopy.setJavaTypeEnumClassMultiple(trueOrNull(configField.getJavaTypeEnumClassMultiple()));
                infoCopy.setGroup(StrUtils.or(I18nUtils.get(configField.getGroup()), configField.getGroup()));
                infoCopy.setEntityName(configField.getEntityName());
                infoCopy.setEntityClass(configField.getEntityClass());
                infoCopy.setEntityClassKey(configField.getEntityClassKey());
                infoCopy.setEntityClassLabel(configField.getEntityClassLabel());
                infoCopy.setEntityClassTargetProp(configField.getEntityClassTargetProp());
                infoCopy.setExtendProp(configField.getExtendProp());
                infoCopy.setSort(configField.getSort());
                infoCopy.setDisabled(trueOrNull(configField.getDisabled()));
                infoCopy.setEntityName(configField.getEntityName());
                infoCopy.setClearable(trueOrNull(configField.getClearable()));
                infoCopy.setWidth(configField.getWidth());
                infoCopy.setAlign(configField.getAlign());
                infoCopy.setJavaTypeEnumClassName(null == configField.getJavaTypeEnumClass() ? null : ClassUtils.getClassName(configField.getJavaTypeEnumClass()));
                infoCopy.setDefaultValueObj(readDefaultValue(configField.getUiType(), configField.getDefaultValue()));
                infoCopy.setDescription(StrUtils.or(I18nUtils.get(configField.getDescription()), configField.getDescription()));
                infoCopy.setSortable(trueOrNull(configField.getSortable()));
                infoCopy.setNotNull(defaultNotNull.equals(configField.getNotNull()) ? null : configField.getNotNull());

                //                infoCopy.setDefaultValue(configField.getDefaultValue());
//                infoCopy.setAutoIncrement(trueOrNull(configField.getAutoIncrement()));
//                infoCopy.setDefaultValue(null);
//                infoCopy.setLabel(configField.getLabel());
//                infoCopy.setType(configField.getType());
                return infoCopy;
            default:
                throw new BusinessException("unknown uiArea " + uiArea);
        }
        return infoCopy;
    }
}
