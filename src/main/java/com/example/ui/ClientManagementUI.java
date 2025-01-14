package com.example.ui;

import com.example.models.Client;
import com.example.services.ClientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;

public class ClientManagementUI {
    private static ClientService clientService;

    public static void setClientService(ClientService service) {
        ClientManagementUI.clientService = service;
    }

    public static BorderPane getUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TableView<Client> clientTable = new TableView<>();
        TableColumn<Client, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idClientProperty().asObject());

        TableColumn<Client, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());

        TableColumn<Client, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        TableColumn<Client, String> phoneColumn = new TableColumn<>("Phone");
        phoneColumn.setCellValueFactory(cellData -> cellData.getValue().telephoneProperty());

        clientTable.getColumns().addAll(idColumn, nameColumn, emailColumn, phoneColumn);
        root.setCenter(clientTable);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        TextField nameField = new TextField();
        TextField emailField = new TextField();
        TextField phoneField = new TextField();


        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Email:"), 0, 1);
        form.add(emailField, 1, 1);
        form.add(new Label("Phone:"), 0, 2);
        form.add(phoneField, 1, 2);

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");


        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);
        buttonBox.setPadding(new Insets(0, 0, 10, 0));

        VBox formBox = new VBox(10, form, buttonBox);
        root.setBottom(formBox);

        ObservableList<Client> clientList = FXCollections.observableArrayList();
        clientTable.setItems(clientList);
        loadClients(clientList);


        clientTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameField.setText(newSelection.getNom());
                emailField.setText(newSelection.getEmail());
                phoneField.setText(newSelection.getTelephone());
            }
        });

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                Client newClient = new Client(name, email, phone);
                try {
                    clientService.addClient(newClient);
                    loadClients(clientList);
                    nameField.clear();
                    emailField.clear();
                    phoneField.clear();
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while adding client" + ex.getMessage());
                }
            }
        });
        updateButton.setOnAction(e -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                selectedClient.setNom(nameField.getText());
                selectedClient.setEmail(emailField.getText());
                selectedClient.setTelephone(phoneField.getText());
                try {
                    clientService.updateClient(selectedClient);
                    loadClients(clientList);
                    clientTable.refresh();
                    nameField.clear();
                    emailField.clear();
                    phoneField.clear();
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while updating client" + ex.getMessage());
                }
            } else {
                showAlert("No Selection", "Please select a client to update.");
            }
        });
        deleteButton.setOnAction(e -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                try {
                    clientService.deleteClient(selectedClient.getIdClient());
                    loadClients(clientList);
                    nameField.clear();
                    emailField.clear();
                    phoneField.clear();
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while deleting client" + ex.getMessage());
                }
            } else {
                showAlert("No Selection", "Please select a client to delete.");
            }
            clientTable.getSelectionModel().clearSelection();
        });
        return root;
    }

    private static void loadClients(ObservableList<Client> clientList) {
        try {
            clientList.clear();
            clientList.addAll(clientService.getAllClients());
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Clients" + e.getMessage());
        }
    }


    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}