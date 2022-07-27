package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.*;
import com.bjpowernode.crm.workbench.domain.*;
import com.bjpowernode.crm.workbench.service.ClueService;
import com.sun.xml.internal.ws.spi.db.DatabindingException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClueServiceImpl implements ClueService {

//    线索相关表
    private ClueDao clueDao= SqlSessionUtil.getSqlSession().getMapper(ClueDao.class);
    private ClueActivityRelationDao clueActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ClueActivityRelationDao.class);
    private ClueRemarkDao clueRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ClueRemarkDao.class);

//    客户相关表
    private CustomerDao customerDao=SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);
    private CustomerRemarkDao customerRemarkDao=SqlSessionUtil.getSqlSession().getMapper(CustomerRemarkDao.class);

//    联系人相关表
    private ContactsDao contactsDao=SqlSessionUtil.getSqlSession().getMapper(ContactsDao.class);
    private ContactsRemarkDao contactsRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ContactsRemarkDao.class);
    private ContactsActivityRelationDao contactsActivityRelationDao=SqlSessionUtil.getSqlSession().getMapper(ContactsActivityRelationDao.class);

//    交易相关表
    private TranDao tranDao=SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao=SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);


    @Override
    public Boolean save(Clue clue) {
        int count=clueDao.save(clue);
        return count==1;
    }

    @Override
    public Clue detail(String id) {
        Clue clue=clueDao.detail(id);
        return clue;
    }

    @Override
    public Boolean unbond(String id) {
        int count=clueActivityRelationDao.unbond(id);
        return count==1;
    }

    @Override
    public Boolean bund(String[] aids, String cid) {
        Boolean flag=true;
        for (String aid : aids) {
            ClueActivityRelation ca=new ClueActivityRelation();
            ca.setId(UUIDUtil.getUUID());
            ca.setActivityId(aid);
            ca.setClueId(cid);
            int count=clueActivityRelationDao.bund(ca);
            if(count!=1){
                flag=false;
            }
        }
        return flag;
    }

    @Override
    public Boolean convert(String clueId, Tran tran, String createBy) throws Exception {

        String createTime= DateTimeUtil.getSysTime();
        boolean flag=true;

//        （1）通过clueId获取线索对象
        Clue clue=clueDao.getById(clueId);

//        （2）通过线索对象提取客户信息，当该客户不存在时，新建客户（根据公司名精确匹配，判断该客户是否存在）
        String company = clue.getCompany();
        Customer customer=customerDao.getCustomerByName(company);
//        如果没有该客户，就创建
        if(customer==null){
            customer=new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setAddress(clue.getAddress());
            customer.setWebsite(clue.getWebsite());
            customer.setPhone(clue.getPhone());
            customer.setOwner(clue.getOwner());
            customer.setNextContactTime(clue.getNextContactTime());
            customer.setName(company);
            customer.setDescription(clue.getDescription());
            customer.setContactSummary(clue.getContactSummary());
            customer.setCreateBy(createBy);
            customer.setCreateTime(createTime);
//          添加客户
            int count=customerDao.save(customer);
            if(count!=1){
                flag=false;
            }
        }

//        （3）通过线索对象提取联系人信息，保存联系人
        Contacts contacts=new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(clue.getOwner());
        contacts.setSource(clue.getSource());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setFullname(clue.getFullname());
        contacts.setEmail(clue.getEmail());
        contacts.setDescription(clue.getDescription());
        contacts.setCustomerId(customer.getId());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setCreateBy(createBy);
        contacts.setCreateTime(createTime);
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAppellation(clue.getAppellation());
        contacts.setAddress(clue.getAddress());
//        添加联系人
        int count=contactsDao.save(contacts);
        if(count!=1){
            flag=false;
        }

//        （4）将线索备注转化为客户备注以及联系人备注
//        查询出与该线索关联的备注信息列表
        List<ClueRemark> clueRemarkList=clueRemarkDao.getListByClueId(clueId);
        for (ClueRemark clueRemark : clueRemarkList) {
//            取出每一条线索的备注信息
            String noteContent = clueRemark.getNoteContent();

//            创建客户备注对象，添加客户备注
            CustomerRemark customerRemark=new CustomerRemark();
            customerRemark.setId(UUIDUtil.getUUID());
            customerRemark.setNoteContent(noteContent);
            customerRemark.setCreateBy(createBy);
            customerRemark.setCreateTime(createTime);
            customerRemark.setEditFlag("0");
            customerRemark.setCustomerId(clueRemark.getId());

            int count2=customerRemarkDao.save(customerRemark);
            if(count2!=1){
                flag=false;
            }

//            创建联系人备注对象，添加联系人备注
            ContactsRemark contactsRemark=new ContactsRemark();
            contactsRemark.setId(UUIDUtil.getUUID());
            contactsRemark.setNoteContent(noteContent);
            contactsRemark.setCreateBy(createBy);
            contactsRemark.setCreateTime(createTime);
            contactsRemark.setEditFlag("0");
            contactsRemark.setContactsId(contacts.getId());

            int count3=contactsRemarkDao.save(contactsRemark);
            if(count3!=1){
                flag=false;
            }
        }

//        （5）线索和市场活动的关系 转化为 联系人和市场活动的关系
//        查询出与该条线索关联的市场活动，查询与市场活动的关联关系列表
        List<ClueActivityRelation> carList=clueActivityRelationDao.getListByClueId();
        for (ClueActivityRelation clueActivityRelation : carList) {

            String activityId = clueActivityRelation.getActivityId();

            ContactsActivityRelation contactsActivityRelation=new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtil.getUUID());
            contactsActivityRelation.setActivityId(activityId);
            contactsActivityRelation.setContactsId(contacts.getId());

            int count4=contactsActivityRelationDao.save(contactsActivityRelation);
            if(count4!=1){
                flag=false;
            }
        }


//        （6）如果有交易需求，创建一条交易
        if(tran!=null){

            tran.setOwner(clue.getOwner());
            tran.setCustomerId(customer.getId());
            tran.setSource(clue.getSource());
            tran.setContactsId(contacts.getId());
            tran.setDescription(clue.getDescription());
            tran.setContactSummary(clue.getContactSummary());
            tran.setNextContactTime(clue.getNextContactTime());

            int count5=tranDao.save(tran);
            if(count5!=1){
                flag=false;
            }

//            （7）如果创建了交易，则创建一条该交易下的交易历史
            TranHistory tranHistory=new TranHistory();
            tranHistory.setId(UUIDUtil.getUUID());
            tranHistory.setStage(tran.getStage());
            tranHistory.setMoney(tran.getMoney());
            tranHistory.setExpectedDate(tran.getExpectedDate());
            tranHistory.setCreateTime(createTime);
            tranHistory.setCreateBy(createBy);
            tranHistory.setTranId(tran.getId());

            int count6=tranHistoryDao.save(tranHistory);
            if(count6!=1){
                flag=false;
            }
        }

//        （8）删除线索备注
        for (ClueRemark clueRemark : clueRemarkList) {
            int count7=clueRemarkDao.delete(clueRemark);
            if(count7!=1){
                flag=false;
            }
        }

//        （9）删除线索和市场活动的关系
        for (ClueActivityRelation clueActivityRelation : carList) {
            int count8=clueActivityRelationDao.delete(clueActivityRelation);
            if(count8!=1){
                flag=false;
            }
        }

//        （10）删除线索
        int count9=clueDao.delete(clueId);
        if(count9!=1){
            flag=false;
        }

        if(!flag){
            throw new Exception("flag=false");
        }
        return flag;
    }
}
