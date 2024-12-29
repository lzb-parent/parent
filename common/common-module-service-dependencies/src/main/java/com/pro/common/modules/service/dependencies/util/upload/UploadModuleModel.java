package com.pro.common.modules.service.dependencies.util.upload;

import com.pro.common.modules.service.dependencies.util.MathUtils;
import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 上传的模块
 */
@Data
public class UploadModuleModel {
    String code;                    //上传必须制定模块code
    String label;                   //很少用,主要开发查看
    Boolean dateFolder = false;     //日期分割文件夹  方便按日期清理  分两级文件夹 [模块文件夹 每个月文件夹 每天文件夹 每个文件]
    Integer maxSize = null;           //文件最大大小 以M为单位
    String salt = "_%(*UH)_";           //文件最大大小 以M为单位
    List<String> types = null;      //文件格式限制

    /**
     * 模块上传 对应路径  (文件路径 统一为 前面拼接分隔符,后面不拼接 如: /folder1/subFolder1 )
     */
    public String getPath(LocalDateTime time, String fileSubFolder) {
        List<String> list = new ArrayList<>();
        list.add(File.separator + code);
        if (dateFolder) {//按照日期分目录
            list.add(File.separator + time.getYear() + MathUtils.appendZore(time.getMonthValue(), 2));
            list.add(File.separator + time.format(DateTimeFormatter.ofPattern("dd")));
        }
        if (fileSubFolder != null) {
            list.add(File.separator + fileSubFolder);
        }
        return String.join("", list);
    }
//    Boolean imgCompress;//图片压缩
//    Long imgCompressSize;//图片起始压缩大小
}
