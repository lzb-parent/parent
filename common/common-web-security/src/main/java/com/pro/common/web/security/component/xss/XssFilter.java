package com.pro.common.web.security.component.xss;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Description: 拦截防止注入漏洞（即防止XSS的跨站脚本攻击）
 */
@Slf4j
public class XssFilter implements Filter {
    private FilterConfig filterConfig = null;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.debug("XssFilter过滤器初始化");
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("执行XssFilter过滤器初始化");
        String contentType = request.getContentType();
        if (null != contentType && contentType.contains("multipart/form-data")) {
            filterChain.doFilter(request, response);
        } else {
            filterChain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
        }
    }

    @Override
    public void destroy() {
        log.debug("销毁XssFilter过滤器初始化");
        this.filterConfig = null;
    }
}
