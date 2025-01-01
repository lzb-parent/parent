package com.pro.common.web.security.component;

import cn.hutool.core.util.StrUtil;
import com.pro.common.module.api.system.model.enums.EnumAuthDict;
import com.pro.common.modules.api.dependencies.R;
import com.pro.common.modules.api.dependencies.enums.EnumApplication;
import com.pro.common.modules.api.dependencies.exception.BusinessException;
import com.pro.common.modules.api.dependencies.exception.BusinessPosterException;
import com.pro.common.modules.api.dependencies.model.IResponse;
import com.pro.common.modules.service.dependencies.properties.CommonProperties;
import com.pro.common.modules.service.dependencies.util.I18nUtils;
import com.pro.framework.api.model.FrameworkException;
import com.pro.framework.api.util.LogicUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketException;

/**
 * 异常处理器
 */
@RestControllerAdvice
@Slf4j
public class BaseExceptionHandler {
    @Autowired
    private CommonProperties commonProperties;

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public IResponse<?> handleNotFound(NoHandlerFoundException ex) {
        return R.fail(404, "Resource not found");
    }

    @ExceptionHandler(IOException.class)
    public IResponse<?> handleIOException(HttpServletRequest request, IOException e) {
        // 判断是否为客户端中断（例如 SocketException）
        if (e instanceof SocketException) {
            // 用户端中断http连接 异常需要进行无视处理,不然日志太多
            log.info("IOException {} {}", request.getRequestURI(), e.getMessage());

        } else {
            log.warn("IOException {} {}", request.getRequestURI(), e.getMessage(), e);
        }
        return R.fail(500, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public IResponse<?> handleException(HttpServletRequest request, Exception e) {
        String exceptionClass = e.getClass().getSimpleName();
        Integer code = 500;
        String msg = e.getMessage();
        if (e.getCause() != null) {
            msg = e.getCause().getMessage();
        }
//        else {
        switch (exceptionClass) {
            case "BusinessException":
            case "BusinessPosterException":
                log.info("{} {} {}", exceptionClass, request.getRequestURI(), logThrowable(e));
                return translate((BusinessException) e);
            case "FrameworkException":
                msg = LogicUtils.or(I18nUtils.get(msg, ((FrameworkException) e).getParams()), msg);
                log.info("{} {} {}", exceptionClass, request.getRequestURI(), logThrowable(e));
                break;
            case "DuplicateKeyException":
                msg = I18nUtils.get("数据重复") + msg;
                log.warn("{} {} {} {}", exceptionClass, request.getRequestURI(), msg, logThrowable(e));
                break;
            case "DataIntegrityViolationException":
                msg = e.getCause().getMessage();
                if ("Field 'user_id' doesn't have a default value".equals(msg)) {
                    msg = EnumApplication.user.equals(commonProperties.getApplication()) ? I18nUtils.get("请先选择登录") : I18nUtils.get("请先选择用户");
                }
                log.warn("{} {} {} {}", exceptionClass, request.getRequestURI(), msg, logThrowable(e));
                break;
            default:
                msg = I18nUtils.get("未知的异常_请联系管理员");
                log.warn("{} {} {}", exceptionClass, request.getRequestURI(), msg, e);
                break;
        }
        // 没太大意义，但是加了会不方便后期的一些截图，先去掉
//        if (EnumEnv.dev.equals(commonProperties.getEnv())) {
//            msg = exceptionClass + " " + msg;
////            log.debug("{} {} {}", exceptionClass, msg, request.getRequestURI(), e);
//        }
        return R.fail(code, msg);
    }

    private IResponse<?> translate(BusinessException e) {
        String errorMsg = I18nUtils.get(e.getMsg(), e.getParams());
        if (StrUtil.isBlank(errorMsg)) {
            switch (commonProperties.getApplication()) {
                case admin:
                    if (EnumAuthDict.ADMIN_LANG_CHINESE.getValueCacheOrDefault(true)) {
                        errorMsg = e.getMsg();
                    }
                    break;
            }
        }
        R<?> rs = R.fail(e.getCode(), errorMsg);
        if (e instanceof BusinessPosterException) {
            rs.setPosterCode(((BusinessPosterException) e).getPosterCode());
            rs.setParamMap(((BusinessPosterException) e).getParamMap());
        }
        return rs;
    }

    public static String logThrowable(Throwable e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        StringBuilder s = new StringBuilder(e.getMessage() + "\n");
        for (StackTraceElement stackTraceElement : stackTrace) {
            String temp = stackTraceElement.toString().trim();
            if (temp.startsWith("com.pro.") && temp.contains(":")) {
                s.append("	").append(temp).append("\n");
            }
        }
        if (s.length() == 0) {
            for (StackTraceElement stackTraceElement : stackTrace) {
                s.append("	").append(stackTraceElement.toString()).append("\n");
            }
        }
        Throwable cause = e.getCause();
        if (cause != null && e != cause) {
            s.append("	").append(logThrowable(cause)).append("\n");
        }
        return s.toString();
    }

}
