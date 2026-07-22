package com.finalproject.Final.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.ExamBean;
import com.finalproject.Final.model.ExamResultBean;
import com.finalproject.Final.repository.StudentExamRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/student/exam")
public class StudentExamController {

    @Autowired
    private StudentExamRepository studentExamRepo;

    // 1. Enrolled ဖြစ်ထားသော Exam List အားလုံး ကြည့်ရန်
    @GetMapping("/list")
    public String examList(HttpSession session, Model model) {
        String userID = (String) session.getAttribute("userID");

        if (userID == null) {
            return "redirect:/login";
        }

        List<ExamBean> exams = studentExamRepo.getStudentExams(userID);
        model.addAttribute("exams", exams);

        return "student/exam-list";
    }

    // 2. Exam တစ်ခုချင်းစီ၏ Offline Detail အချက်အလက်များ ကြည့်ရန်
    @GetMapping("/detail/{id}")
    public String examDetail(@PathVariable("id") String examID,
                             HttpSession session,
                             Model model) {
        String userID = (String) session.getAttribute("userID");

        if (userID == null) {
            return "redirect:/login";
        }

        // Student သည် ထို Exam ၏ Course ထဲတွင် Enrollment ရှိ/မရှိ Validation စစ်ခြင်း
        if (!studentExamRepo.isEnrolledInCourse(examID, userID)) {
            return "redirect:/student/exam/list";
        }

        ExamBean exam = studentExamRepo.getExamByID(examID);
        model.addAttribute("exam", exam);

        return "student/exam-detail";
    }

    // 3. Student ရဲ့ Offline Exam Result များ အားလုံးကို ကြည့်ရန်
    @GetMapping("/results")
    public String viewResults(HttpSession session, Model model) {
        String userID = (String) session.getAttribute("userID");

        if (userID == null) {
            return "redirect:/login";
        }

        List<ExamResultBean> results = studentExamRepo.getStudentResults(userID);
        model.addAttribute("results", results);

        return "student/exam-result";
    }
}