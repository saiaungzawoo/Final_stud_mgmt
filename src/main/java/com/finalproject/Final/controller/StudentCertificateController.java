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
@RequestMapping("/student/certificate")
public class StudentCertificateController {


    private final CertificateRepository certificateRepo;


    public StudentCertificateController(
            CertificateRepository certificateRepo
    ){
        this.certificateRepo = certificateRepo;
    }



    @GetMapping("/list")
    public String certificateList(
            HttpSession session,
            Model model
    ){


        UserBean loginUser =
                (UserBean) session.getAttribute("loginUser");


        if(loginUser == null){

            return "redirect:/login";

        }



        model.addAttribute(
                "certificateList",
                certificateRepo.getStudentCertificateList(
                        loginUser.getUserID()
                )
        );


        return "student/certificate-list";

    }
    @GetMapping("/download/{certificateID}")
    public ResponseEntity<Resource> downloadStudentCertificate(
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


}