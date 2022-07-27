package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran tran);

    Tran detail(String tid);

    int changeStage(Tran tran);

    int getTotal();

    List<Map<String, String>> getCharts();
}
