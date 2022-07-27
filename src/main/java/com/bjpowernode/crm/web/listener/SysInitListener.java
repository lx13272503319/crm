package com.bjpowernode.crm.web.listener;

import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.settings.service.impl.DicServiceImpl;
import com.bjpowernode.crm.utils.ServiceFactory;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.*;

@WebListener
public class SysInitListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        System.out.println("上下文对象创建了");

//        取出上下文对象
        ServletContext application = event.getServletContext();

        DicService ds= (DicService) ServiceFactory.getService(new DicServiceImpl());
        Map<String, List<DicValue>> map=ds.getAll();

//        将map解析为上下文对象中保存的键值对
        Set<String> keys = map.keySet();
        for (String key : keys) {
            application.setAttribute(key,map.get(key));
        }

        System.out.println("数据字典处理完成");


        Map<String,String> pMap=new HashMap<>();
        ResourceBundle rb= ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
//            阶段
            String key = e.nextElement();
//            可能性
            String value = rb.getString(key);
            pMap.put(key,value);
        }
//        保存到服务器缓存中
        application.setAttribute("pMap",pMap);
    }
}
