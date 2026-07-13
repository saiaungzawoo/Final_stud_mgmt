package com.finalproject.Final.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.InstallmentRuleBean;
import com.finalproject.Final.repository.InstallmentRuleRepository;


@Service
public class InstallmentRuleService {


    @Autowired
    private InstallmentRuleRepository repository;



    public InstallmentRuleBean getByCourseId(
            String courseId
    ){

        return repository.findByCourseId(courseId);

    }


}