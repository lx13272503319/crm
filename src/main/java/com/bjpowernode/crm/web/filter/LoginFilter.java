package com.bjpowernode.crm.web.filter;

import com.bjpowernode.crm.settings.domain.User;
import com.sun.deploy.net.HttpRequest;
import com.sun.deploy.net.HttpResponse;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
//        将servletRequest转换为Http
        HttpServletRequest request= (HttpServletRequest) req;
        HttpServletResponse response= (HttpServletResponse) resp;

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String path = request.getServletPath();
//        如果user不为空或要访问登录页，则放行
        if("/login.jsp".equals(path) || "/settings/user/login.do".equals(path)){
            filterChain.doFilter(req,resp);
        }else if(user!=null){
            filterChain.doFilter(req,resp);
        }else{// 其余情况重定向到登录页
            response.sendRedirect(request.getContextPath()+"/login.jsp");
        }
    }
}
