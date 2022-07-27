package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

public interface TranService {
    Boolean save(Tran tran, String customerName) throws Exception;

    Tran detail(String tid);

    List<TranHistory> getHistoryListByTranId(String TranId);

    Boolean changeStage(Tran tran) throws Exception;

    Map<String, Object> getCharts();
}
