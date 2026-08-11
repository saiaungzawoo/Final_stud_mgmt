package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.repository.CourseRepository;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<CourseBean> getAllCourses() {
        return courseRepository.findAll();
    }
    
    public List<CourseBean> getAllCoursesForAdmin() {
        return courseRepository.adminViewActiveCourseList();
    }
    
    //admin 
    public List<CourseBean> getArchivedCourses(){

        return courseRepository.archivedCourseList();

    }

    public CourseBean getById(String id) {
        return courseRepository.findById(id);
    }

    public void createCourse(CourseBean c) {
        courseRepository.save(c);
    }

    public void updateCourse(CourseBean c) {
        courseRepository.update(c);
    }

    //soft delete
    public void deleteCourse(String id, String adminId) {
        courseRepository.delete(id, adminId);
    }
    
    public void decreaseSeat(String courseId) {
        courseRepository.decreaseSeat(courseId);
    }
    
    public int getSeatsAvailable(String courseId){
        return courseRepository.getSeatsAvailable(courseId);
    }
    
    public List<CourseBean> getByCategory(String categoryId){

        return courseRepository.findByCategory(categoryId);

    }
    
    
    public int countCourses(){

        return courseRepository.countAllCourses();

    }


    public int countCoursesByStatus(String status){

        return courseRepository.countByStatus(status);

    }
    
   

    public void restoreCourse(String courseId) {

        courseRepository.restore(courseId);

    }
    
    
    public int countArchivedThisMonth(){

        return courseRepository.countArchivedThisMonth();

    }
    
    public int countArchivedCourses(){

        return courseRepository.countArchivedCourses();

    }
    
    
//    public List<CourseBean> searchAdminCourses(String keyword){
//
//        return courseRepository.searchAdminCourses(keyword);
//
//    }
    
    public List<CourseBean> searchAndFilterCourses(
            String keyword,
            String status) {

        return courseRepository.searchAndFilterCourses(
                keyword,
                status
        );

    }
    
    
    public List<CourseBean> searchAndFilterArchivedCourses(
            String keyword,
            String status) {


        return courseRepository.searchAndFilterArchivedCourses(
                keyword,
                status
        );

    }
    
    
    public List<CourseBean> searchStudentCourses(String keyword){

        return courseRepository.searchStudentCourses(keyword);

    }
    
    public int getScholarshipStatus(String courseId) {

        return courseRepository.getScholarshipStatus(courseId);

    }
    


}
    
    
    //test
//    public CourseBean getById(String id) {
//
//        CourseBean course = courseRepository.findById(id);
//
//        System.out.println("===== COURSE SERVICE =====");
//        System.out.println("Course ID = " + course.getCourseId());
//        System.out.println("Scholarship = " + course.getAllowedScholarship());
//        System.out.println("==========================");
//
//        return course;
//    }
//}