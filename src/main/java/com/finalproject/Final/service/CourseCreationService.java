package com.finalproject.Final.service;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.finalproject.Final.dto.CourseCreateRequest;
import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.InstallmentRuleBean;
import com.finalproject.Final.model.InstallmentRuleItemBean;


@Service
public class CourseCreationService {


    @Autowired
    private CourseService courseService;


    @Autowired
    private InstallmentRuleService installmentRuleService;


    @Autowired
    private InstallmentRuleItemService installmentRuleItemService;



    @Transactional
    public void createCourse(
            CourseCreateRequest request,
            String adminId
    ){



        CourseBean course =
                request.getCourse();



        // ===============================
        // COURSE SETUP
        // ===============================


        course.setCourseId(
                UUID.randomUUID().toString()
        );


        course.setCreatedBy(
                adminId
        );



        // checkbox handling

        if(course.getAllowedInstallment() == null){

            course.setAllowedInstallment(0);

        }



        if(course.getAllowedScholarship() == null){

            course.setAllowedScholarship(0);

        }



        course.setSeatsAvailable(
                course.getSeatsTotal()
        );



        // save course
        course.setIsActive(1);

        courseService.createCourse(course);





        // ===============================
        // INSTALLMENT RULE
        // ===============================


        if(course.getAllowedInstallment() == 1){



            validateInstallmentAmount(request, course);



            InstallmentRuleBean rule =
                    new InstallmentRuleBean();



            rule.setInstallmentRuleId(
                    UUID.randomUUID().toString()
            );


            rule.setCourseId(
                    course.getCourseId()
            );



            if(request.getInstallmentRuleName() == null
                    || request.getInstallmentRuleName().isBlank()){


                request.setInstallmentRuleName(
                        "Default Installment Plan"
                );

            }



            rule.setName(
                    request.getInstallmentRuleName()
            );



            rule.setInstallmentCount(
                    request.getInstallmentCount()
            );



            rule.setIsActive(1);



            rule.setCreatedById(
                    adminId
            );



            installmentRuleService.createRule(rule);






            // ===============================
            // INSTALLMENT ITEMS
            // ===============================


            int installmentNumber = 1;



            for(InstallmentRuleItemBean item :
                    request.getInstallmentItems()){


                // ignore empty row

                if(item.getAmount()==null
                        || item.getDueDate()==null){

                    continue;

                }



                item.setInstallmentRuleItemId(
                        UUID.randomUUID().toString()
                );



                item.setInstallmentRuleId(
                        rule.getInstallmentRuleId()
                );



                item.setInstallmentNumber(
                        installmentNumber++
                );



                installmentRuleItemService.createItem(item);


            }



        }



    }






    // ===============================
    // VALIDATION
    // ===============================


    private void validateInstallmentAmount(
            CourseCreateRequest request,
            CourseBean course
    ){



        if(request.getInstallmentItems()==null
                || request.getInstallmentItems().isEmpty()){


            throw new IllegalArgumentException(
                    "Installment payment schedule is required."
            );

        }



        double total = 0;



        for(InstallmentRuleItemBean item :
                request.getInstallmentItems()){


            if(item.getAmount()!=null){

                total += item.getAmount();

            }


        }




        if(total != course.getFee()){


            throw new IllegalArgumentException(
                    "Installment total must match course fee. Course creation is cancelled."
            );


        }



    }


}