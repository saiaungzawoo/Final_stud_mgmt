package com.finalproject.Final.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ScholarshipDiscountService {

    @Autowired
    private JdbcTemplate jdbc;


    public Double getDiscountAmount(
            String userId,
            String courseId,
            Double courseFee
    ) {


        String sql = """
            SELECT 
                s.discount_type,
                s.discount_value

            FROM scholarship_application sa

            JOIN scholarship s
            ON sa.scholarshipID = s.scholarshipID

            WHERE sa.userID = ?
            AND s.courseID = ?
            AND sa.status = 'Approved'
            """;


        try {

            return jdbc.queryForObject(
                    sql,
                    (rs, rowNum) -> {


                        String type =
                                rs.getString("discount_type");


                        Double value =
                                rs.getDouble("discount_value");



                        // Percentage discount
                        if("Percentage".equals(type)) {

                            return courseFee * value / 100;

                        }


                        // Fixed amount discount
                        else {

                            return value;

                        }

                    },
                    userId,
                    courseId
            );


        } catch(Exception e) {

            // No approved scholarship
            return 0.0;
        }

    }
    
    
    public String getApprovedScholarshipApplicationId(
            String userId,
            String courseId
    ){

        String sql = """
            SELECT sa.scholarshipApplicationID
            FROM scholarship_application sa
            JOIN scholarship s
            ON sa.scholarshipID = s.scholarshipID
            WHERE sa.userID = ?
            AND s.courseID = ?
            AND sa.status = 'Approved'
            LIMIT 1
            """;
        
        
        List<String> result =
                jdbc.query(
                        sql,
                        (rs, rowNum) ->
                                rs.getString("scholarshipApplicationID"),
                        userId,
                        courseId
                );


        return result.isEmpty()
                ? null
                : result.get(0);


//        try {
//
//            return jdbc.queryForObject(
//                    sql,
//                    String.class,
//                    userId,
//                    courseId
//            );
//
//        } catch(Exception e){
//
//            return null;
//        }
    }

}