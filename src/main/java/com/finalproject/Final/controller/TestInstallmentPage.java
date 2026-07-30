package com.finalproject.Final.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestInstallmentPage {
@GetMapping("/admin/enrollD")
public String testInstallmentPage() {
    return "admin/admin-enrollment-detail";}}

