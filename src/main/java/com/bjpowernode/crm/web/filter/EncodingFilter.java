package com.bjpowernode.crm.web.filter;

import javax.servlet.*;
import java.io.IOException;

public class EncodingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
//        设置请求和响应的字符编码
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/html;charset=utf-8");
//        System.out.println(666);
        filterChain.doFilter(req,resp);
    }
}
