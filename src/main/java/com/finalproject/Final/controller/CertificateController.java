package com.finalproject.Final.controller;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.finalproject.Final.model.CertificateBean;
import com.finalproject.Final.model.UserBean;
import com.finalproject.Final.repository.CertificateRepository;
import com.finalproject.Final.service.CertificatePdfService;

import jakarta.servlet.http.HttpSession;



@Controller
@RequestMapping("/teacher/certificate")
public class CertificateController {



    private final CertificateRepository certificateRepo;
    private final CertificatePdfService certificatePdfService;


    public CertificateController(
            CertificateRepository certificateRepo,
            CertificatePdfService certificatePdfService
    ){

        this.certificateRepo = certificateRepo;
        this.certificatePdfService = certificatePdfService;

    }




    // ===============================
    // Certificate List Page
    // Completed Final Grade Only
    // ===============================

    @GetMapping("/list")
    public String certificateList(
            Model model,
            HttpSession session
    ){


        model.addAttribute(
                "certificateList",
                certificateRepo.getCompletedFinalGradeList()
        );


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");


        model.addAttribute(
                "loginUser",
                loginUser
        );


        return "teacher/certificate-list";

    }






    // ===============================
    // Generate Certificate
    // ===============================

    @PostMapping("/generate/{finalGradeID}")
    public String generateCertificate(

            @PathVariable String finalGradeID,

            HttpSession session,

            RedirectAttributes redirectAttributes

    ){



        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");



        if(loginUser == null){

            return "redirect:/login";

        }




        // 1. Check Final Grade Status

        String status =
                certificateRepo.getFinalGradeStatus(finalGradeID);



        if(!"Completed".equals(status)){


            redirectAttributes.addFlashAttribute(
                    "error",
                    "Only completed final grades can generate certificate."
            );


            return "redirect:/teacher/certificate/list";

        }






        // 2. Check Already Generated

        if(
          certificateRepo.existsByFinalGradeID(finalGradeID)
        ){


            redirectAttributes.addFlashAttribute(
                    "error",
                    "Certificate already generated."
            );


            return "redirect:/teacher/certificate/list";

        }






        // 3. Get Enrollment ID

        String enrollmentID =
                certificateRepo.getEnrollmentByFinalGradeID(finalGradeID);






        // 4. Create Certificate

        CertificateBean certificate =
                new CertificateBean();



        certificate.setCertificateID(
                UUID.randomUUID().toString()
        );



        certificate.setEnrollmentID(
                enrollmentID
        );
        
        certificate.setFinalGradeID(
                finalGradeID
        );



        CertificateBean finalGradeData =
                certificateRepo.getFinalGradeByID(finalGradeID);


        certificate.setFinalScore(
                finalGradeData.getFinalScore()
        );


        certificate.setLetterGrade(
                finalGradeData.getLetterGrade()
        );
        certificate.setStudentName(
                finalGradeData.getStudentName()
        );


        certificate.setCourseName(
                finalGradeData.getCourseName()
        );

        certificate.setCertificateNumber(
                "CERT-" + System.currentTimeMillis()
        );



        certificate.setIssueDate(
                LocalDate.now()
        );



        certificate.setIssuedByID(
                loginUser.getUserID()
        );
        certificate.setIssuedByName(
                loginUser.getName()
        );

     // Generate PDF

        String pdfPath =
                certificatePdfService.generateCertificate(
                        certificate
                );


        certificate.setPdfPath(
                pdfPath
        );

        certificateRepo.saveCertificate(
                certificate
        );





        redirectAttributes.addFlashAttribute(
                "success",
                "Certificate Generated Successfully"
        );



        return "redirect:/teacher/certificate/list";

    }
    @GetMapping("/view/{certificateID}")
    public String viewCertificate(
            @PathVariable String certificateID,
            Model model
    ) {


        CertificateBean certificate =
                certificateRepo.getCertificateByID(certificateID);


        model.addAttribute(
                "certificate",
                certificate
        );


        return "teacher/certificate-view";

    }
	
    @GetMapping("/download/{certificateID}")
    public ResponseEntity<Resource> downloadCertificate(
            @PathVariable String certificateID
    ) {


        CertificateBean certificate =
                certificateRepo.getCertificateByID(certificateID);



        File file =
                new File(
                    certificate.getPdfPath()
                );


        if(!file.exists()){

            throw new RuntimeException(
                "Certificate PDF file not found"
            );

        }



        Resource resource =
                new FileSystemResource(file);



        return ResponseEntity.ok()

                .header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" 
                    + file.getName()
                    + "\""
                )

                .contentType(
                    MediaType.APPLICATION_PDF
                )

                .body(resource);

    }
    @GetMapping("/generated")
	  public String generatedCertificateList(
	          Model model
	  ){

	      model.addAttribute(
	          "certificateList",
	          certificateRepo.getGeneratedCertificateList()
	      );


	      return "teacher/generated-certificate-list";
	  }
	  
	 
}