package com.example.ui;

import com.example.models.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ClientManagementUI {
    public static BorderPane getUI() {
        BorderPane root = new BorderPane();

        TableView<Client> clientTable = new TableView<>();
        TableColumn<Client, Integer> idColumn = new TableColumn<>("ID");
        TableColumn<Client, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Client, String> emailColumn = new TableColumn<>("Email");
        TableColumn<Client, String> phoneColumn = new TableColumn<>("Phone");

        clientTable.getColumns().addAll(idColumn, nameColumn, emailColumn, phoneColumn);

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

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

        ObservableList<Client> clientList = FXCollections.observableArrayList();
        clientTable.setItems(clientList);

        addButton.setOnAction(e -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            if (!name.isEmpty() && !email.isEmpty() && !phone.isEmpty()) {
                clientList.add(new Client(name, email, phone));
                nameField.clear();
                emailField.clear();
                phoneField.clear();
            }
        });

        deleteButton.setOnAction(e -> {
            Client selectedClient = clientTable.getSelectionModel().getSelectedItem();
            if (selectedClient != null) {
                clientList.remove(selectedClient);
            }
        });

        VBox layout = new VBox(10, form, buttonBox);
        root.setCenter(clientTable);
        root.setBottom(layout);

        return root;
    }
}
