package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.Tran;

public interface ClueService {
    Boolean save(Clue clue);

    Clue detail(String id);

    Boolean unbond(String id);

    Boolean bund(String[] aids, String cid);


    Boolean convert(String clueId, Tran tran, String createBy) throws Exception;
}
