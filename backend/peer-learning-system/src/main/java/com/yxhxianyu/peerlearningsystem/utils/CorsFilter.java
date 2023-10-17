package com.yxhxianyu.peerlearningsystem.utils;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理跨域请求
 * @author YXH_XianYu
 * @date 2023/10/17 20:24
 **/
@Component
public class CorsFilter implements Filter {
    /**
     * 跨域请求Filter
     * 此方法来自ChatGPT3.5
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setHeader("Access-Control-Allow-Origin", "http://localhost:10087"); // 允许的前端域名
        httpResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE"); // 允许的请求方法
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Auth-Token, Origin, Authorization"); // 允许的请求头
        chain.doFilter(request, response);
    }
}
