package com.finalproject.Final.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.AdminEnrollmentDetailBean;
import com.finalproject.Final.repository.AdminEnrollmentDetailRepository;



@Service
public class AdminEnrollmentDetailService {


@Autowired
private AdminEnrollmentDetailRepository repo;



public AdminEnrollmentDetailBean getDetail(
        String enrollmentId
){

    return repo.findById(enrollmentId);

}


}