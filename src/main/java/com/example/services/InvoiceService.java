package com.example.services;

import com.example.dao.InvoiceDAO;
import com.example.models.Invoice;

import java.sql.SQLException;
import java.util.List;

public class InvoiceService {
    private InvoiceDAO invoiceDAO;

    public InvoiceService(InvoiceDAO invoiceDAO) {
        this.invoiceDAO = invoiceDAO;
    }
    public Invoice getInvoiceById(int id) throws SQLException {
        return invoiceDAO.getInvoiceById(id);
    }
    public List<Invoice> getAllInvoices() throws SQLException {
        return invoiceDAO.getAllInvoices();
    }
    public int addInvoice(Invoice invoice) throws SQLException {
        return invoiceDAO.addInvoice(invoice);
    }
    public int deleteInvoice(int id) throws SQLException {
        return invoiceDAO.deleteInvoice(id);
    }
}
