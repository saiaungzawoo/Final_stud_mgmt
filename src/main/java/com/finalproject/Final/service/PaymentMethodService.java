package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.PaymentMethodBean;
import com.finalproject.Final.repository.PaymentMethodRepository;

@Service
public class PaymentMethodService {

    @Autowired
    private PaymentMethodRepository repo;

    public List<PaymentMethodBean> getAllActive() {
        return repo.getAllActive();
    }

}