package com.pro.common.modules.service.dependencies.properties;

import com.pro.common.modules.api.dependencies.enums.EnumEnv;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.service.dependencies.util.upload.UploadModuleModel;
import com.pro.framework.api.entity.IEntityProperties;
import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Data
@Component
@ConfigurationProperties(prefix = "common")
public class CommonProperties implements IEntityProperties {
    /**
     * 本地/测试/生产
     */
    private EnumEnv env;
    /**
     * user/agent/admin/pay
     */
    private EnumApplication application;
    /**
     * ai/store/
     */
    private String platform;
    /**
     * 公开路径
     */
    private List<String> publicPaths = Collections.emptyList();
    /**
     * 角色token默认时效(秒)
     */
    private Map<String, Integer> roleTokenValidSecondMap = Collections.emptyMap();
    private Files files;

    @Data
    public static class Files {
        //(admin)上传文件到user端,添加的密码要求
        String uploadPassword;
        //上传文件保存路径
        String savePath;
        //上传文件保存路径
        Map<String,UploadModuleModel> modules;
    }

    /**
     * 定制实体类
     * 例如:
     * User: com.pro.snowball.api.model.db.Usersnowball
     * UserMoney: com.pro.xxx.api.model.db.UserMoneyXxx
     */
    private Map<String, Class<?>> entityClassReplaceMap = new ConcurrentHashMap<>();
}
