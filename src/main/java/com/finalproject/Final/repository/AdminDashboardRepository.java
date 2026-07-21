package com.finalproject.Final.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AdminDashboardRepository {


	@Autowired
    private JdbcTemplate jdbcTemplate;


    


    // Count all courses
    public int countCourses() {

        String sql = """
                SELECT COUNT(*)
                FROM course
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }




    // Count students only
    public int countStudents() {

        String sql = """
                SELECT COUNT(*)
                FROM user u
                JOIN role r 
                ON u.roleID = r.roleID
                WHERE r.name = 'Student'
                """;

        return jdbcTemplate.queryForObject(sql, Integer.class);
    }





    // Total collected money
    // Only successful payments
    public double totalSuccessfulPayments() {

        String sql = """
                SELECT COALESCE(SUM(amount),0)
                FROM payment
                WHERE status = 'Success'
                """;


        return jdbcTemplate.queryForObject(sql, Double.class);
    }





    // Count pending payments
    public int countPendingPayments() {

        String sql = """
                SELECT COUNT(*)
                FROM payment
                WHERE status = 'Pending'
                """;


        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

}