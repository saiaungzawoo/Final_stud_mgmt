 package com.finalproject.Final.repository;


import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.finalproject.Final.model.CertificateBean;


@Repository
public class CertificateRepository {


    private final JdbcTemplate jdbcTemplate;


    public CertificateRepository(
            JdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
    }



    /*
     * 1. Certificate Generate လုပ်နိုင်တဲ့
     * Completed Final Grade List
     */
    public List<CertificateBean> getCompletedFinalGradeList(){


        String sql = """
                
            SELECT

                fg.finalGradeID,
                fg.final_score,
                fg.letter_grade,

                e.enrollmentID,

                u.name AS studentName,

                c.name AS courseName


            FROM final_grade fg


            JOIN enrollment e
            ON fg.enrollmentID = e.enrollmentID


            JOIN user u
            ON e.userID = u.userID


            JOIN course c
            ON e.courseID = c.courseID


            WHERE fg.status = 'Completed'


            AND NOT EXISTS (

                SELECT 1
                FROM certificate cer
                WHERE cer.finalGradeID = fg.finalGradeID

            )


            ORDER BY u.name


            """;


        return jdbcTemplate.query(

                sql,

                (rs, rowNum) -> {


                    CertificateBean bean =
                            new CertificateBean();



                    bean.setFinalGradeID(
                            rs.getString("finalGradeID")
                    );



                    bean.setEnrollmentID(
                            rs.getString("enrollmentID")
                    );



                    bean.setStudentName(
                            rs.getString("studentName")
                    );



                    bean.setCourseName(
                            rs.getString("courseName")
                    );



                    bean.setFinalScore(
                            rs.getBigDecimal("final_score")
                    );



                    bean.setLetterGrade(
                            rs.getString("letter_grade")
                    );



                    return bean;


                }

        );

    }

    /*
     * 2. Certificate Save
     */
    public int saveCertificate(
            CertificateBean certificate
    ) {


        String sql = """
                INSERT INTO certificate
                (
                    certificateID,
                    enrollmentID,
                    certificate_number,
                    issue_date,
                    expiry_date,
                    finalGradeID,
                    issuedByID,
                    template_path,
                    pdf_path,
                    created_at
                )

                VALUES
                (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW())
                """;



        return jdbcTemplate.update(
                sql,

                certificate.getCertificateID(),

                certificate.getEnrollmentID(),

                certificate.getCertificateNumber(),

                certificate.getIssueDate(),

                certificate.getExpiryDate(),

                certificate.getFinalGradeID(),

                certificate.getIssuedByID(),

                certificate.getTemplatePath(),

                certificate.getPdfPath()

        );

    }





    /*
     * 3. Certificate Already Generated Check
     */
    public boolean existsByFinalGradeID(
            String finalGradeID
    ) {


        String sql = """
                SELECT COUNT(*)
                FROM certificate
                WHERE finalGradeID = ?
                """;


        Integer count =
                jdbcTemplate.queryForObject(
                        sql,
                        Integer.class,
                        finalGradeID
                );
return count > 0;

    }






    /*
     * 4. Generated Certificate List
     */
    public List<CertificateBean> getCertificateList() {


        String sql = """
                SELECT

                    cer.certificateID,
                    cer.certificate_number,
                    cer.issue_date,

                    u.name AS studentName,

                    c.name AS courseName,

                    fg.final_score,
                    fg.letter_grade


                FROM certificate cer


                JOIN final_grade fg
                    ON cer.finalGradeID = fg.finalGradeID


                JOIN enrollment e
                    ON cer.enrollmentID = e.enrollmentID


                JOIN user u
                    ON e.userID = u.userID


                JOIN course c
                    ON e.courseID = c.courseID


                ORDER BY cer.created_at DESC

                """;



        return jdbcTemplate.query(
                sql,
                (rs,rowNum)->{


                    CertificateBean bean =
                            new CertificateBean();



                    bean.setCertificateID(
                            rs.getString("certificateID")
                    );


                    bean.setCertificateNumber(
                            rs.getString("certificate_number")
                    );


                    bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    bean.setIssueDate(
                            rs.getDate("issue_date")
                            .toLocalDate()
                    );


                    bean.setFinalScore(
                            rs.getBigDecimal("final_score")
                    );


                    bean.setLetterGrade(
                            rs.getString("letter_grade")
                    );


                    return bean;

                }
        );

    }






    /*
     * 5. Get Final Grade Status
     * Generate မလုပ်ခင် check
     */
    public String getFinalGradeStatus(
            String finalGradeID
    ) {


        String sql = """
                SELECT status
                FROM final_grade
                WHERE finalGradeID = ?
                """;


        return jdbcTemplate.queryForObject(
                sql,
                String.class,
                finalGradeID
        );

    }





    /*
     * 6. Get Enrollment ID
     */
    public String getEnrollmentByFinalGradeID(
            String finalGradeID
    ) {


        String sql = """
                SELECT enrollmentID
                FROM final_grade
                WHERE finalGradeID = ?
                """;


        return jdbcTemplate.queryForObject(
                sql,
                String.class,
                finalGradeID
        );

    }
    public CertificateBean getCertificateByID(
            String certificateID
    ) {


        String sql = """
                SELECT
                    c.*,
                    u.name AS studentName,
                    co.name AS courseName,
                    fg.final_score,
                    fg.letter_grade

                FROM certificate c

                JOIN enrollment e
                ON c.enrollmentID = e.enrollmentID

                JOIN user u
                ON e.userID = u.userID

                JOIN course co
                ON e.courseID = co.courseID

                JOIN final_grade fg
                ON c.finalGradeID = fg.finalGradeID

                WHERE c.certificateID = ?
                """;


        return jdbcTemplate.queryForObject(
                sql,
                (rs,rowNum)->{


                    CertificateBean bean =
                            new CertificateBean();


                    bean.setCertificateID(
                            rs.getString("certificateID")
                    );
 bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    bean.setPdfPath(
                            rs.getString("pdf_path")
                    );


                    bean.setFinalScore(
                            rs.getBigDecimal("final_score")
                    );


                    bean.setLetterGrade(
                            rs.getString("letter_grade")
                    );


                    return bean;

                },
                certificateID
        );

    }
    public List<CertificateBean> getStudentCertificate(
            String studentID
    ){


        String sql = """
            SELECT
                c.certificateID,
                c.certificate_number,
                c.issue_date,
                co.name AS courseName

            FROM certificate c

            JOIN enrollment e
            ON c.enrollmentID = e.enrollmentID

            JOIN course co
            ON e.courseID = co.courseID

            WHERE e.userID = ?

            ORDER BY c.issue_date DESC
            """;



        return jdbcTemplate.query(
                sql,
                (rs,rowNum)->{


                    CertificateBean bean =
                            new CertificateBean();


                    bean.setCertificateID(
                            rs.getString("certificateID")
                    );


                    bean.setCertificateNumber(
                            rs.getString("certificate_number")
                    );


                    bean.setIssueDate(
                            rs.getDate("issue_date")
                                    .toLocalDate()
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    return bean;

                },
                studentID
        );

    }
    public List<CertificateBean> getCompletedFinalGradesByTeacher(
            String teacherID
    ) {


        String sql = """
                
            SELECT

                fg.finalGradeID,
                fg.finalScore,
                fg.letterGrade,

                e.enrollmentID,

                u.name AS studentName,

                c.name AS courseName


            FROM final_grade fg


            JOIN enrollment e
            ON fg.enrollmentID = e.enrollmentID


            JOIN user u
            ON e.userID = u.userID


            JOIN course c
            ON e.courseID = c.courseID


            WHERE c.teacherID = ?

            AND fg.status = 'Completed'


            AND NOT EXISTS (

                SELECT 1
                FROM certificate cer
                WHERE cer.finalGradeID = fg.finalGradeID

            )


            ORDER BY u.name


            """;



        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {


                    CertificateBean bean =
                            new CertificateBean();


                    bean.setFinalGradeID(
                            rs.getString("finalGradeID")
                    );


                    bean.setEnrollmentID(
                            rs.getString("enrollmentID")
                    );


                    bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    bean.setFinalScore(
                            rs.getBigDecimal("finalScore")
                    );

                    bean.setLetterGrade(
                            rs.getString("letterGrade")
                    );


                    return bean;


                },
                teacherID
        );

    }
    public List<CertificateBean> getGeneratedCertificateList(){


        String sql = """
 SELECT

                c.certificateID,
                c.certificate_number,
                c.issue_date,
                c.pdf_path,

                fg.finalGradeID,
                fg.final_score,
                fg.letter_grade,

                student.name AS studentName,

                co.name AS courseName,

                issuer.name AS issuedByName


            FROM certificate c


            JOIN final_grade fg
            ON c.finalGradeID = fg.finalGradeID


            JOIN enrollment e
            ON c.enrollmentID = e.enrollmentID


            JOIN user student
            ON e.userID = student.userID


            JOIN course co
            ON e.courseID = co.courseID


            LEFT JOIN user issuer
            ON c.issuedByID = issuer.userID


            ORDER BY c.issue_date DESC


            """;



        return jdbcTemplate.query(

                sql,

                (rs, rowNum) -> {


                    CertificateBean bean =
                            new CertificateBean();



                    bean.setCertificateID(
                            rs.getString("certificateID")
                    );


                    bean.setCertificateNumber(
                            rs.getString("certificate_number")
                    );


                    bean.setIssueDate(
                            rs.getDate("issue_date")
                                    .toLocalDate()
                    );


                    bean.setPdfPath(
                            rs.getString("pdf_path")
                    );


                    bean.setFinalGradeID(
                            rs.getString("finalGradeID")
                    );


                    bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    bean.setFinalScore(
                            rs.getBigDecimal("final_score")
                    );


                    bean.setLetterGrade(
                            rs.getString("letter_grade")
                    );


                    bean.setIssuedByName(
                            rs.getString("issuedByName")
                    );


                    return bean;

                }

        );

    }
    public CertificateBean getFinalGradeByID(String finalGradeID){

        String sql = """
            
            SELECT

                fg.final_score,
                fg.letter_grade,

                u.name AS studentName,

                c.name AS courseName


            FROM final_grade fg


            JOIN enrollment e
            ON fg.enrollmentID = e.enrollmentID


            JOIN user u
            ON e.userID = u.userID


            JOIN course c
            ON e.courseID = c.courseID


            WHERE fg.finalGradeID = ?

            """;


        return jdbcTemplate.queryForObject(

                sql,

                (rs,rowNum)->{


                    CertificateBean bean =
                            new CertificateBean();


                    bean.setFinalScore(
                            rs.getBigDecimal("final_score")
                    );


                    bean.setLetterGrade(
                            rs.getString("letter_grade")
                    );


                    bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    return bean;

                },

                finalGradeID

        );

    }
    public List<CertificateBean> getStudentCertificateList(String studentID){


        String sql = """

            SELECT

                c.certificateID,
                c.certificate_number,
                c.issue_date,
                c.pdf_path,


                fg.final_score,
                fg.letter_grade,
 u.name AS studentName,


                co.name AS courseName,


                issuer.name AS issuedByName



            FROM certificate c



            JOIN final_grade fg
            ON c.finalGradeID = fg.finalGradeID



            JOIN enrollment e
            ON c.enrollmentID = e.enrollmentID



            JOIN user u
            ON e.userID = u.userID



            JOIN course co
            ON e.courseID = co.courseID



            LEFT JOIN user issuer
            ON c.issuedByID = issuer.userID



            WHERE e.userID = ?



            ORDER BY c.issue_date DESC


            """;



        return jdbcTemplate.query(
                sql,
                (rs,rowNum)->{


                    CertificateBean bean =
                            new CertificateBean();



                    bean.setCertificateID(
                            rs.getString("certificateID")
                    );


                    bean.setCertificateNumber(
                            rs.getString("certificate_number")
                    );


                    bean.setIssueDate(
                            rs.getDate("issue_date")
                                    .toLocalDate()
                    );


                    bean.setPdfPath(
                            rs.getString("pdf_path")
                    );



                    bean.setFinalScore(
                            rs.getBigDecimal("final_score")
                    );


                    bean.setLetterGrade(
                            rs.getString("letter_grade")
                    );


                    bean.setStudentName(
                            rs.getString("studentName")
                    );


                    bean.setCourseName(
                            rs.getString("courseName")
                    );


                    // Add this
                    bean.setIssuedByName(
                            rs.getString("issuedByName")
                    );
                    System.out.println(
                          "Issued By Name >>> " + bean.getIssuedByName()
                      );


                    return bean;

                },
                studentID
        );

    }
}
