package com.finalproject.Final.service;

import java.awt.Color;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

public class CertificateBorder extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();

        // Colors Matching HTML Preview
        Color darkBlue = new Color(13, 71, 161);   // #0d47a1
        Color lightBlue = new Color(25, 118, 210);  // #1976d2

        float width = document.getPageSize().getWidth();
        float height = document.getPageSize().getHeight();

        cb.saveState();

        // 1. Outer Heavy Border (Dark Blue)
        cb.setColorStroke(darkBlue);
        cb.setLineWidth(10f);
        cb.rectangle(20, 20, width - 40, height - 40);
        cb.stroke();

        // 2. Inner Thin Border (Light Blue)
        cb.setColorStroke(lightBlue);
        cb.setLineWidth(2f);
        cb.rectangle(35, 35, width - 70, height - 70);
        cb.stroke();

        cb.restoreState();
    }
}