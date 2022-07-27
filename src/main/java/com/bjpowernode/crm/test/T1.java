package com.bjpowernode.crm.test;

import com.bjpowernode.crm.utils.MD5Util;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import org.apache.ibatis.session.SqlSession;

import javax.servlet.annotation.WebServlet;

@WebServlet("/T")
public class T1 {
    String name;
    String cno;

    public static void main(String[] args) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession();
    }
}
