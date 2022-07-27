package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    int unbond(String id);

    int bund(ClueActivityRelation ca);

    List<ClueActivityRelation> getListByClueId();

    int delete(ClueActivityRelation clueActivityRelation);
}
