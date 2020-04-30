package com.nowak.demo.pdf;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.DeviceGray;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.TabAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.element.TabStop;

import com.nowak.demo.models.invoices.CompanyInvoice;
import com.nowak.demo.models.invoices.PersonalInvoice;
import com.nowak.demo.models.items.Item;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class PDFGenerator {

    private static PdfFont FONT_BOLD;
    private static final String DEST_PREFIX = "./src/main/resources/";
    private static final String DEST_SUFFIX = ".pdf";

    static {
        try {
            FONT_BOLD = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String generatePDFCompanyInvoice(CompanyInvoice invoice, ArrayList<Item> items) throws IOException {
        final String dest = DEST_PREFIX + invoice.getInvoiceNo() + DEST_SUFFIX;
        final String companyAddress = invoice.getCompany().getAddress().showAddress() + "\n";
        final String companyOwner = invoice.getCompany().showOwner();
        final String creatorInfo = invoice.getCreator().getUsername() + "\n " + invoice.getCreator().getEmail();
        Document document = generateDoc(dest);

        addDocHeaders(invoice.getDateOfIssue().toString(), invoice.getInvoiceNo(), document);
        addDocFromToParagraph(creatorInfo,companyAddress,companyOwner,document);
        Table table = showPersonalItems(items);
        document.add(table);

        addDocTotals(String.valueOf(invoice.getAmount()), document);
        document.close();
        return getFileAbsolutePath(DEST_PREFIX + invoice.getInvoiceNo() + DEST_SUFFIX);
    }

    public static String generatePDFPersonalInvoice(PersonalInvoice invoice, ArrayList<Item> items) throws IOException {
        final String dest = DEST_PREFIX + invoice.getInvoiceNo() + DEST_SUFFIX;
        final String customerAddress = "\n" + invoice.getCustomer().getAddress().showAddress() + "\n";
        final String customerInfo = invoice.getCustomer().showCustomer();
        final String creatorInfo = "\n " + invoice.getCreator().getUsername() + "\n " + invoice.getCreator().getEmail();
        final String discountInfo = "\n" + invoice.getDiscount() + " $";
        Document document = generateDoc(dest);

        addDocHeaders(invoice.getDateOfIssue().toString(), invoice.getInvoiceNo(), document);
        addDocFromToParagraph(creatorInfo, customerAddress, customerInfo, document);

        Table table = showPersonalItems(items);
        document.add(table);
        Table discount = new Table(3);
        discount.addCell(getCell("\nTOTAL DISCOUNT ", TextAlignment.CENTER)).setFont(FONT_BOLD);
        discount.addCell(getCell((discountInfo), TextAlignment.RIGHT).setFont(FONT_BOLD));
        document.add(discount);

        addDocTotals(String.valueOf(invoice.getAmount()), document);
        document.close();
        return getFileAbsolutePath(DEST_PREFIX + invoice.getInvoiceNo() + DEST_SUFFIX);
    }

    private static String getFileAbsolutePath(String pathname) {
        File savedFile = new File(pathname);
        return savedFile.getAbsolutePath();
    }

    private static Table showPersonalItems(ArrayList<Item> items) {
        float[] columnWidths = {1, 5, 5, 5, 5, 5};
        Table table = new Table(UnitValue.createPercentArray(columnWidths));

        Cell[] headerFooter = new Cell[]{
                new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("id")),
                new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Description")),
                new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Cost")),
                new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("VAT")),
                new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Quantity")),
                new Cell().setBackgroundColor(new DeviceGray(0.75f)).add(new Paragraph("Category")),
        };

        for (Cell hfCell : headerFooter) {
            table.addHeaderCell(hfCell);
        }

        for (int counter = 0; counter < items.size(); counter++) {
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(counter + 1))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(items.get(counter).getDescription())));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(items.get(counter).getCost()))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(items.get(counter).getVat()))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(items.get(counter).getQuantity()))));
            table.addCell(new Cell().setTextAlignment(TextAlignment.CENTER).add(new Paragraph(String.valueOf(items.get(counter).getCategory()))));
        }
        return table;
    }

    private static Document generateDoc(String dest) throws FileNotFoundException {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        return new Document(pdfDoc, PageSize.A4);
    }

    private static void addDocHeaders(String dateOfIssue, String title, Document doc) {
        Paragraph date = new Paragraph("Date of issue : " + dateOfIssue);
        date.add(new Tab());
        date.addTabStops(new TabStop(1000, TabAlignment.RIGHT));
        doc.add(date);

        Table invoiceTitle = new Table(UnitValue.createPercentArray(new float[]{1})).useAllAvailableWidth();
        invoiceTitle.addCell(getCell(title, TextAlignment.CENTER));
        doc.add(invoiceTitle);
    }

    private static void addDocTotals(String amount, Document doc) {
        Table totals = new Table(3);
        totals.addCell(getCell("\nTOTAL AMOUNT ", TextAlignment.CENTER)).setFont(FONT_BOLD);
        totals.addCell(getCell(("\n" + amount + " $"), TextAlignment.RIGHT).setFont(FONT_BOLD));
        doc.add(totals);
    }

    private static void addDocFromToParagraph(String creatorInfo, String address, String receiver, Document document) {
        Paragraph from = new Paragraph("FROM: \n").setFont(FONT_BOLD);
        from.add(creatorInfo);

        Paragraph to = new Paragraph("TO: \n").setFont(FONT_BOLD);
        to.add(address);
        to.add(receiver);

        document.add(from);
        document.add(to);
    }

    private static Cell getCell(String text, TextAlignment alignment) {
        Cell cell = new Cell().add(new Paragraph(text));
        cell.setPadding(0);
        cell.setTextAlignment(alignment);
        cell.setBorder(Border.NO_BORDER);
        return cell;
    }
}
