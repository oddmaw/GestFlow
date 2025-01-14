package com.example.utils;

import com.example.models.Invoice;
import com.example.models.Command;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;


public class CSVExporter {

    public static void exportInvoices(List<Invoice> invoices, String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            writer.write("ID,Date,Total,Client ID\n");

            for (Invoice invoice : invoices) {
                writer.write(String.format("%d,%s,%.2f,%d\n",
                        invoice.getIdFacture(),
                        invoice.getDate(),
                        invoice.getMontantTotal(),
                        invoice.getIdClient()));
            }
        }
    }
    public static void exportCommands(List<Command> commands, String filePath) throws IOException {
        try (Writer writer = new FileWriter(filePath)) {
            writer.write("ID,Date,Client ID\n");

            for (Command command : commands) {
                writer.write(String.format("%d,%s,%d\n",
                        command.getIdCommande(),
                        command.getDate(),
                        command.getIdClient()));
            }
        }
    }

}