package com.finalproject.Final.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.finalproject.Final.model.FeedbackBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.FeedbackRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/student/feedback")
public class FeedbackController {


    @Autowired
    private FeedbackRepository feedRepo;

    @GetMapping("/create")
    public String feedbackForm(Model model, HttpSession session) {

        UserBean loginUser = (UserBean) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/login";
        }

        String userID = loginUser.getUserID();

        var courses = feedRepo.getEnrolledCourses(userID);

        model.addAttribute("courses", courses);
        model.addAttribute("noCourse", courses.isEmpty());

        FeedbackBean feedback = new FeedbackBean();
        feedback.setUserID(userID);
        feedback.setIsAnonymous(0);

        model.addAttribute("feedback", feedback);

        return "student/feedback-form";
    }
//    // Student feedback form
//    @GetMapping("/create")
//    public String feedbackForm(
//            Model model,
//            HttpSession session) {
//
//
//        UserBean loginUser =
//                (UserBean) session.getAttribute("loginUser");
//
//
//        if(loginUser == null) {
//            return "redirect:/login";
//        }
//
//
//        String userID = loginUser.getUserID();
//
//
//        // enrolled course list
//        model.addAttribute(
//                "courses",
//                feedRepo.getEnrolledCourses(userID)
//        );
//
//
//        FeedbackBean feedback = new FeedbackBean();
//
//        feedback.setUserID(userID);
//        feedback.setIsAnonymous(0);
//
//
//        model.addAttribute(
//                "feedback",
//                feedback
//        );
//
//
//        return "student/feedback-form";
//    }
//


    // Save feedback
    @PostMapping("/save")
    public String saveFeedback(
            @Valid @ModelAttribute("feedback") FeedbackBean feedback,
            BindingResult result,
            HttpSession session,
            Model model) {


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");


        if(loginUser == null) {
            return "redirect:/login";
        }



        if(result.hasErrors()) {
model.addAttribute(
                    "courses",
                    feedRepo.getEnrolledCourses(
                            loginUser.getUserID()
                    )
            );

            return "student/feedback-form";
        }



        feedback.setUserID(
                loginUser.getUserID()
        );


        feedRepo.saveFeedback(feedback);
 return "redirect:/student/feedback/create";
    }




    // Admin feedback list
    @GetMapping("/list")
    public String feedbackList(Model model){


        model.addAttribute(
                "feedbackList",
                feedRepo.getAllFeedback()
        );


        return "admin/adminfeedback-list";
    }



    // Delete feedback
    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") String id){


        feedRepo.deleteFeedback(id);


        return "redirect:/student/feedback/list";
    }

}