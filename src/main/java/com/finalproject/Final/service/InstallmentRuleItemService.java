package com.finalproject.Final.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.InstallmentRuleItemBean;
import com.finalproject.Final.repository.InstallmentRuleItemRepository;


@Service
public class InstallmentRuleItemService {


    @Autowired
    private InstallmentRuleItemRepository repository;



    public List<InstallmentRuleItemBean> getByRuleId(
            String ruleId
    ){

        return repository.findByRuleId(ruleId);

    }


}