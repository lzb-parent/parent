package com.pro.common.modules.service.dependencies.util.upload;


import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.framework.api.structure.Tuple2;
import com.pro.framework.api.util.StrUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FileUploadUtils {
    private static CommonProperties commonProperties;
    public static final String FILE_PREPEND = "{host}";

    @Autowired
    public void setCommonProperties(CommonProperties commonProperties) {
        this.commonProperties = commonProperties;
    }

    public static List<String> uploadFiles(MultipartFile[] files, String moduleCode, String sign) {
        if (Objects.isNull(files)) {
            throw new BusinessException("上传失败_文件不能为空");
        }
        UploadModuleModel module = commonProperties.getFiles().getModules().get(moduleCode);
// && !"".equals(module.getSalt())
        if (StrUtils.isNotBlank(sign)) {
            String okSign = calSign(files[0].getName());
            if (!okSign.equals(sign)) {
                throw new BusinessException("上传密匙uploadPassword错误!,请重试");
            }
            if (null == module) {
                module = new UploadModuleModel();
                module.setCode(moduleCode);
            }
        } else {
            if (null == module) {
                log.error("上传失败|模块不存在,请先在yml中定义模块" + moduleCode);
                throw new BusinessException("upload module not exist:" + moduleCode);
            }
            if (files[0].getSize() <= 10) {
                throw new BusinessException("upload file size cannot be 10b");
            }
        }

        LocalDateTime time = LocalDateTime.now();
        UploadModuleModel finalModule = module;
        return Arrays.stream(files).map(file -> uploadFile(new UploadVo(file), finalModule, time)).collect(Collectors.toList());
    }

    /**
     * 新建文件
     */
    @SneakyThrows
    public static Tuple2<String, String> newFile(UploadModuleModel moduleEnum, LocalDateTime time, String fileSubFolder, String fileName) {
        String relativePath = moduleEnum.getPath(time, fileSubFolder) + File.separator + fileName;
        String fullPath = commonProperties.getFiles().getSavePath() + relativePath;
        new File(fullPath).createNewFile();
        return new Tuple2<>((File.separator + "file" + relativePath).replaceAll("\\\\", File.separator), fullPath);
    }

    /**
     * 新建目录
     */
    @SneakyThrows
    public static Tuple2<String, String> newFolder(UploadModuleModel moduleEnum, LocalDateTime time, String fileSubFolder) {
        String relativePath = moduleEnum.getPath(time, fileSubFolder) + File.separator;
        String fullPath = commonProperties.getFiles().getSavePath() + relativePath;
        new File(fullPath).mkdirs();
        return new Tuple2<>((File.separator + "file" + relativePath).replaceAll("\\\\", File.separator), fullPath);
    }

    /**
     * 上传文件
     *
     * @param uploadFile 文件内容
     * @param moduleEnum 上传模块
     * @param time       目录时间(可以是上传时间/用户创建时间等等)
     * @return 文件路径
     */
    @SneakyThrows
    public static String uploadFile(UploadVo uploadFile, UploadModuleModel moduleEnum, LocalDateTime time) {
        String oriName = uploadFile.getOriName();
        Integer maxSize = moduleEnum.getMaxSize();
        if (null != maxSize && uploadFile.getSize() / (1024 * 1024) > maxSize) {
            throw new BusinessException("file max size: " + maxSize + "M");
        }
        //相对路径 (包括文件名,相对于根路径)
//        String relativePath = moduleEnum.getPath(time) + File.separator + IdUtil.simpleUUID() + "_" + oriName;
        String relativePath = moduleEnum.getPath(time, null) + File.separator + IdUtil.simpleUUID() + uploadFile.getSuffix();
        File saveFile = new File(commonProperties.getFiles().getSavePath() + relativePath);
        boolean dirOK = true;
        if (!saveFile.getParentFile().exists()) {
            dirOK = saveFile.getParentFile().mkdirs();
        }
        boolean newFile = saveFile.createNewFile();
        if (!dirOK || !newFile) {
            throw new BusinessException("创建文件上传失败.");
        }

        // 默认不压缩
        //如果是图片,并且要压缩,并且图片过大
//        if (uploadFile.getIsImg() && moduleEnum.getImgCompress() && uploadFile.getSize() > moduleEnum.getImgCompressSize()) {
//            float rate = uploadFile.getSize() / ((float) moduleEnum.getImgCompressSize());
//            //统一转jpeg格式,压缩到1兆(若需要)
//            @Cleanup FileOutputStream outputStream = new FileOutputStream(saveFile);
//            @Cleanup InputStream inputStream1 = uploadFile.getInputStream();
//            ImageUtil.scale(inputStream1, outputStream, rate);
//
//        } else {
//        FileTypeUtil.getType(saveFile)
//        }
        uploadFile.transferTo(saveFile);
        uploadFile.inputStreamClose();

        return (File.separator + "file" + relativePath).replaceAll("\\\\", File.separator);
    }

    public static String calSign(String fileName) {
        String data = commonProperties.getFiles().getUploadPassword() + fileName + +System.currentTimeMillis() / 1000 / 1000;
        log.info("上传文件|签名前数据:" + data);
        return SecureUtil.md5(data);
    }
}
