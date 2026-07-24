package com.finalproject.Final.controller;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ExamBean;
import com.finalproject.Final.model.ExamResultBean;
import com.finalproject.Final.model.ExamResultFormBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.ExamRepository;
import com.finalproject.Final.repository.ExamResultRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/teacher/exam-results")
public class ExamResultController {

    @Autowired
    private ExamRepository examRepo;

    @Autowired
    private ExamResultRepository examResultRepo;

 
    @GetMapping
    public String showDashboard(
            @RequestParam(value = "courseId", required = false) String courseId,
            @RequestParam(value = "examId", required = false) String examId,
            Model model,
            HttpSession session) {

        // Session Checking
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) {
            return "redirect:/login";
        }

        String teacherID = loginUser.getUserID();
//course list
        List<CourseBean> courses = examRepo.getTeacherCourses(teacherID);
        model.addAttribute("courses", courses);
//exam list
        if (courseId != null && !courseId.trim().isEmpty()) {
        	List<ExamBean> exams = examResultRepo.getCompletedExamListByCourse(courseId);
            model.addAttribute("exams", exams);
            model.addAttribute("selectedCourseId", courseId);
        }
//exam result
        if (examId != null && !examId.trim().isEmpty()) {
            List<ExamResultBean> results = examResultRepo.getResultListByExam(examId);
            model.addAttribute("results", results);
            model.addAttribute("selectedExamId", examId);

            // Directly URL ကနေ examId ပဲ ရောက်လာခဲ့ရင် Course နဲ့ Exam Dropdown ကို auto select လုပ်ပေးရန်
            if (courseId == null || courseId.trim().isEmpty()) {
                ExamBean exam = examRepo.getExamByID(examId);
                if (exam != null) {
                    model.addAttribute("selectedCourseId", exam.getCourseID());
                    model.addAttribute("exams", examRepo.getExamListByCourse(exam.getCourseID()));
                }
            }
            ExamResultFormBean form = new ExamResultFormBean();
            form.setExamID(examId);
            
            // Repository ထဲက NOT EXISTS Query ကိုသုံးပြီး Record မရှိသေးတဲ့ Student များ ဆွဲထုတ်ခြင်း
            List<ExamResultBean> pendingStudents = examResultRepo.getStudentListByExam(examId);
            form.setResults(pendingStudents);
            
            // HTML (Thymeleaf) သို့ 'form' name ဖြင့် Binding လုပ်ပေးရန်
            model.addAttribute("form", form);
        
        }

        return "teacher/exam-result-dashboard";
    }

    /**
     * 2. Grade / Create Exam Result (New Student Grade)
     */
    @PostMapping("/save")
    public String saveResults(@ModelAttribute("form") ExamResultFormBean form, HttpSession session) {
        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        
        for (ExamResultBean item : form.getResults()) {
            // Score ထည့်ထားသည့် ကျောင်းသားများကိုပဲ DB ထဲ သိမ်းမည်
            if (item.getScore() != null) {
                item.setExamID(form.getExamID());
                examResultRepo.saveResult(item, loginUser.getUserID());
            }
        }
        
        return "redirect:/teacher/exam-results?examId=" + form.getExamID();
    }
    /**
     * 3. Update Existing Exam Result (Modal Form Submit)
     */
    @PostMapping("/update")
    public String updateResult(@Valid @ModelAttribute("examResult") ExamResultBean examResult,
                               BindingResult bindingResult,
                               HttpSession session) {

        UserBean loginUser = (UserBean) session.getAttribute("loginUser");
        if (loginUser == null) return "redirect:/login";

        if (!bindingResult.hasErrors()) {
            examResultRepo.updateResult(examResult, loginUser.getUserID());
        }

        // 🟢 examId ပါဝင်သော Fresh URL သို့ Redirect လုပ်ပေးပါ
        return "redirect:/teacher/exam-results?examId=" + examResult.getExamID();
    }
    
    
}