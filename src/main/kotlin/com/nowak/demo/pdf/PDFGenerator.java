package com.nowak.demo.pdf;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.nowak.demo.models.invoices.CompanyInvoice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFGenerator {

    public static void generatePDFInvoice(CompanyInvoice companyInvoice) throws DocumentException {
        //TODO
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(new File("test")));

            document.open();

            Paragraph p = new Paragraph();
            p.add("Test");
            p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);

            document.close();
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }

}
