package com.bjpowernode.crm.settings.dao;

import com.bjpowernode.crm.settings.domain.DicType;
import com.bjpowernode.crm.settings.domain.DicValue;

import java.util.List;

public interface DicTypeDao {
    List<DicType> getTypeList();
}
