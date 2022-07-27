package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.service.TranService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao= SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao= SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);

    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public Boolean save(Tran tran, String customerName) throws Exception {
        Boolean flag=true;
//        根据name查询customer对象，得到id，不存在则创建一个
        Customer customer = customerDao.getCustomerByName(customerName);
        if(customer==null){
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setName(customerName);
            customer.setCreateBy(tran.getCreateBy());
            customer.setCreateTime(tran.getCreateTime());
            customer.setContactSummary(tran.getContactSummary());
            customer.setNextContactTime(tran.getNextContactTime());
            customer.setOwner(tran.getOwner());
            int count=customerDao.save(customer);
            if(count!=1){
                flag=false;
            }
        }
        tran.setCustomerId(customer.getId());

//        添加一条交易
        int count2=tranDao.save(tran);
        if(count2!=1){
            flag=false;
        }
//        添加一条交易历史
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setTranId(tran.getId());
        int count3=tranHistoryDao.save(tranHistory);
        if(count3!=1){
            flag=false;
        }

        if(!flag){
            throw new Exception("flag=false");
        }
        return flag;
    }

    @Override
    public Tran detail(String tid) {
        Tran tran=tranDao.detail(tid);
        return tran;
    }

    @Override
    public List<TranHistory> getHistoryListByTranId(String tranId) {
        List<TranHistory> thList=tranHistoryDao.getHistoryListByTranId(tranId);
        return thList;
    }

    @Override
    public Boolean changeStage(Tran tran) throws Exception {
//        修改交易
        Boolean flag=true;
        int count=tranDao.changeStage(tran);
        if(count!=1){
            flag=false;
        }

//        生成交易历史
        TranHistory tranHistory=new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setCreateTime(tran.getEditTime());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setTranId(tran.getId());

        int count2 = tranHistoryDao.save(tranHistory);
        if(count!=1){
            flag=false;
        }

        if(!flag){
            throw new Exception("flag=false");
        }
        return flag;
    }

    @Override
    public Map<String, Object> getCharts() {
//        取得total
        int total=tranDao.getTotal();
//        取得dataList
        List<Map<String,String>> dataList=tranDao.getCharts();
//        封装成map
        Map<String,Object> map=new HashMap<>();
        map.put("total",total);
        map.put("dataList",dataList);
        return map;
    }
}
