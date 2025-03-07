package com.pro.common.web.security.service;

import cn.hutool.core.io.resource.InputStreamResource;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.service.dependencies.util.upload.FileUploadUtils;
import com.pro.framework.api.util.StrUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class OtherService {

    /**
     * 管理端 统一上传文件到 用户端(方便统一浏览)
     * 做管理端签名验证(可以自定义上传存储路径)
     */
    public List<String> uploadAdmin(MultipartFile[] files, String module) {
        String host = EnumAuthDict.FILE_UPLOAD_DOMAIN.getValueCache();
        String msg;
        if (StrUtils.isNotBlank(host)) {
            String body = null;
            try {
                HttpRequest post = HttpUtil.createPost(host + "/common/uploadPre");
                MultipartFile file = files[0];
                post.form("file", new InputStreamResource(file.getInputStream(), file.getOriginalFilename()));
                post.form("module", module);
                post.form("sign", FileUploadUtils.calSign(file.getName()));
                log.error("sign::" + FileUploadUtils.calSign(file.getName()));
                HttpResponse httpResponse = null;
                try {
                    httpResponse = post.executeAsync();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    if (httpResponse != null) {
                        httpResponse.close();
                    }
                }
                body = null == httpResponse ? null : httpResponse.body();

//                JsonElement element = JSON_PARSER.parse(body);
                if (JSONUtil.isTypeJSONObject(body)) {
                    JSONObject rs = JSONUtil.parseObj(body);
                    String code = rs.getStr("code");
                    if ("0".equals(code)) {
                        return rs.getJSONArray("data").toList(String.class);
                    } else {
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
}
