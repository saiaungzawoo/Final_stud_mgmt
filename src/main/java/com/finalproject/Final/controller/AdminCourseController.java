package com.finalproject.Final.controller;


import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.dto.CourseCreateRequest;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.InstallmentRuleBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;
import com.finalproject.Final.service.CourseCategoryService;
import com.finalproject.Final.service.CourseCreationService;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.EnrollmentService;
import com.finalproject.Final.service.InstallmentRuleItemService;
import com.finalproject.Final.service.InstallmentRuleService;
import com.finalproject.Final.service.SubCategoryService;
import com.finalproject.Final.service.TeacherService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {


    @Autowired
    private CourseCategoryService courseCategoryService;
    
    @Autowired
    private SubCategoryService subCategoryService;
    
    @Autowired
    private TeacherService teacherService;
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private EnrollmentService enrollmentService;
    
//    @Autowired
//    private InstallmentRuleService installmentRuleService;
//
//
//    @Autowired
//    private InstallmentRuleItemService installmentRuleItemService;
    
    
    @Autowired
    private CourseCreationService courseCreationService;



    @GetMapping("/create")
    public String createCoursePage(Model model) {


//        model.addAttribute(
//                "course",
//                new CourseBean()
//        );
    	
//    	CourseCreateRequest request = new CourseCreateRequest();

//    	request.getInstallmentItems().add(new InstallmentRuleItemBean());
//    	request.getInstallmentItems().add(new InstallmentRuleItemBean());
//    	request.getInstallmentItems().add(new InstallmentRuleItemBean());

//    	model.addAttribute("courseCreate", request);
    	
    	  if(!model.containsAttribute("courseCreate")) {

    	        model.addAttribute(
    	                "courseCreate",
    	                new CourseCreateRequest()
    	        );

    	    }
    	
//    	model.addAttribute(
//    	        "courseCreate",
//    	        new CourseCreateRequest()
//    	);


        model.addAttribute(
                "categories",
                courseCategoryService.getAllCategories()
        );
        
        model.addAttribute(
                "subcategories",
               subCategoryService.getAll()
        );
        
        model.addAttribute(
                "teachers",
                teacherService.getAllTeachers()
        );



        return "admin/admin-course-create";
    }

    
    
    
    //new
//    @PostMapping("/create")
//    public String createCourse(
//            @ModelAttribute("courseCreate") CourseCreateRequest request,
//            HttpSession session) {
//
//
//        CourseBean course = request.getCourse();
//
//
//
//        // ================================
//        // CREATE COURSE
//        // ================================
//
//        course.setCourseId(
//                UUID.randomUUID().toString()
//        );
//
//
//        String adminId =
//                (String) session.getAttribute("userID");
//
//
//        course.setCreatedBy(adminId);
//
//
//
//        // Checkbox handling
//
//        if(course.getAllowedInstallment() == null) {
//
//            course.setAllowedInstallment(0);
//
//        }
//
//
//        if(course.getAllowedScholarship() == null) {
//
//            course.setAllowedScholarship(0);
//
//        }
//
//
//
//        // Available seats initially
//
//        course.setSeatsAvailable(
//                course.getSeatsTotal()
//        );
//
//
//
//        courseService.createCourse(course);
//
//
//
//
//
//        // ================================
//        // CREATE INSTALLMENT RULE
//        // ================================
//
//
//        if(course.getAllowedInstallment() == 1) {
//
//
//
//            InstallmentRuleBean rule =
//                    new InstallmentRuleBean();
//
//
//
//            rule.setInstallmentRuleId(
//                    UUID.randomUUID().toString()
//            );
//
//
//            rule.setCourseId(
//                    course.getCourseId()
//            );
//
//
//
//            // Default rule name
//
//            if(request.getInstallmentRuleName() == null
//                    || request.getInstallmentRuleName().isBlank()) {
//
//
//                request.setInstallmentRuleName(
//                        "Default Installment Plan"
//                );
//
//            }
//
//
//            rule.setName(
//                    request.getInstallmentRuleName()
//            );
//
//
//
//            rule.setInstallmentCount(
//                    request.getInstallmentCount()
//            );
//
//
//            rule.setIsActive(1);
//
//
//
//            rule.setCreatedById(
//                    course.getCreatedBy()
//            );
//
//
//
//            installmentRuleService.createRule(rule);
//
//
//
//
//
//            // ================================
//            // CREATE INSTALLMENT ITEMS
//            // ================================
//
//
//            if(request.getInstallmentItems() != null
//                    && !request.getInstallmentItems().isEmpty()) {
//
//
//
//                System.out.println(
//                        "===== ITEMS RECEIVED ====="
//                );
//
//
//                System.out.println(
//                        "INSTALLMENT ITEM COUNT = "
//                        + request.getInstallmentItems().size()
//                );
//
//
//
//                int installmentNumber = 1;
//
//
//
//                for(InstallmentRuleItemBean item :
//                        request.getInstallmentItems()) {
//
//
//
//                    System.out.println(
//                            "Amount = "
//                            + item.getAmount()
//                            + " Date = "
//                            + item.getDueDate()
//                    );
//
//
//
//                    // Skip empty rows
//
//                    if(item.getAmount() == null
//                            || item.getDueDate() == null) {
//
//                        continue;
//
//                    }
//
//
//
//
//                    item.setInstallmentRuleItemId(
//                            UUID.randomUUID().toString()
//                    );
//
//
//
//                    item.setInstallmentRuleId(
//                            rule.getInstallmentRuleId()
//                    );
//
//
//
//                    item.setInstallmentNumber(
//                            installmentNumber++
//                    );
//
//
//
//                    installmentRuleItemService.createItem(item);
//
//
//
//                }
//
//
//
//                System.out.println(
//                        "=========================="
//                );
//
//
//            }
//
//
//
//        }
//
//
//
//
//        return "redirect:/admin/courses";
//
//    }
    
    
    
    //neww
    @PostMapping("/create")
    public String createCourse(
            @ModelAttribute("courseCreate") CourseCreateRequest request,
            HttpSession session,
            RedirectAttributes redirectAttributes) {


        String adminId =
                (String) session.getAttribute("userID");


        try {


            courseCreationService.createCourse(
                    request,
                    adminId
            );


        } catch (IllegalArgumentException e) {


            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    e.getMessage()
            );


            return "redirect:/admin/courses/create";

        }


        return "redirect:/admin/courses";

    }
    
    @GetMapping("/{id}")
    public String viewCourse(
            @PathVariable("id") String id,
            Model model) {

        CourseBean course = courseService.getById(id);

        model.addAttribute("course", course);

        return "admin/admin-course-detail";
    }
    
    
    @GetMapping("/{id}/edit")
    public String editCoursePage(
            @PathVariable("id") String id,
            Model model) {
    	
    	
    	

        CourseBean course = courseService.getById(id);
        
        //test
        System.out.println("===== EDIT PAGE =====");
    	System.out.println(course.getCreatedBy());
    	System.out.println(course.getSeatsAvailable());

        model.addAttribute("course", course);

        model.addAttribute(
                "categories",
                courseCategoryService.getAllCategories());

        model.addAttribute(
                "subcategories",
                subCategoryService.getAll());

        model.addAttribute(
                "teachers",
                teacherService.getAllTeachers());

        return "admin/admin-course-edit";
    }
    
    
//    @PostMapping("/{id}/edit")
//    public String updateCourse(
//            @PathVariable String id,
//            @ModelAttribute("course") CourseBean course) {
//    	
//    	CourseBean existingCourse =
//    	        courseService.getById(course.getCourseId());
//
//        course.setCourseId(id);
//
//        courseService.updateCourse(course);
//
//        return "redirect:/admin/courses/" + id;
//    }
    
    @PostMapping("/{id}/edit")
    public String updateCourse(
    		@PathVariable("id") String id,
            @ModelAttribute("course") CourseBean course) {
    	
    	
    	System.out.println("===== FORM VALUES =====");
    	System.out.println("Course ID        = " + course.getCourseId());
    	System.out.println("Created By       = " + course.getCreatedBy());
    	System.out.println("Total Seats      = " + course.getSeatsTotal());
    	System.out.println("Available Seats  = " + course.getSeatsAvailable());
    	System.out.println("Installment      = " + course.getAllowedInstallment());
    	System.out.println("Scholarship      = " + course.getAllowedScholarship());
    	System.out.println("=======================");

        // Load existing course from database
        CourseBean existingCourse =
                courseService.getById(id);

        // -----------------------------
        // Update editable fields
        // -----------------------------
        existingCourse.setName(course.getName());

        existingCourse.setDescription(course.getDescription());

        existingCourse.setCourseCategoryId(course.getCourseCategoryId());

        existingCourse.setSubcategoryId(course.getSubcategoryId());

        existingCourse.setTeacherId(course.getTeacherId());

        existingCourse.setDurationWeeks(course.getDurationWeeks());

        existingCourse.setFee(course.getFee());

        existingCourse.setLevel(course.getLevel());

        existingCourse.setStatus(course.getStatus());

        // -----------------------------
        // Checkbox handling
        // -----------------------------
        if (course.getAllowedInstallment() == null) {
            existingCourse.setAllowedInstallment(0);
        } else {
            existingCourse.setAllowedInstallment(1);
        }

        if (course.getAllowedScholarship() == null) {
            existingCourse.setAllowedScholarship(0);
        } else {
            existingCourse.setAllowedScholarship(1);
        }

        // -----------------------------
        // Seat handling
        // Keep enrolled students unchanged
        // -----------------------------
        int enrolled =
                enrollmentService.countEnrolledStudents(id);

        existingCourse.setSeatsTotal(course.getSeatsTotal());

        existingCourse.setSeatsAvailable(
                course.getSeatsTotal() - enrolled);

        // Prevent negative available seats
        if (existingCourse.getSeatsAvailable() < 0) {
            existingCourse.setSeatsAvailable(0);
        }

        // -----------------------------
        // Save
        // -----------------------------
        
        System.out.println("===== BEFORE REPOSITORY =====");
        System.out.println(existingCourse.getCreatedBy());
        System.out.println(existingCourse.getSeatsAvailable());
        
        courseService.updateCourse(existingCourse);
        
        System.out.println("===== BEFORE UPDATE =====");
        System.out.println("Course ID        = " + existingCourse.getCourseId());
        System.out.println("Created By       = " + existingCourse.getCreatedBy());
        System.out.println("Total Seats      = " + existingCourse.getSeatsTotal());
        System.out.println("Available Seats  = " + existingCourse.getSeatsAvailable());
        System.out.println("Installment      = " + existingCourse.getAllowedInstallment());
        System.out.println("Scholarship      = " + existingCourse.getAllowedScholarship());
        System.out.println("=========================");

        return "redirect:/admin/courses";
    }
    
    
    @PostMapping("/{id}/delete")
    public String deleteCourse(
            @PathVariable("id") String id,
            RedirectAttributes redirectAttributes, HttpSession session) {
    	
    	String adminId =
    	        (String) session.getAttribute("userID");


        courseService.deleteCourse(id, adminId);


        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Course archived successfully."
        );


        return "redirect:/admin/courses";
    }
    
    
    @GetMapping("/archive")
    public String archivedCourses(
    		 @RequestParam(required = false) String keyword,
    	        @RequestParam(required = false) String status,
    		
    		Model model) {

//        model.addAttribute(
//            "courses",
//            courseService.getArchivedCourses()
//        );
    	
    	
    	List<CourseBean> courses;


        if((keyword != null && !keyword.isBlank())
                || (status != null && !status.isBlank())) {


            courses =
                courseService.searchAndFilterArchivedCourses(
                        keyword,
                        status
                );


        } else {


            courses =
                courseService.getArchivedCourses();

        }
        
        model.addAttribute(
                "courses",
                courses
        );
        
        model.addAttribute(
                "totalArchivedCourses",
                courseService.countArchivedCourses()
        );


        model.addAttribute(
                "keyword",
                keyword
        );


        model.addAttribute(
                "status",
                status
        );


        model.addAttribute(
            "archivedThisMonth",
            courseService.countArchivedThisMonth()
        );


        return "admin/admin-archived-courses";
    }
    
    
    @PostMapping("/{id}/restore")
    public String restoreCourse(
            @PathVariable("id") String id,
            RedirectAttributes redirectAttributes) {


        courseService.restoreCourse(id);


        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Course restored successfully."
        );


        return "redirect:/admin/courses/archive";
    }
}