package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.settings.service.impl.UserServiceImpl;
import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.PrintJson;
import com.bjpowernode.crm.utils.ServiceFactory;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.CustomerService;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.service.impl.CustomerServiceImpl;
import com.bjpowernode.crm.workbench.service.impl.TranServiceImpl;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet({"/workbench/transaction/add.do","/workbench/transaction/getCustomerName.do",
            "/workbench/transaction/save.do","/workbench/transaction/detail.do",
            "/workbench/transaction/getHistoryListByTranId.do","/workbench/transaction/changeStage.do",
            "/workbench/transaction/getCharts.do"})
public class TranController extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if ("/workbench/transaction/add.do".equals(path)) {
            add(request,response);
        }else if("/workbench/transaction/getCustomerName.do".equals(path)) {
            getCustomerName(request,response);
        }else if("/workbench/transaction/save.do".equals(path)) {
            save(request,response);
        }else if("/workbench/transaction/detail.do".equals(path)) {
            detail(request,response);
        }else if("/workbench/transaction/getHistoryListByTranId.do".equals(path)) {
            getHistoryListByTranId(request,response);
        }else if("/workbench/transaction/changeStage.do".equals(path)) {
            changeStage(request,response);
        }else if("/workbench/transaction/getCharts.do".equals(path)) {
            getCharts(request,response);
        }else if("/workbench/transaction/xxx.do".equals(path)) {
//            xxx(request,response);
        }else if("/workbench/transaction/xxx.do".equals(path)) {
//            xxx(request,response);
        }else if("/workbench/transaction/xxx.do".equals(path)) {
//            xxx(request,response);
        }else if("/workbench/transaction/xxx.do".equals(path)) {
//            xxx(request,response);
        }else if("/workbench/transaction/xxx.do".equals(path)) {
//            xxx(request,response);
        }
    }

    private void getCharts(HttpServletRequest request, HttpServletResponse response) {

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Map<String,Object> map=ts.getCharts();
        PrintJson.printJsonObj(response,map);
    }

    private void changeStage(HttpServletRequest request, HttpServletResponse response) {

        String tranId = request.getParameter("tranId");
        String stage = request.getParameter("stage");
        String money = request.getParameter("money");
        String expectedDate = request.getParameter("expectedDate");
        String editTime = DateTimeUtil.getSysTime();
        String editBy = ((User) request.getSession().getAttribute("user")).getName();



        Tran tran=new Tran();
        tran.setId(tranId);
        tran.setStage(stage);
        tran.setEditBy(editBy);
        tran.setEditTime(editTime);
        tran.setMoney(money);
        tran.setExpectedDate(expectedDate);

        Map<String,String> pMap= (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag= null;
        try {
            flag = ts.changeStage(tran);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String,Object> map=new HashMap<>();
        map.put("success",flag);
        map.put("tran",tran);
        PrintJson.printJsonObj(response,map);

    }

    private void getHistoryListByTranId(HttpServletRequest request, HttpServletResponse response) {

        String tranId = request.getParameter("tranId");

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        List<TranHistory> thList=ts.getHistoryListByTranId(tranId);

        Map<String,String> pMap= (Map<String, String>) request.getServletContext().getAttribute("pMap");
        for (TranHistory tranHistory : thList) {
            String possibility = pMap.get(tranHistory.getStage());
            tranHistory.setPossibility(possibility);
        }

        PrintJson.printJsonObj(response,thList);
    }

    private void detail(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String tid = request.getParameter("tid");

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Tran tran=ts.detail(tid);

        Map<String,String> pMap= (Map<String, String>) request.getServletContext().getAttribute("pMap");
        String possibility = pMap.get(tran.getStage());
        tran.setPossibility(possibility);

        request.setAttribute("tran",tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String id= UUIDUtil.getUUID();
        String owner = request.getParameter("owner");
        String money = request.getParameter("money");
        String name = request.getParameter("name");
        String expectedDate = request.getParameter("expectedDate");
        String customerName = request.getParameter("customerName");
        String stage = request.getParameter("stage");
        String type = request.getParameter("type");
        String source = request.getParameter("source");
        String activityId = request.getParameter("activityId");
        String contactsId = request.getParameter("contactsId");
        String createBy=((User)request.getSession().getAttribute("user")).getName();
        String createTime= DateTimeUtil.getSysTime();
        String description = request.getParameter("description");
        String contactSummary = request.getParameter("contactSummary");
        String nextContactTime = request.getParameter("nextContactTime");

        Tran tran=new Tran();
        tran.setId(id);
        tran.setOwner(owner);
        tran.setMoney(money);
        tran.setName(name);
        tran.setExpectedDate(expectedDate);
        tran.setStage(stage);
        tran.setType(type);
        tran.setSource(source);
        tran.setActivityId(activityId);
        tran.setContactsId(contactsId);
        tran.setCreateBy(createBy);
        tran.setCreateTime(createTime);
        tran.setDescription(description);
        tran.setContactSummary(contactSummary);
        tran.setNextContactTime(nextContactTime);

        TranService ts= (TranService) ServiceFactory.getService(new TranServiceImpl());
        Boolean flag= null;
        try {
            flag = ts.save(tran,customerName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flag){
            response.sendRedirect(request.getContextPath()+"/workbench/transaction/index.jsp");
        }
    }

    private void getCustomerName(HttpServletRequest request, HttpServletResponse response) {

        String name = request.getParameter("name");

        CustomerService cs= (CustomerService) ServiceFactory.getService(new CustomerServiceImpl());
        List<String> cList=cs.getCustomerName(name);
        PrintJson.printJsonObj(response,cList);
    }

    private void add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        UserService us= (UserService) ServiceFactory.getService(new UserServiceImpl());
        List<User> uList = us.getUserList();

        request.setAttribute("uList",uList);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }
}
