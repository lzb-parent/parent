package com.pro.common.modules.service.dependencies.modelauth.base;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MultipartFilePart implements MultipartFile, Part {
    private final MultipartFile multipartFile;
    private final Map<String, String> headers;

    public MultipartFilePart(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
        this.headers = new HashMap<>();
        headers.put("Content-Disposition", "form-data; name=\"" + multipartFile.getName() + "\"; filename=\"" + multipartFile.getOriginalFilename() + "\"");
        headers.put("Content-Type", multipartFile.getContentType());
    }

    // MultipartFile methods
    @Override
    public String getName() {
        return multipartFile.getName();
    }

    @Override
    public String getOriginalFilename() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public String getContentType() {
        return multipartFile.getContentType();
    }

    @Override
    public boolean isEmpty() {
        return multipartFile.isEmpty();
    }

    @Override
    public long getSize() {
        return multipartFile.getSize();
    }

    @Override
    public byte[] getBytes() throws IOException {
        return multipartFile.getBytes();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return multipartFile.getInputStream();
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        multipartFile.transferTo(dest);
    }

    // Part methods
    @Override
    public String getSubmittedFileName() {
        return multipartFile.getOriginalFilename();
    }

    @Override
    public void write(String fileName) throws IOException {
        multipartFile.transferTo(new java.io.File(fileName));
    }

    @Override
    public void delete() throws IOException {
        // 删除文件逻辑（可选）
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }

    @Override
    public Collection<String> getHeaders(String name) {
        return headers.containsKey(name) ? List.of(headers.get(name)) : List.of();
    }

    @Override
    public Collection<String> getHeaderNames() {
        return headers.keySet();
    }
}
