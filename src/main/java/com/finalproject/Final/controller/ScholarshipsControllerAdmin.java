package com.finalproject.Final.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.finalproject.Final.model.ScholarshipApplicationBean;
import com.finalproject.Final.model.ScholarshipBean;
import com.finalproject.Final.repository.ScholarshipApplicationRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class ScholarshipsControllerAdmin {

    @Autowired
    private ScholarshipApplicationRepository sArepo;

    // Create Scholarship Page
    @GetMapping("/scholarship-create")
    public String create(Model model) {
        model.addAttribute("scholarship", new ScholarshipBean());
        model.addAttribute("courses", sArepo.getAllCourseNa()); 
        return "admin/create-scholarship";
    }

    // Save New Scholarship
    @PostMapping("/save")
    public String save(
            @Valid @ModelAttribute("scholarship") ScholarshipBean scholarship,
            BindingResult result,
            Model model,
            HttpSession session) {

        if (result.hasErrors()) {
            model.addAttribute("courses", sArepo.getAllCourseNa()); 
            return "admin/create-scholarship";
        }

        scholarship.setScholarshipID(java.util.UUID.randomUUID().toString());
        scholarship.setCreatedAt(LocalDateTime.now());
        scholarship.setUpdatedAt(LocalDateTime.now());

        sArepo.insert(scholarship);

        return "redirect:/admin/list";
    }

    // Scholarship Management List
    @GetMapping("/list")
    public String list(Model model) {
        model.addAttribute("scholarships", sArepo.getAll());
        return "admin/adminscholarship-list";
    }

    // Edit Scholarship Page
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable String id, Model model) {
        ScholarshipBean scholarship = sArepo.findByScholarshipId(id);
        model.addAttribute("scholarship", scholarship);
        return "admin/update-scholarship";
    }

    // Update Scholarship Action
    @PostMapping("/update")
    public String update(
            @ModelAttribute("scholarship") ScholarshipBean scholarship,
            @RequestParam(value = "oldDeadline", required = false) String oldDeadline) {

        if (scholarship.getApplicationDeadline() == null && oldDeadline != null && !oldDeadline.isEmpty()) {
            scholarship.setApplicationDeadline(LocalDate.parse(oldDeadline));
        }
        
        scholarship.setUpdatedAt(LocalDateTime.now());
        sArepo.update(scholarship);

        return "redirect:/admin/list";
    }

    // Show Applicants By Scholarship
    @GetMapping("/scholarship/{id}/applications")
    public String scholarshipApplications(@PathVariable String id, Model model) {
        model.addAttribute("applications", sArepo.getApplicationsByScholarship(id));
        return "admin/scholarshipapplication-list";
    }

    // Student Application Detail
    @GetMapping("/application/detail/{id}")
    public String applicationDetail(@PathVariable String id, Model model) {
        ScholarshipApplicationBean application = sArepo.getApplicationDetail(id);
        model.addAttribute("appdetail", application);
        return "admin/scholarshipapplication-detail";
    }

    // Approve / Reject Application with Automated Quota Check
    @PostMapping("/application/status")
    public String updateStatus(
            @RequestParam("applicationID") String applicationID,
            @RequestParam("status") String status,
            @RequestParam(value = "reviewNotes", required = false) String reviewNotes,
            @RequestParam(value = "scholarshipID", required = false) String scholarshipID) {

        // Admin ID placeholder (Replace with session-based admin ID when auth is ready)
        String adminID = "3c312b98-7a84-11f1-bfcb-b4b686e7f920";

        // Update selected application status & notes
        sArepo.updateApplicationStatus(applicationID, status, adminID, reviewNotes);

        // Automation logic: Check quota when application is approved
        if ("APPROVED".equalsIgnoreCase(status) && scholarshipID != null && !scholarshipID.isEmpty()) {

            ScholarshipBean scholarship = sArepo.findByScholarshipId(scholarshipID);
            int approvedCount = sArepo.getApprovedCountByScholarship(scholarshipID);

            // If maximum recipient limit is reached or exceeded
            if (approvedCount >= scholarship.getMaxRecipients()) {

                // 1. Mark scholarship inactive
                sArepo.updateScholarshipStatus(scholarshipID, "INACTIVE");

                // 2. Automatically reject all remaining pending applications
                sArepo.rejectPendingApplications(
                        scholarshipID,
                        adminID,
                        "Automatically rejected because scholarship quota has been filled."
                );
            }
        }

        // Redirect back to applicant list if scholarshipID is present, else list page
        if (scholarshipID != null && !scholarshipID.isEmpty()) {
            return "redirect:/admin/scholarship/" + scholarshipID + "/applications";
        } else {
            return "redirect:/admin/list";
        }
    }
}