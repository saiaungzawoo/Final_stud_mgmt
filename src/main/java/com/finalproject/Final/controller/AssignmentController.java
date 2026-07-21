package com.finalproject.Final.controller;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.model.AssignmentBean;
import com.finalproject.Final.model.AssignmentStatus;
import com.finalproject.Final.repository.AssignmentRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {

    @Autowired
    private AssignmentRepository assignmentRepo;

    private final String TEACHER_ID = "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";

    /**
     * ၁။ အဓိက Dashboard Page (List ပြသခြင်း၊ Course Filter လုပ်ခြင်းအားလုံး ဝင်မည့်နေရာ)
     * URL: /assignment/list
     */
    @GetMapping("/list")
    public String assignmentDashboard(
            @RequestParam(value = "courseID", required = false) String courseID,
            Model model) {

        if (!model.containsAttribute("assignment")) {
            AssignmentBean bean = new AssignmentBean();
            bean.setStatus(AssignmentStatus.Draft); // Default Status 
            model.addAttribute("assignment", bean);
        }

        if (courseID != null && !courseID.isEmpty()) {
            model.addAttribute("assignmentList", assignmentRepo.getAssignmentListByCourse(courseID));
        } else {
            model.addAttribute("errorMessage", "Please select a course to view assignments.");
        }

        // Dropdown Form တွေအတွက် Course List နဲ့ Status List ကို ထည့်ပေးခြင်း
        model.addAttribute("courseList", assignmentRepo.getTeacherCourses(TEACHER_ID));
        model.addAttribute("statusList", AssignmentStatus.values());

        return "teacher/assignment-dashboard"; //  Single-page HTML Name
    }

    /**
     * ၂။ Assignment အသစ်ကို သိမ်းဆည်းခြင်း (Create Form ကနေ လာမည့်နေရာ)
     * URL: /assignment/save
     */
    @PostMapping("/save")
    public String saveAssignment(
            @Valid @ModelAttribute("assignment") AssignmentBean bean,
            BindingResult result,
            RedirectAttributes redirectAttributes) {

        // validation input error 
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.assignment", result);
            redirectAttributes.addFlashAttribute("assignment", bean);
            return "redirect:/assignment/list?courseID=" + bean.getCourseID();
        }

        if (bean.getAssignmentID() == null || bean.getAssignmentID().isEmpty()) {
            bean.setAssignmentID(UUID.randomUUID().toString());
        }

        bean.setCreatedByID(TEACHER_ID); // Teacher ID 
        assignmentRepo.saveAssignment(bean); // DB 

        //  Course ရဲ့ list ဆီကို Redirect 
        return "redirect:/assignment/list?courseID=" + bean.getCourseID();
    }

    /**
     * ၃။ Assignment ကို တည်းဖြတ်ခြင်း (Update လုပ်ခြင်း)
     * URL: /assignment/update
     */
    @PostMapping("/update")
    public String updateAssignment(@ModelAttribute("assignment") AssignmentBean bean) {
        
        // Form ထဲမှာ teacher id မပျောက်သွားအောင် ယာယီ ID ပြန်ထည့်ပေးထားမယ်
        bean.setCreatedByID(TEACHER_ID);
        
        assignmentRepo.updateAssignment(bean); // DB ထဲမှာ သွားပြင်မယ်

        //Course list 
        return "redirect:/assignment/list?courseID=" + bean.getCourseID();
    }

    /**
     * ၄။ Assignment ကို ဖျက်သိမ်းခြင်း (Delete Modal ကနေ လာမည့်နေရာ)
     * URL: /assignment/delete
     */
	/*
	 * @PostMapping("/delete") public String deleteAssignment(
	 * 
	 * @RequestParam("assignmentID") String assignmentID,
	 * 
	 * @RequestParam(value = "courseID", required = false) String courseID) {
	 * 
	 * // မင်းရဲ့ Repository ထဲမှာ ရှိပြီးသား Delete Method ကို လှမ်းခေါ်ပါမယ် //
	 * (မှတ်ချက် - မင်းရဲ့ Repo ထဲက method name က deleteAssignment ဖြစ်ဖြစ်
	 * deleteById ဖြစ်ဖြစ် ပြောင်းပေးပါ)
	 * assignmentRepo.deleteAssignment(assignmentID);
	 * 
	 * // ဖျက်ပြီးရင် အဲ့ဒီ course ရဲ့ Dashboard list ဆီကိုပဲ Redirect ပြန်လုပ်မယ်
	 * if (courseID != null && !courseID.isEmpty()) { return
	 * "redirect:/assignment/list?courseID=" + courseID; } return
	 * "redirect:/assignment/list"; }
	 */

    /**
     */
    @GetMapping("/course-select")
    public String redirectOldSelect() {
        return "redirect:/assignment/list";
    }

    @GetMapping("/create")
    public String redirectOldCreate() {
        return "redirect:/assignment/list";
    }
}