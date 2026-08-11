package com.finalproject.Final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestInstallmentPage {
@GetMapping("/payment/success1/{paymentId}")
public String testInstallmentPage() {
    return "student/enroll-success";}}

