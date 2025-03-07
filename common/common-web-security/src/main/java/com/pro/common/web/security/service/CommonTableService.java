package com.pro.common.web.security.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.model.classes.*;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.common.web.security.model.request.JTDTableInfoRequest;
import com.pro.framework.api.FrameworkConst;
import com.pro.framework.api.entity.IEntityProperties;
import com.pro.framework.api.util.ClassUtils;
import com.pro.framework.api.util.CollUtils;
import com.pro.framework.api.util.StrUtils;
import com.pro.framework.javatodb.constant.JTDConst;
import com.pro.framework.javatodb.enums.EnumPositionAlign;
import com.pro.framework.javatodb.enums.EnumUIArea;
import com.pro.framework.javatodb.model.JTDFieldInfoDbVo;
import com.pro.framework.javatodb.model.JTDTableInfoVo;
import com.pro.framework.javatodb.model.UITableInfo;
import com.pro.framework.javatodb.service.IJTDService;
import com.pro.framework.mtq.service.multiwrapper.util.MultiClassRelationFactory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.pro.framework.javatodb.constant.JTDConst.EnumAdminButton.*;

/**
 *
 */
@Service
@AllArgsConstructor
public class CommonTableService {
    private IEntityProperties entityProperties;
    private IJTDService jtdService;


    public JTDTableInfoVo getTableSimpleInfo(String entityClassName) {
        JTDTableInfoVo tableInfo = jtdService.readTableInfo(
                MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName));
        if (tableInfo == null) {
            int i = 0;
        }
        List<JTDFieldInfoDbVo> fields = tableInfo.getFields();
//        tableInfo.setTableName(StrUtil.toCamelCase(tableInfo.getTableName()));
        fields.forEach(field ->
                {
                    field.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                    Class<?> entityClass = field.getEntityClass();
                    if (null != entityClass && !Object.class.equals(entityClass)) {
                        field.setEntityName(StrUtils.firstToLowerCase(entityClass.getSimpleName()));
                    }
                }
        );
        return tableInfo;
    }


    private static final List<String> fieldNamesSortEnd = Arrays.asList("enabled", "remark", "sort", "isDemo", "createTime");
    private static final Set<String> fieldNamesIgnore = new HashSet<>(Arrays.asList("deleted", "updateTime"));
    private static final Set<String> fieldNamesIgnoreForm = new HashSet<>(Arrays.asList("createTime", "isDemo"));
    private static final Integer defaultMainLength = 200;
    private static final JTDConst.EnumFieldNullType defaultNotNull = JTDConst.EnumFieldNullType.not_null;
    private static final JTDConst.EnumFieldEmptyType defaultNotEmpty = JTDConst.EnumFieldEmptyType.can_empty;
    private static final JTDConst.EnumFieldUiType defaultUiType = JTDConst.EnumFieldUiType.text;

    public UITableInfo getTableStruct(String entityClassName, JTDTableInfoRequest request) {
        Class<?> clazz = MultiClassRelationFactory.INSTANCE.getEntityClass(entityClassName);
        UITableInfo jtdTableInfoVo = new UITableInfo();
        if (clazz != null) {
            JTDTableInfoVo tableInfo = jtdService.readTableInfo(clazz);
            String entityName = tableInfo.getEntityName();

            List<JTDFieldInfoDbVo> fields = tableInfo.getFields();
            // 字段名改驼峰
            //noinspection UnnecessaryLocalVariable
//            String tableName = entityClassName;
//            tableInfo.setTableName(tableName);
            tableInfo.setLabel(I18nUtils.get(tableInfo.getLabel()));
            fields.forEach(field ->
                    {
                        field.setFieldName(StrUtil.toCamelCase(field.getFieldName()));
                        field.setDefaultValue(field.getDefaultValue().replaceAll("'", ""));
                        Class<?> fieldEntityClass = field.getEntityClass();
                        if (null != fieldEntityClass && !Object.class.equals(fieldEntityClass)) {
                            field.setEntityName(StrUtils.firstToLowerCase(fieldEntityClass.getSimpleName()));
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
                    .toList();
            List<JTDFieldInfoDbVo> fieldsSort = fields.stream()
                    .filter(vo -> null != vo.getSort() && vo.getSort() >= 0)
                    .toList();
            List<JTDFieldInfoDbVo> fieldsNoSort = fields.stream()
                    .filter(vo -> null == vo.getSort() || vo.getSort() < 0)
                    .toList();
            List<String> fieldNames = fieldsNoSort.stream()
                    .map(JTDFieldInfoDbVo::getFieldName)
                    .filter(n -> !"id".equals(n))
                    .collect(Collectors.toList());
            fieldsSort.forEach(fieldSort -> fieldNames.add(fieldSort.getSort(), fieldSort.getFieldName()));


            UITableInfo.TableConfigOne tableConfigOne = new UITableInfo.TableConfigOne();
            BeanUtil.copyProperties(tableInfo, tableConfigOne);
            tableConfigOne.setFieldNames(fieldNames);

            String urlTemplate = request.getUrlTemplate();
            tableConfigOne.setGetOneUrl(getUrl(urlTemplate, "selectOne", entityName));
            tableConfigOne.setInsertUrl(getUrl(urlTemplate, "insert", entityName));
            tableConfigOne.setUpdateUrl(getUrl(urlTemplate, "update", entityName));
            tableConfigOne.setGetPageUrl(getUrl(urlTemplate, "selectPage", entityName));
            tableConfigOne.setDeleteUrl(getUrl(urlTemplate, "delete", entityName));
            tableConfigOne.setExportUrl(getUrl(urlTemplate, "export", entityName));

            if (IUserOrderClass.class.isAssignableFrom(clazz)) {
                tableConfigOne.setAdminButtons(new JTDConst.EnumAdminButton[]{query, edit});
            } else if (IUserDataClass.class.isAssignableFrom(clazz)) {
                tableConfigOne.setAdminButtons(new JTDConst.EnumAdminButton[]{query, add, edit, delete});
            } else if (IOpenConfigClass.class.isAssignableFrom(clazz) || IAdminClass.class.isAssignableFrom(clazz)) {
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

        }
        return jtdTableInfoVo;
    }


    private static String getUrl(String urlTemplate, String option, String entityName) {
        return StrUtil.format(urlTemplate, Map.of("option", option, "entityName", entityName));
    }
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
                        infoCopy.setWidth(80);
                        infoCopy.setAlign(EnumPositionAlign.center);
                        break;
                    case "createTime":
                        infoCopy.setWidth(98);
                        break;
                    case "remark":
                        infoCopy.setWidth(90);
                        break;
                    case "state":
                        infoCopy.setWidth(110);
                        break;
                }
                break;
            case base:
//                UITableInfo.FieldConfigOne fieldConfigOne = new UITableInfo.FieldConfigOne();
                infoCopy.setFieldName(configField.getFieldName());
                infoCopy.setLabel(StrUtils.or(I18nUtils.get(StrUtils.replaceSpecialToUnderline(configField.getLabel())), configField.getLabel()));
                infoCopy.setUiType(defaultUiType.equals(configField.getUiType()) ? null : configField.getUiType());
                infoCopy.setMainLength(
                        defaultMainLength.equals(configField.getMainLength()) ? null : configField.getMainLength());
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
                infoCopy.setJavaTypeEnumClassName(
                        null == configField.getJavaTypeEnumClass() ? null : ClassUtils.getClassName(
                                configField.getJavaTypeEnumClass()));
                infoCopy.setDefaultValueObj(readDefaultValue(configField.getUiType(), configField.getDefaultValue()));
                infoCopy.setDescription(
                        StrUtils.or(I18nUtils.get(configField.getDescription()), configField.getDescription()));
                infoCopy.setSortable(trueOrNull(configField.getSortable()));
                infoCopy.setNotNull(defaultNotNull.equals(configField.getNotNull()) ? null : configField.getNotNull());
                infoCopy.setNotEmpty(
                        defaultNotEmpty.equals(configField.getNotEmpty()) ? null : configField.getNotEmpty());

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

    private static Boolean trueOrNull(Boolean bool) {
        return null == bool ? null : (bool ? true : null);
    }


    /**
     *
     */
    private UITableInfo.TableConfigOne getUIAreaTable(JTDTableInfoVo tableInfo, EnumUIArea uiArea, UITableInfo.TableConfigOne infoOri) {
//        ObjectUtil.clone(tableConfigOne)
        UITableInfo.TableConfigOne infoNew = new UITableInfo.TableConfigOne();
//        String tableName = tableInfo.getTableName();
        Map<String, JTDFieldInfoDbVo> fieldMap = CollUtils.listToMap(tableInfo.getFields(),
                JTDFieldInfoDbVo::getFieldName);
        // 排序
        List<String> fieldNamesOri = infoOri.getFieldNames();
        Set<String> fieldUserDataId = fieldNamesOri.stream().filter(fieldName -> {
            JTDFieldInfoDbVo field = fieldMap.get(fieldName);
            Class<?> entityClass = field.getEntityClass();
            return null != entityClass && IUserClass.class.isAssignableFrom(entityClass) && Objects.equals(
                    field.getEntityClassTargetProp(), "id");
        }).collect(Collectors.toSet());
        List<String> fieldNamesNotId = fieldNamesOri.stream()
                .filter(fieldName -> !fieldUserDataId.contains(fieldName))
                .collect(Collectors.toList());
        List<String> fieldNames;
        boolean isEdit;
        switch (uiArea) {
            case search:
                List<JTDConst.EnumFieldUiType> uiTypes = List.of(JTDConst.EnumFieldUiType.text,
                        JTDConst.EnumFieldUiType.select);
                fieldNames = fieldNamesNotId.stream()
                        .filter(fieldName -> uiTypes.contains(fieldMap.get(fieldName).getUiType()))
                        .collect(Collectors.toList());
                isEdit = true;
                break;
            case form:
                // 如果有关联实体 只显示id选择框,不显示其他属性
                fieldNames = fieldNamesOri.stream()
                        .filter(fieldName -> !fieldNamesIgnoreForm.contains(fieldName))
                        .filter(fieldName -> {
                            String entityClassTargetProp = fieldMap.get(fieldName).getEntityClassTargetProp();
                            return null == fieldMap.get(fieldName).getEntityClass() || Objects.equals(
                                    entityClassTargetProp, "code") || Objects.equals(entityClassTargetProp, "id");
                        })
                        .collect(Collectors.toList());
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

    private Object readDefaultValue(JTDConst.EnumFieldUiType uiType, String defaultValue) {
        if (!defaultValue.isEmpty()) {
            if (defaultValue.startsWith("'") || defaultValue.startsWith("\"")) {
                defaultValue = defaultValue.substring(1, defaultValue.length() - 1);
            }
            if (FrameworkConst.Str.Sql.NULL.equals(defaultValue)) {
                return null;
            }
            if (!defaultValue.isEmpty()) {
                //noinspection
                switch (uiType) {
                    case number:
                        return new BigDecimal(defaultValue);
                    case bool:
                        return FrameworkConst.Str.TRUE.equals(defaultValue) || FrameworkConst.Num.TRUE.toString()
                                .equals(defaultValue);
                    case datetime:
                        if (FrameworkConst.Str.Sql.CURRENT_TIMESTAMP.equals(defaultValue)) {
                            return null;
                        }
                    default:
                        return defaultValue;
                }
            }
        }
        return null;
    }

}
