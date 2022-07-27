package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.vo.PaginationVO;
import com.bjpowernode.crm.workbench.domain.Activity;
import com.bjpowernode.crm.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    Boolean save(Activity activity);

    PaginationVO<Activity> pageList(Map<String, Object> map);

    Boolean delete(String[] ids);

    Map<String, Object> getUserListAndActivity(String id);

    Boolean update(Activity activity);

    Activity detail(String id);

    List<ActivityRemark>  getRemarkListByAid(String activityId);

    Boolean deleteRemark(String id);

    Boolean saveRemark(ActivityRemark ar);

    Boolean updateRemark(ActivityRemark ar);

    List<Activity> getActivityListByClueId(String clueId);

    List<Activity> getActivityListByNameAndNotByClueId(String aName, String clueId);

    List<Activity> getActivityListByName(String aName);
}
