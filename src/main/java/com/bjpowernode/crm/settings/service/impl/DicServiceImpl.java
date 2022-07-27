package com.bjpowernode.crm.settings.service.impl;

import com.bjpowernode.crm.settings.dao.DicTypeDao;
import com.bjpowernode.crm.settings.dao.DicValueDao;
import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;
import com.bjpowernode.crm.settings.service.DicService;
import com.bjpowernode.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao= SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getAll() {
        Map<String,List<DicValue>> map=new HashMap<>();
//        将字典类型数组取出
        List<DicType> dtList=dicTypeDao.getTypeList();

        for (DicType dicType : dtList) {
//            取出每一种类型的类型编码
            String code = dicType.getCode();
//            根据编码获取对应的字符值列表
            List<DicValue> dvList=dicValueDao.getListByCode(code);
//            封装成map
            map.put(code+"List",dvList);
        }
        return map;
    }
}
