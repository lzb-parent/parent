package com.pro.common.web.security.upload;

import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class FileUtil {

    /**
     * 判断是否是图片
     */
    public static boolean isImg(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).contains("image");
    }

    public static List<String> imgExt = Arrays.asList("jpg", "bmp", "jpeg", "png", "gif");

    /**
     * 判断是否是图片
     */
    public static boolean isImg(String name) {
        String extension = "";
        int i = name.lastIndexOf('.');
        if (i > 0) {
            extension = name.substring(i + 1);
        }
        return imgExt.contains(extension.toLowerCase());
    }

}
