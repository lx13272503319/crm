package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;

import javax.jws.WebService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/settings/user/login.do")
public class UserController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if("/settings/user/login.do".equals(path)){
            System.out.println(path);
            login(request,response);
        }else if("".equals(path)){

        }
    }

    private void login(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String pwd = request.getParameter("pwd");
        String ip = request.getRemoteAddr();
//        System.out.println(username+pwd+ip);

        pwd= MD5Util.getMD5(pwd);

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());


        try{
//            如果账号密码错误，login()会抛出异常，阻止session操作
            User user=us.login(username,pwd,ip);
            System.out.println(user);
            request.getSession().setAttribute("user",user);
//            返回success为true的json字符串
            PrintJson.printJsonFlag(response,true);
        }catch (Exception e){
            e.printStackTrace();
            Map<String,Object> map=new HashMap<>();
            map.put("success",false);
            map.put("msg",e.getMessage());
            PrintJson.printJsonObj(response,map);
        }

    }
}
