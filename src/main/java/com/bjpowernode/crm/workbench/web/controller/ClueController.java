package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.service.ActivityService;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.bjpowernode.crm.workbench.service.impl.ActivityServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.ClueServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

@WebServlet({"/workbench/clue/getUserList.do","/workbench/clue/save.do",
            "/workbench/clue/detail.do","/workbench/clue/getActivityListByClueId.do",
            "/workbench/clue/unbond.do","/workbench/clue/getActivityListByNameAndNotByClueId.do",
            "/workbench/clue/bund.do","/workbench/clue/getActivityListByName.do",
            "/workbench/clue/convert.do"})
public class ClueController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();

        if("/workbench/clue/getUserList.do".equals(path)){
            getUserList(request,response);
        }else if("/workbench/clue/save.do".equals(path)){
            save(request,response);
        }else if("/workbench/clue/detail.do".equals(path)){
            detail(request,response);
        }else if("/workbench/clue/getActivityListByClueId.do".equals(path)){
            getActivityListByClueId(request,response);
        }else if("/workbench/clue/unbond.do".equals(path)){
            unbond(request,response);
        }else if("/workbench/clue/getActivityListByNameAndNotByClueId.do".equals(path)){
            getActivityListByNameAndNotByClueId(request,response);
        }else if("/workbench/clue/bund.do".equals(path)){
            bund(request,response);
        }else if("/workbench/clue/getActivityListByName.do".equals(path)){
            getActivityListByName(request,response);
        }else if("/workbench/clue/convert.do".equals(path)){
            convert(request,response);
        }else if("/workbench/clue/xxx.do".equals(path)){
//            xxx(request,response);
        }else if("/workbench/clue/xxx.do".equals(path)){
//            xxx(request,response);
        }else if("/workbench/clue/xxx.do".equals(path)){
//            xxx(request,response);
        }
    }

    private void convert(HttpServletRequest request, HttpServletResponse response)  {

        String flag = request.getParameter("flag");
        String clueId = request.getParameter("clueId");
        String createBy=((User)request.getSession().getAttribute("user")).getName();

        Tran tran=null;
        if("f".equals(flag)){
            tran=new Tran();

//            接收交易表中的参数
            String id =UUIDUtil.getUUID();
            String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");
            String createTime=DateTimeUtil.getSysTime();

            tran.setId(id);
            tran.setMoney(money);
            tran.setName(name);
            tran.setExpectedDate(expectedDate);
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setCreateBy(createBy);
            tran.setCreateTime(createTime);
        }

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean f= null;
        try {
            f = cs.convert(clueId,tran,createBy);
            if(f){
//            重定向到线索页
                response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void getActivityListByName(HttpServletRequest request, HttpServletResponse response) {

        String aName = request.getParameter("aName");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList=as.getActivityListByName(aName);
        PrintJson.printJsonObj(response,aList);
    }

    private void bund(HttpServletRequest request, HttpServletResponse response) {

        String[] aids = request.getParameterValues("aid");
        String cid = request.getParameter("cid");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=cs.bund(aids,cid);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByNameAndNotByClueId(HttpServletRequest request, HttpServletResponse response) {

        String aName = request.getParameter("aName");
        String clueId = request.getParameter("clueId");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList=as.getActivityListByNameAndNotByClueId(aName,clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void unbond(HttpServletRequest request, HttpServletResponse response) {

        String id = request.getParameter("id");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=cs.unbond(id);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getActivityListByClueId(HttpServletRequest request, HttpServletResponse response) {

        String clueId = request.getParameter("clueId");

        ActivityService as= (ActivityService) ServiceFactory.getService(new ActivityServiceImpl());
        List<Activity> aList=as.getActivityListByClueId(clueId);
        PrintJson.printJsonObj(response,aList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Clue clue=cs.detail(id);

        request.setAttribute("clue",clue);
        request.getRequestDispatcher("/workbench/clue/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) {

        String id = UUIDUtil.getUUID();
        String fullname = request.getParameter("fullname");
        String appellation = request.getParameter("appellation");
        String owner = request.getParameter("owner");
        String company = request.getParameter("company");
        String job = request.getParameter("job");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String website = request.getParameter("website");
        String mphone = request.getParameter("mphone");
        String state = request.getParameter("state");
        String source = request.getParameter("source");
        String createBy = ((User)request.getSession().getAttribute("user")).getName();
        String createTime = DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");
        String address = request.getParameter("address");

        Clue clue=new Clue();
        clue.setId(id);
        clue.setFullname(fullname);
        clue.setAppellation(appellation);
        clue.setOwner(owner);
        clue.setCompany(company);
        clue.setJob(job);
        clue.setEmail(email);
        clue.setPhone(phone);
        clue.setWebsite(website);
        clue.setMphone(mphone);
        clue.setState(state);
        clue.setSource(source);
        clue.setCreateBy(createBy);
        clue.setCreateTime(createTime);
        clue.setDescription(description);
        clue.setContactSummary(contactSummary);
        clue.setNextContactTime(nextContactTime);
        clue.setAddress(address);

        ClueService cs= (ClueService) ServiceFactory.getService(new ClueServiceImpl());
        Boolean flag=cs.save(clue);
        PrintJson.printJsonFlag(response,flag);
    }

    private void getUserList(HttpServletRequest request, HttpServletResponse response) {

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();
        PrintJson.printJsonObj(response,uList);
    }
}
