package com.finalproject.Final.controller;

import java.net.BindException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.ExamBean;
import com.finalproject.Final.model.ExamResultBean;
import com.finalproject.Final.model.ExamResultFormBean;
import com.finalproject.Final.repository.ExamRepository;
import com.finalproject.Final.repository.ExamResultRepository;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/exam/result")
public class ExamResultController {

@Autowired
private ExamRepository examRepo;
    private final ExamResultRepository examResultRepo;


    public ExamResultController(ExamResultRepository examResultRepo) {
        this.examResultRepo = examResultRepo;
    }


    @GetMapping("/{examID}")
    public String showResultForm(
            @PathVariable String examID,
            Model model) {

    	 // Check existing result
        List<ExamResultBean> existing =
                examResultRepo.getResultListByExam(examID);


        if(existing != null && !existing.isEmpty()) {

            return "redirect:/exam/result/view/" + examID;

        }

        ExamResultFormBean form = new ExamResultFormBean();


        form.setExamID(examID);



        form.setResults(
            examResultRepo.getStudentListByExam(examID)
        );



        model.addAttribute("form", form);



        return "teacher/exam-result-form";
    }
    @PostMapping("/save")
    public String saveResult(
            @Valid @ModelAttribute("form") ExamResultFormBean form,
            BindingResult result,
            Model model) {


        if(result.hasErrors()) {

            System.out.println("ERROR COUNT : "
                    + result.getErrorCount());

            System.out.println(
                    result.getAllErrors()
            );


            model.addAttribute(
                    "form",
                    form
            );


            return "teacher/exam-result-form";
        }



        ExamBean exam =
                examRepo.getExamByID(form.getExamID());



        for(ExamResultBean bean : form.getResults()) {


            // Score empty check
            if(bean.getScore() == null) {


                model.addAttribute(
                        "error",
                        "Please enter score for all students"
                );


                model.addAttribute(
                        "form",
                        form
                );


                return "teacher/exam-result-form";
            }



            // Max score check
            if(bean.getScore()
                    .compareTo(exam.getMaxScore()) > 0) {


                model.addAttribute(
                        "error",
                        "Score cannot exceed maximum score : "
                        + exam.getMaxScore()
                );


                model.addAttribute(
                        "form",
                        form
                );


                return "teacher/exam-result-form";
            }



            bean.setExamID(
                    form.getExamID()
            );


        }



        examResultRepo.saveResults(
                form.getResults()
        );


        return "redirect:/exam/result/view/"
                + form.getExamID();
    }
    @GetMapping("/select")
    public String selectExam(Model model) {


        String teacherID =
            "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";


        List<ExamBean> exams =
                examRepo.getExamListByTeacher(teacherID);


        model.addAttribute("exams", exams);


        return "teacher/exam-result-select";

    }
    @GetMapping("/course")
    public String selectCourse(Model model) {


        String teacherID =
            "00ee5b4b-7a6f-11f1-8f4f-183d2d227d02";


        List<CourseBean> courses =
                examRepo.getTeacherCourses(teacherID);


        model.addAttribute("courses", courses);


        return "teacher/exam-result-course-select";
    }
    @GetMapping("/exam/{courseID}")
    public String selectExam(
            @PathVariable String courseID,
            Model model) {


        List<ExamBean> exams =
                examRepo.getExamListByCourse(courseID);


        model.addAttribute("exams", exams);

        model.addAttribute("courseID", courseID);


        return "teacher/exam-result-exam-select";
    }
    @GetMapping("/view/{examID}")
    public String viewResult(
            @PathVariable String examID,
            Model model) {


        List<ExamResultBean> results =
                examResultRepo.getResultListByExam(examID);


        model.addAttribute("results", results);

        model.addAttribute("examID", examID);


        return "teacher/exam-result-list";
    }
    @PostMapping("/update")
    public String updateResult(
            @Valid @ModelAttribute("result") ExamResultBean bean,
            BindingResult bindingResult,
            Model model) {
    	 System.out.println("SCORE = " + bean.getScore());

    	    System.out.println("ERROR = "
    	            + bindingResult.getErrorCount());


    	    if(bindingResult.hasErrors()) {


    	        ExamResultBean oldData =
    	                examResultRepo.getResultByID(
    	                        bean.getExamResultID()
    	                );


    	        oldData.setScore(bean.getScore());
    	        oldData.setRemarks(bean.getRemarks());


    	        model.addAttribute(
    	                "result",
    	                oldData
    	        );


    	        model.addAttribute(
    	                "org.springframework.validation.BindingResult.result",
    	                bindingResult
    	        );


    	        return "teacher/exam-result-edit";
    	    }


        ExamBean exam =
                examRepo.getExamByID(bean.getExamID());



        if(bean.getScore() == null) {


            ExamResultBean oldData =
                    examResultRepo.getResultByID(
                            bean.getExamResultID()
                    );


            oldData.setScore(bean.getScore());
            oldData.setRemarks(bean.getRemarks());
            
            
            model.addAttribute(
                    "error",
                    "Score is required"
            );

            model.addAttribute(
                    "result",
                    oldData
            );


          


            return "teacher/exam-result-edit";
        }


        examResultRepo.updateResult(bean);


        return "redirect:/exam/result/view/"
                + bean.getExamID();
    }  
    
    @GetMapping("/edit/{id}")
    public String editResult(
            @PathVariable String id,
            Model model) {


        ExamResultBean bean =
                examResultRepo.getResultByID(id);


        model.addAttribute("result", bean);


        return "teacher/exam-result-edit";
    }
    @GetMapping("/student/{userID}")
    public String viewStudentExamResult(
            @PathVariable String userID,
            Model model) {


        List<ExamResultBean> results =
                examResultRepo.getResultByStudent(userID);


        model.addAttribute(
                "results",
                results
        );


        return "teacher/student-exam-result";
    }
    
    }