package com.finalproject.Final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestInstallmentPage {
@GetMapping("/test-installment")
public String testInstallmentPage() {
    return "student/installment-payment";}}

