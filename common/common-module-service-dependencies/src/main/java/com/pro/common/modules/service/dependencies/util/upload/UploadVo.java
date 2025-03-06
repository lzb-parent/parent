package com.pro.common.modules.service.dependencies.util.upload;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.util.ObjUtil;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

@Data
public class UploadVo {
    String oriName;
    String suffix;
    String pre;
    Long size;
    InputStream inputStream;
    Boolean isImg;
//    byte[] bytes;

    @SneakyThrows
    public UploadVo(MultipartFile file) {
        this.oriName = ObjUtil.defaultIfNull(file.getOriginalFilename(), "");
        this.suffix = this.oriName.substring(this.oriName.lastIndexOf("."));
        this.size = file.getSize();
        this.inputStream = file.getInputStream();
        this.isImg = FileUtil.isImg(file);
    }

    @SneakyThrows
    public UploadVo(String base64, String oriName) {
        int indexOf = base64.indexOf("base64,");
        if (indexOf != -1) {
            //去掉base64头部(html中Img.url才需要)
            base64 = base64.substring(indexOf + 7);
        }
        BufferedImage bufferedImage = ImgUtil.toImage(base64);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "gif", os);
        this.inputStream = new ByteArrayInputStream(os.toByteArray());
        this.oriName = ObjUtil.defaultIfNull(oriName, "");
        this.suffix = this.oriName.substring(this.oriName.lastIndexOf("."));
        this.isImg = FileUtil.isImg(oriName);
        this.size = (long) base64.length();
    }

    public void transferTo(File saveFile) {
        cn.hutool.core.io.FileUtil.writeFromStream(inputStream, saveFile);
    }

    @SneakyThrows
    public void inputStreamClose() {
        if(null!=inputStream){
            inputStream.close();
        }
    }
}
