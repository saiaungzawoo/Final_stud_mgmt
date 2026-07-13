package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.PaymentTypeBean;
import com.finalproject.Final.repository.PaymentTypeRepository;

@Service
public class PaymentTypeService {

    @Autowired
    private PaymentTypeRepository repo;

    public List<PaymentTypeBean> getAll() {
        return repo.getAll();
    }

    public PaymentTypeBean getById(String paymentTypeId) {

        return repo.findById(paymentTypeId);

    }
}