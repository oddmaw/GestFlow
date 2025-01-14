package com.example.dao;

import com.example.database.DatabaseConnection;
import com.example.models.Command;
import com.example.models.LineItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CommandDAO {
    private final DatabaseConnection dbConnection;

    public CommandDAO(DatabaseConnection dbConnection) {
        this.dbConnection = dbConnection;
    }
    public Command getCommandById(int id) throws SQLException {
        String query = "SELECT * FROM commandes WHERE idCommande = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return mapCommand(resultSet, connection);
                }
                return null;
            }
        }
    }
    public List<Command> getAllCommands() throws SQLException {
        List<Command> commands = new ArrayList<>();
        String query = "SELECT * FROM commandes";
        try (Connection connection = dbConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                commands.add(mapCommand(resultSet, connection));
            }
        }
        return commands;
    }
    public int addCommand(Command command) throws SQLException {
        String query = "INSERT INTO commandes (idClient) VALUES (?)";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, command.getIdClient());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating command failed, no rows affected.");
            }
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int commandId = generatedKeys.getInt(1);
                    if(command.getLineItems() != null && !command.getLineItems().isEmpty()){
                        for (LineItem lineItem : command.getLineItems()){
                            addLineItem(lineItem, commandId, connection);
                        }
                    }
                    return affectedRows;
                } else {
                    throw new SQLException("Creating command failed, no ID obtained.");
                }
            }
        }
    }
    private int addLineItem(LineItem lineItem, int commandId, Connection connection) throws SQLException {
        String query = "INSERT INTO lignes_commande (idCommande, idProduit, quantite, sousTotal) VALUES (?, ?, ?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, commandId);
            statement.setInt(2, lineItem.getIdProduct());
            statement.setInt(3, lineItem.getQuantity());
            statement.setDouble(4, lineItem.getSousTotal());
            return statement.executeUpdate();
        }
    }
    public int deleteCommand(int id) throws SQLException {
        String deleteLineItemsQuery = "DELETE FROM lignes_commande WHERE idCommande = ?";
        String deleteCommandQuery = "DELETE FROM commandes WHERE idCommande = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement deleteLineItemsStatement = connection.prepareStatement(deleteLineItemsQuery);
             PreparedStatement deleteCommandStatement = connection.prepareStatement(deleteCommandQuery)) {

            deleteLineItemsStatement.setInt(1, id);
            deleteLineItemsStatement.executeUpdate();

            deleteCommandStatement.setInt(1, id);
            return deleteCommandStatement.executeUpdate();
        }
    }
    private Command mapCommand(ResultSet resultSet, Connection connection) throws SQLException {
        Command command = new Command();
        command.setIdCommande(resultSet.getInt("idCommande"));
        command.setDate(resultSet.getObject("date", LocalDateTime.class));
        command.setIdClient(resultSet.getInt("idClient"));
        command.setLineItems(getLineItems(command.getIdCommande(), connection));
        return command;
    }

    private List<LineItem> getLineItems(int commandId, Connection connection) throws SQLException {
        List<LineItem> lineItems = new ArrayList<>();
        String query = "SELECT * FROM lignes_commande WHERE idCommande = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)){
            statement.setInt(1, commandId);
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    lineItems.add(mapLineItem(resultSet));
                }
            }
        }
        return lineItems;
    }
    private LineItem mapLineItem(ResultSet resultSet) throws SQLException {
        LineItem lineItem = new LineItem();
        lineItem.setIdLigne(resultSet.getInt("idLigneCommande"));
        lineItem.setIdProduct(resultSet.getInt("idProduit"));
        lineItem.setQuantity(resultSet.getInt("quantite"));
        lineItem.setSousTotal(resultSet.getDouble("sousTotal"));
        lineItem.setIdCommande(resultSet.getInt("idCommande"));
        return lineItem;
    }
}