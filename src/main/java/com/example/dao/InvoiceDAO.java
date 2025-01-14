package dao;

import database.DatabaseConnection;
import models.Invoice;
import models.LineItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class InvoiceDAO {
    public InvoiceDAO() {
    }

    public Invoice getInvoiceById(int id) throws SQLException {
        String query = "SELECT f.*, lf.idLigne, lf.idProduit, lf.quantite, lf.sousTotal " +
                "FROM factures f LEFT JOIN lignes_facture lf ON f.idFacture = lf.idFacture " +
                "WHERE f.idFacture = ?";
        Invoice invoice = null;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (invoice == null) {
                        invoice = mapInvoice(resultSet);
                        invoice.setLineItems(new ArrayList<>());
                    }
                    LineItem lineItem = mapLineItem(resultSet);
                    if (lineItem != null && lineItem.getIdProduct() != 0) {
                        invoice.getLineItems().add(lineItem);
                    }
                }
            }
        }
        return invoice;
    }

    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String query = "SELECT f.*, lf.idLigne, lf.idProduit, lf.quantite, lf.sousTotal " +
                "FROM factures f LEFT JOIN lignes_facture lf ON f.idFacture = lf.idFacture";
        Map<Integer, Invoice> invoiceMap = new HashMap<>();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int invoiceId = resultSet.getInt("idFacture");
                Invoice invoice = invoiceMap.get(invoiceId);

                if (invoice == null) {
                    invoice = mapInvoice(resultSet);
                    invoice.setLineItems(new ArrayList<>());
                    invoiceMap.put(invoiceId, invoice);
                }

                LineItem lineItem = mapLineItem(resultSet);
                if (lineItem != null && lineItem.getIdProduct() != 0) {
                    invoice.getLineItems().add(lineItem);
                }
            }
        }
        return new ArrayList<>(invoiceMap.values());
    }

    public int addInvoice(Invoice invoice) throws SQLException {
        String query = "INSERT INTO factures (date, montantTotal, idClient) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, invoice.getDate());
            statement.setDouble(2, invoice.getMontantTotal());
            statement.setInt(3, invoice.getIdClient());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating invoice failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int invoiceId = generatedKeys.getInt(1);
                    if (invoice.getLineItems() != null && !invoice.getLineItems().isEmpty()) {
                        for (LineItem lineItem : invoice.getLineItems()) {
                            addLineItem(lineItem, invoiceId);
                        }
                    }
                    return invoiceId;
                } else {
                    throw new SQLException("Creating invoice failed, no ID obtained.");
                }
            }
        }
    }

    private int addLineItem(LineItem lineItem, int invoiceId) throws SQLException {
        String query = "INSERT INTO lignes_facture (idFacture, idProduit, quantite, sousTotal) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invoiceId);
            statement.setInt(2, lineItem.getIdProduct());
            statement.setInt(3, lineItem.getQuantity());
            statement.setDouble(4, lineItem.getSousTotal());
            return statement.executeUpdate();
        }
    }

    private int deleteLineItemsByInvoiceId(Connection connection, int invoiceId) throws SQLException {
        String query = "DELETE FROM lignes_facture WHERE idFacture = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, invoiceId);
            return statement.executeUpdate();
        }
    }

    public int deleteInvoice(int id) throws SQLException {
        Connection connection = null;
        try {
            connection = DatabaseConnection.getConnection();
            connection.setAutoCommit(false);
            deleteLineItemsByInvoiceId(connection, id);
            String query = "DELETE FROM factures WHERE idFacture = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, id);
                int affectedRows = statement.executeUpdate();
                connection.commit();
                return affectedRows;
            }
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    rollbackException.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException closeException) {
                    closeException.printStackTrace();
                }
            }
        }
    }

    private Invoice mapInvoice(ResultSet resultSet) throws SQLException {
        Invoice invoice = new Invoice();
        invoice.setIdFacture(resultSet.getInt("idFacture"));
        invoice.setDate(resultSet.getObject("date", LocalDateTime.class));
        invoice.setMontantTotal(resultSet.getDouble("montantTotal"));
        invoice.setIdClient(resultSet.getInt("idClient"));
        return invoice;
    }

    private LineItem mapLineItem(ResultSet resultSet) throws SQLException {
        LineItem lineItem = new LineItem();
        int productId = resultSet.getInt("idProduit");
        if (productId != 0) {
            lineItem.setIdLigne(resultSet.getInt("idLigne"));
            lineItem.setIdProduct(productId);
            lineItem.setQuantity(resultSet.getInt("quantite"));
            lineItem.setSousTotal(resultSet.getDouble("sousTotal"));
        } else {
            return null;
        }
        return lineItem;
    }
}