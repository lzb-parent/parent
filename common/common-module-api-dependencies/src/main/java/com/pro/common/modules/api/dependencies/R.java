package com.pro.common.modules.api.dependencies;

import com.pro.common.modules.api.dependencies.model.IResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.Data;

import java.util.Map;

/**
 * 通用返回数据
 */
@ApiModel(description = "响应")
@Data
public class R<T> implements IResponse<T> {
    public static final Integer SUCCESS = 0;
    @ApiParam("响应码")
    private Integer code = SUCCESS;
    @ApiParam("响应成功内容")
    private T data;
    @ApiParam("响应错误信息")
    private String msg;
    @ApiParam("响应时间戳")
    private Long timestamp;


    @ApiParam("文章编号")
    private String posterCode;
    @ApiParam("文章参数")
    private Map<String, Object> paramMap;

    public static <T> R<T> ok() {
        return ok(null);
    }

    public static <T> R<T> ok(T data) {
        R<T> r = new R<>();
        r.setData(data);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }

    public static <T> R<T> fail(Integer code, String errorMsg) {
        R<T> r = new R<>();
        r.setCode(code);
        r.setMsg(errorMsg);
        r.setTimestamp(System.currentTimeMillis());
        return r;
    }
}
