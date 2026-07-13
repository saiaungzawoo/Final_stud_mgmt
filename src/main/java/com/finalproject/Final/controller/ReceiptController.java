package com.finalproject.Final.controller;

import java.io.ByteArrayInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.PaymentBean;
import com.finalproject.Final.service.CourseService;
import com.finalproject.Final.service.PaymentService;
import com.finalproject.Final.util.PdfReceiptGenerator;

@RestController
@RequestMapping("/receipt")
public class ReceiptController {

  /*  @Autowired
    private PaymentService paymentService;

    @Autowired
    private CourseService courseService;
*/
   /* @GetMapping("/download/{paymentId}")
    public ResponseEntity<InputStreamResource> download(
            @PathVariable int paymentId)
            throws Exception {

        PaymentBean payment =  paymentService.getById(paymentId);
        
        if (payment == null) {
            return ResponseEntity.notFound().build();
        }
        
        if (payment.getReceiptDownloaded() == 1) {

            return ResponseEntity.noContent().build();

        }
        
      */
}
       

       /* CourseBean course =
                courseService.getById(payment.getCourseId());

        ByteArrayInputStream pdf =
                PdfReceiptGenerator.generate(payment, course);
        
        paymentService.markReceiptDownloaded(paymentId);

        HttpHeaders headers = new HttpHeaders();

        headers.add(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=PaymentReceipt.pdf");
        
       

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(pdf));
    }*/

//}