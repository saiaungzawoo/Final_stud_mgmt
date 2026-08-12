 package com.finalproject.Final.service;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.finalproject.Final.model.CertificateBean;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class CertificatePdfService {

    public String generateCertificate(CertificateBean certificate) {

        try {
            String folderPath = "uploads/certificate/";
            File folder = new File(folderPath);

            if (!folder.exists()) {
                folder.mkdirs();
            }

            String studentName = certificate.getStudentName() == null
                    ? "Student"
                    : certificate.getStudentName().replace(" ", "_");

            String fileName = "ACE_Certificate_" + studentName + ".pdf";
            String filePath = folderPath + fileName;

            // Landscape A4 Document
            Document document = new Document(PageSize.A4.rotate(), 40, 40, 40, 40);

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            
            // Register Border Event
            writer.setPageEvent(new CertificateBorder());

            document.open();

            // 🎨 CSS Palette Matching Colors
            Color darkBlue = new Color(13, 71, 161);    // #0d47a1
            Color midBlue = new Color(21, 101, 192);    // #1565c0
            Color lightBlue = new Color(25, 118, 210);  // #1976d2
            Color inkBlue = new Color(11, 60, 93);      // #0b3c5d (Dark Ink Blue)
            Color grayText = new Color(108, 117, 125);  // Gray Text

            // ✍️ Fonts Definition
            Font academyFont = new Font(Font.HELVETICA, 16, Font.BOLD, darkBlue);
            Font titleFont = new Font(Font.HELVETICA, 28, Font.BOLD, midBlue);
            Font studentFont = new Font(Font.TIMES_ROMAN, 30, Font.BOLD, darkBlue);
            Font courseFont = new Font(Font.HELVETICA, 18, Font.BOLDITALIC, lightBlue);
            
            Font normalFont = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);
            Font smallLabelFont = new Font(Font.HELVETICA, 10, Font.BOLD, grayText);
            Font infoValueFont = new Font(Font.HELVETICA, 16, Font.BOLD, darkBlue);

            // ✒️ Custom Great Vibes Cursive Signature Font Setup
            Font signatureFont;
            try {
              BaseFont customBase = BaseFont.createFont(
                      "src/main/resources/fonts/GreatVibes-Regular.ttf",
                       BaseFont.IDENTITY_H,
                      BaseFont.EMBEDDED
              );

              signatureFont = new Font(customBase, 38, Font.NORMAL, inkBlue);
            } catch (Exception e) {
                // Fallback Font if TTF file is missing
                signatureFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLDITALIC, inkBlue);
            }

            // 1. Academy Name
            Paragraph academy = new Paragraph("Ace Inspiration", academyFont);
            academy.setAlignment(Element.ALIGN_CENTER);
            document.add(academy);

            // 2. Certificate Title
            Paragraph title = new Paragraph("CERTIFICATE\nOF COMPLETION\n", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // Presentation Line
            Paragraph p1 = new Paragraph("This certificate is proudly presented to", normalFont);
            p1.setAlignment(Element.ALIGN_CENTER);
            document.add(p1);

            // 3. Student Name
            Paragraph student = new Paragraph(certificate.getStudentName(), studentFont);
            student.setAlignment(Element.ALIGN_CENTER);
            document.add(student);
 // Completion Line
            Paragraph p2 = new Paragraph("For successfully completing", normalFont);
            p2.setAlignment(Element.ALIGN_CENTER);
            document.add(p2);

            // 4. Course Name
            Paragraph course = new Paragraph(certificate.getCourseName(), courseFont);
            course.setAlignment(Element.ALIGN_CENTER);
            document.add(course);

            // 5. Info Box (3 Columns Layout)
            PdfPTable infoTable = new PdfPTable(3);
            infoTable.setWidthPercentage(65);
            infoTable.setSpacingBefore(12f);
            infoTable.setSpacingAfter(10f);

            // Final Score Column
            PdfPCell scoreCell = createInfoCell("Final Score", String.valueOf(certificate.getFinalScore()), smallLabelFont, infoValueFont);
            
            // Grade Column
            PdfPCell gradeCell = createInfoCell("Grade", certificate.getLetterGrade(), smallLabelFont, infoValueFont);
            
            // Issue Date Column
            PdfPCell dateCell = createInfoCell("Issue Date", LocalDate.now().toString(), smallLabelFont, infoValueFont);

            infoTable.addCell(scoreCell);
            infoTable.addCell(gradeCell);
            infoTable.addCell(dateCell);

            document.add(infoTable);

            // 6. Signature Area (Teacher & Director Side-by-Side)
            PdfPTable signatureTable = new PdfPTable(2);
            signatureTable.setWidthPercentage(80);
            signatureTable.setSpacingBefore(10f);
            signatureTable.setKeepTogether(true);

            // Left Side: Issued By (Teacher Signature)
            PdfPCell teacherCell = new PdfPCell();
            teacherCell.setBorder(0);
            teacherCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            Paragraph issuedByLbl = new Paragraph("Issued By:", smallLabelFont);
            issuedByLbl.setAlignment(Element.ALIGN_CENTER);

            String teacherName = (certificate.getIssuedByName() != null && !certificate.getIssuedByName().isEmpty()) 
                    ? certificate.getIssuedByName() 
                    : "Teacher Name";

            Paragraph printedTeacherName = new Paragraph(teacherName, new Font(Font.HELVETICA, 11, Font.BOLD, darkBlue));
            printedTeacherName.setAlignment(Element.ALIGN_CENTER);

            Paragraph fakeTeacherSig = new Paragraph(teacherName, signatureFont);
            fakeTeacherSig.setAlignment(Element.ALIGN_CENTER);

            Paragraph sigLine1 = new Paragraph("____________________", normalFont);
            sigLine1.setAlignment(Element.ALIGN_CENTER);

            Paragraph instructorLbl = new Paragraph("Instructor", smallLabelFont);
            instructorLbl.setAlignment(Element.ALIGN_CENTER);

            teacherCell.addElement(issuedByLbl);
            teacherCell.addElement(printedTeacherName);
            teacherCell.addElement(fakeTeacherSig);
            teacherCell.addElement(sigLine1);
            teacherCell.addElement(instructorLbl);

            // Right Side: Director Signature
            PdfPCell directorCell = new PdfPCell();
            directorCell.setBorder(0);
            directorCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            Paragraph emptySpace = new Paragraph(" ", smallLabelFont);
            
            Paragraph fakeDirectorSig = new Paragraph("A. Johnson", signatureFont);
            fakeDirectorSig.setAlignment(Element.ALIGN_CENTER);

            Paragraph sigLine2 = new Paragraph("____________________", normalFont);
            sigLine2.setAlignment(Element.ALIGN_CENTER);

            Paragraph directorLbl = new Paragraph("Director Signature", smallLabelFont);
            directorLbl.setAlignment(Element.ALIGN_CENTER);

            directorCell.addElement(emptySpace);
            directorCell.addElement(emptySpace); // Spacing alignment
            directorCell.addElement(fakeDirectorSig);
            directorCell.addElement(sigLine2);
            directorCell.addElement(directorLbl);
 signatureTable.addCell(teacherCell);
            signatureTable.addCell(directorCell);

            document.add(signatureTable);

            document.close();

            return filePath;

        } catch (Exception e) {
            throw new RuntimeException("Certificate PDF Generate Failed", e);
        }
    }

    // Helper Method for Info Table Cells
    private PdfPCell createInfoCell(String label, String value, Font labelFont, Font valueFont) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(0);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        
        Paragraph lblPara = new Paragraph(label, labelFont);
        lblPara.setAlignment(Element.ALIGN_CENTER);
        
        Paragraph valPara = new Paragraph(value, valueFont);
        valPara.setAlignment(Element.ALIGN_CENTER);

        cell.addElement(lblPara);
        cell.addElement(valPara);
        return cell;
    }
}
