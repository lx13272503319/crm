package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/activity/getUserList.do",
        "/workbench/activity/save.do","/workbench/activity/pageList.do",
        "/workbench/activity/delete.do","/workbench/activity/getUserListAndActivity.do",
        "/workbench/activity/update.do","/workbench/activity/detail.do",
        "/workbench/activity/getRemarkListByAid.do","/workbench/activity/deleteRemark.do",
        "/workbench/activity/saveRemark.do","/workbench/activity/updateRemark.do"})
public class ActivityController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if("/workbench/activity/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/activity/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/activity/pageList.do".equals(path)){
            pageList(request,response);
        }else if("/workbench/activity/delete.do".equals(path)) {
            delete(request, response);
        }else if("/workbench/activity/getUserListAndActivity.do".equals(path)) {
            getUserListAndActivity(request, response);
        }else if("/workbench/activity/update.do".equals(path)) {
            update(request, response);
        }else if("/workbench/activity/detail.do".equals(path)) {
            detail(request, response);
        }else if("/workbench/activity/getRemarkListByAid.do".equals(path)) {
            getRemarkListByAid(request, response);
        }else if("/workbench/activity/deleteRemark.do".equals(path)) {
            deleteRemark(request, response);
        }else if("/workbench/activity/saveRemark.do".equals(path)) {
            saveRemark(request, response);
        }else if("/workbench/activity/updateRemark.do".equals(path)) {
            updateRemark(request, response);
        }
    }

    private void updateRemark(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");
        String noteContent = request.getParameter("noteContent");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User)request.getSession().getAttribute("user")).getName();
        String editFlag="1";

        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setEditTime(editTime);
        ar.setEditBy(editBy);
        ar.setEditFlag(editFlag);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.updateRemark(ar);
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);
    }

    private void saveRemark(HttpServletRequest request, HttpServletResponse response) {

        String id=UUIDUtil.getUUID();
        String noteContent = request.getParameter("noteContent");
        String createTime=DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String editFlag="0";
        String activityId = request.getParameter("activityId");

        ActivityRemark ar=new ActivityRemark();
        ar.setId(id);
        ar.setNoteContent(noteContent);
        ar.setCreateTime(createTime);
        ar.setCreateBy(createBy);
        ar.setEditFlag(editFlag);
        ar.setActivityId(activityId);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.saveRemark(ar);
        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("ar",ar);
        PrintJson.printJsonObj(response,map);
    }

    private void deleteRemark(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.deleteRemark(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getRemarkListByAid(HttpServletRequest request, HttpServletResponse response) {

        System.out.println("666");
        String activityId = request.getParameter("activityId");

        ActivityService as = (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<ActivityRemark> arList=as.getRemarkListByAid(activityId);
        PrintJson.printJsonObj(response,arList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Activity activity=as.detail(id);
        request.setAttribute("activity",activity);
        request.getRequestDispatcher("/workbench/activity/detail.jsp").forward(request,response);
    }

    private void update(HttpServletRequest request, HttpServletResponse response) {

        String id= request.getParameter("id");
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String editTime= DateTimeUtil.getSysTime();
        String editBy=((User)request.getSession().getAttribute("user")).getName();

//      用对象来保存这些属性
        Activity activity=new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setEditTime(editTime);
        activity.setEditBy(editBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.update(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserListAndActivity(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
//        返回的数据使用率不高，使用map
        Map<String,Object> map=as.getUserListAndActivity(id);
        PrintJson.printJsonObj(response,map);
    }

    private void delete(HttpServletRequest request, HttpServletResponse response) {
        String[] ids = request.getParameterValues("id");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.delete(ids);
        PrintJson.printJsonFlag(response,flag);
    }

    private void pageList(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String pageNoStr = request.getParameter("pageNo");
        String pageSizeStr = request.getParameter("pageSize");

        int pageNo = Integer.valueOf(pageNoStr);
        int pageSize=Integer.valueOf(pageSizeStr);
//        计算略过的记录数
        int skipCount=(pageNo-1)*pageSize;

        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("skipCount",skipCount);
        map.put("pageSize",pageSize);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
//        使用VO类，方便其它模块调用
        PaginationVO<Activity> vo=as.pageList(map);
        PrintJson.printJsonObj(response,vo);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id= UUIDUtil.getUUID();
        String owner=request.getParameter("owner");
        String name=request.getParameter("name");
        String startDate=request.getParameter("startDate");
        String endDate=request.getParameter("endDate");
        String cost=request.getParameter("cost");
        String description=request.getParameter("description");
        String createTime= DateTimeUtil.getSysTime();
        String createBy=((User)request.getSession().getAttribute("user")).getName();

//      用对象来保存这些属性
        Activity activity=new Activity();
        activity.setId(id);
        activity.setOwner(owner);
        activity.setName(name);
        activity.setStartDate(startDate);
        activity.setEndDate(endDate);
        activity.setCost(cost);
        activity.setDescription(description);
        activity.setCreateTime(createTime);
        activity.setCreateBy(createBy);

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        Boolean flag=as.save(activity);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> ulist=us.getUserList();
        PrintJson.printJsonObj(response,ulist);
    }
}
