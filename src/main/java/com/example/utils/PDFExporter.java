package com.example.utils;

import com.example.models.Invoice;
import com.example.models.Command;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFExporter {


    public static void exportInvoices(List<Invoice> invoices, String filePath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        addTableHeader(table, fontHeader, "ID");
        addTableHeader(table, fontHeader, "Date");
        addTableHeader(table, fontHeader, "Total");
        addTableHeader(table, fontHeader, "Client ID");

        for (Invoice invoice : invoices) {
            addTableCell(table, fontNormal, String.valueOf(invoice.getIdFacture()));
            addTableCell(table, fontNormal, String.valueOf(invoice.getDate()));
            addTableCell(table, fontNormal, String.format("%.2f", invoice.getMontantTotal()));
            addTableCell(table, fontNormal, String.valueOf(invoice.getIdClient()));
        }
        document.add(table);
        document.close();
    }

    public static void exportCommands(List<Command> commands, String filePath) throws IOException, DocumentException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font fontNormal = FontFactory.getFont(FontFactory.HELVETICA, 10);

        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);

        addTableHeader(table, fontHeader, "ID");
        addTableHeader(table, fontHeader, "Date");
        addTableHeader(table, fontHeader, "Client ID");

        for (Command command : commands) {
            addTableCell(table, fontNormal, String.valueOf(command.getIdCommande()));
            addTableCell(table, fontNormal, String.valueOf(command.getDate()));
            addTableCell(table, fontNormal, String.valueOf(command.getIdClient()));

        }
        document.add(table);
        document.close();
    }
    private static void addTableHeader(PdfPTable table, Font font, String text) {
        PdfPCell header = new PdfPCell(new Phrase(text, font));
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(5);
        table.addCell(header);
    }
    private static void addTableCell(PdfPTable table, Font font, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setPadding(5);
        table.addCell(cell);
    }
}