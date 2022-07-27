package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.settings.dao.UserDao;
import com.bjpowernode.crm.settings.domain.User;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.dao.ActivityDao;
import com.bjpowernode.crm.workbench.dao.ActivityRemarkDao;
import com.bjpowernode.crm.workbench.dao.ClueActivityRelationDao;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;
import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;
import com.bjpowernode.crm.workbench.service.ActivityService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityServiceImpl implements ActivityService {

    private ActivityDao activityDao= SqlSessionUtil.getSqlSession().getMapper(ActivityDao.class);
    private ActivityRemarkDao activityRemarkDao=SqlSessionUtil.getSqlSession().getMapper(ActivityRemarkDao.class);
    private UserDao userDao=SqlSessionUtil.getSqlSession().getMapper(UserDao.class);


    @Override
    public Boolean save(Activity activity) {
        int count=activityDao.save(activity);
//        数量为1时返回ture，否则false
        return count == 1;
    }

    @Override
    public PaginationVO<Activity> pageList(Map<String, Object> map) {
//        取得total
        int total=activityDao.getTotalByCondition(map);
//        取得dataList
        List<Activity> dataList=activityDao.getActivityListByCondition(map);
//        封装成VO
        PaginationVO<Activity> vo=new PaginationVO<>();
        vo.setTotal(total);
        vo.setDataList(dataList);
        return vo;
    }

    @Override
    public Boolean delete(String[] ids) {
        Boolean flag=true;
//        查询出需要删除的备注的数量
        int count1=activityRemarkDao.getCountByAids(ids);
//        实际删除的备注的数量
        int count2=activityRemarkDao.deleteByAids(ids);

        if(count1!=count2){
            flag=false;
        }

//        删除市场活动
        int count3=activityDao.delete(ids);
        if(count3!=ids.length){
            flag=false;
        }

        return flag;
    }

    @Override
    public Map<String, Object> getUserListAndActivity(String id) {
//        取uList
        List<User> userList = userDao.getUserList();
//        取a
        Activity a=activityDao.getById(id);
//        封装成map
        Map<String,Object> map=new HashMap<>();
        map.put("uList",userList);
        map.put("a",a);
        return map;
    }

    @Override
    public Boolean update(Activity activity) {
        int count=activityDao.update(activity);
//        数量为1时返回ture，否则false
        return count == 1;
    }

    @Override
    public Activity detail(String id) {
        Activity activity = activityDao.detail(id);
        return activity;
    }

    @Override
    public List<ActivityRemark>  getRemarkListByAid(String activityId) {
        List<ActivityRemark> arList=activityRemarkDao.getRemarkListByAid(activityId);
        return arList;
    }

    @Override
    public Boolean deleteRemark(String id) {
        int count=activityRemarkDao.deleteRemark(id);
        return count==1;
    }

    @Override
    public Boolean saveRemark(ActivityRemark ar) {
//        执行添加操作，得到success
        int count=activityRemarkDao.saveRemark(ar);
        return count==1;
    }

    @Override
    public Boolean updateRemark(ActivityRemark ar) {
        int count=activityRemarkDao.updateRemark(ar);
        return count==1;
    }

    @Override
    public List<Activity> getActivityListByClueId(String clueId) {
        List<Activity> aList=activityDao.getActivityListByClueId(clueId);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByNameAndNotByClueId(String aName, String clueId) {
        Map<String,String> map=new HashMap<>();
        map.put("aName",aName);
        map.put("clueId",clueId);
        List<Activity> aList=activityDao.getActivityListByNameAndNotByClueId(map);
        return aList;
    }

    @Override
    public List<Activity> getActivityListByName(String aName) {
        List<Activity> aList=activityDao.getActivityListByName(aName);
        return aList;
    }
}
