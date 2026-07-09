package com.finalproject.Final.util;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.finalproject.Final.model.CourseBean;
import com.finalproject.Final.model.PaymentBean;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.draw.LineSeparator;

import org.springframework.core.io.ClassPathResource;

public class PdfReceiptGenerator {

    public static ByteArrayInputStream generate(
            PaymentBean payment,
            CourseBean course) throws Exception {

        Document document = new Document(PageSize.A4, 40, 40, 40, 40);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        // =========================
        // COLORS (EDUCATIONAL STYLE)
        // =========================
        Color primary = new Color(235, 245, 255); // very soft blue       
        Color accent = new Color(13, 110, 253);      // blue accent
        Color softBg = new Color(248, 249, 250);      // light background
        Color border = new Color(222, 226, 230);     // border gray
        Color success = new Color(25, 135, 84);      // success green

        // =========================
        // FONTS
        // =========================
        Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD, Color.BLACK);
        Font subTitleFont = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.DARK_GRAY);

        Font sectionFont = new Font(Font.HELVETICA, 14, Font.BOLD, new Color(33, 37, 41));
        Font labelFont = new Font(Font.HELVETICA, 11, Font.BOLD, Color.BLACK);
        Font valueFont = new Font(Font.HELVETICA, 11, Font.NORMAL, Color.BLACK);

        // =========================
        // HEADER (LOGO + TITLE)
        // =========================
        PdfPTable header = new PdfPTable(2);
        header.setWidthPercentage(100);
        header.setWidths(new float[]{1, 4});

        // ---- Logo ----
        PdfPCell logoCell = new PdfPCell();
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setBackgroundColor(primary);
        logoCell.setPadding(10);

        try {
            ClassPathResource resource = new ClassPathResource("static/images/ace_logo.png");
            Image logo = Image.getInstance(resource.getInputStream().readAllBytes());
            logo.scaleToFit(60, 60);
            logoCell.addElement(logo);
        } catch (Exception e) {
            // ignore logo if missing
        }

        header.addCell(logoCell);

        // ---- Title ----
        PdfPCell titleCell = new PdfPCell();
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setBackgroundColor(primary);
        titleCell.setPadding(15);

        Paragraph title = new Paragraph("STUDENT PAYMENT RECEIPT", titleFont);
        Paragraph subtitle = new Paragraph("Official System Generated Document", subTitleFont);

        title.setSpacingAfter(5);
        titleCell.addElement(title);
        titleCell.addElement(subtitle);

        header.addCell(titleCell);

        document.add(header);
        
        LineSeparator line = new LineSeparator();
        line.setLineColor(new Color(13, 110, 253));
        document.add(line);

        document.add(new Paragraph(" "));

        // =========================
        // SECTION TITLE
        // =========================
        Paragraph section1 = new Paragraph("Payment Summary", sectionFont);
        section1.setSpacingAfter(10);
        document.add(section1);

        // =========================
        // PAYMENT TABLE
        // =========================
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(5);
        table.setSpacingAfter(15);

        addRow(table, "Transaction ID", payment.getTransactionReference(), labelFont, valueFont, border);
        addRow(table, "Course", course.getName(), labelFont, valueFont, border);
        addRow(table, "Amount", payment.getAmount() + " MMK", labelFont, valueFont, border);
        addRow(table, "Payment Method", payment.getPaymentMethod(), labelFont, valueFont, border);
        addRow(table, "Payment Date", String.valueOf(payment.getPaymentDate()), labelFont, valueFont, border);

        // Status (highlighted)
        Font statusFont = new Font(Font.HELVETICA, 11, Font.BOLD, success);
        addRow(table, "Status", payment.getPaymentStatus(), labelFont, statusFont, border);

        document.add(table);

        // =========================
        // FOOTER
        // =========================
        Paragraph footer = new Paragraph(
                "This is an official system-generated receipt. No signature required.",
                new Font(Font.HELVETICA, 10, Font.ITALIC, Color.DARK_GRAY)
        );

        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(20);

        document.add(footer);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }

    // =========================
    // ROW HELPER (STYLED)
    // =========================
    private static void addRow(
            PdfPTable table,
            String label,
            String value,
            Font labelFont,
            Font valueFont,
            Color borderColor) {

        // LEFT (LABEL)
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(Color.WHITE);
        labelCell.setBorderColor(borderColor);
        labelCell.setPadding(10);

        // RIGHT (VALUE)
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBackgroundColor(Color.WHITE);
        valueCell.setBorderColor(borderColor);
        valueCell.setPadding(10);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}