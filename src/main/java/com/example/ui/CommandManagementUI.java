package com.example.ui;

import com.example.models.Client;
import com.example.models.Command;
import com.example.models.LineItem;
import com.example.models.Product;
import com.example.services.ClientService;
import com.example.services.CommandService;
import com.example.services.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class CommandManagementUI {
    private static CommandService commandService;
    private static ClientService clientService;
    private static ProductService productService;


    public static void setCommandService(CommandService service) {
        commandService = service;
    }

    public static void setClientService(ClientService service) {
        clientService = service;
    }

    public static void setProductService(ProductService service) {
        productService = service;
    }


    public static BorderPane getUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // TableView for Commands
        TableView<Command> commandTable = new TableView<>();
        TableColumn<Command, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idCommandeProperty().asObject());

        TableColumn<Command, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        TableColumn<Command, Integer> clientIdColumn = new TableColumn<>("Client ID");
        clientIdColumn.setCellValueFactory(cellData -> cellData.getValue().idClientProperty().asObject());

        commandTable.getColumns().addAll(idColumn, dateColumn, clientIdColumn);
        root.setCenter(commandTable);


        // Form for creating commands
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        ComboBox<Client> clientComboBox = new ComboBox<>();
        clientComboBox.setPromptText("Select a Client");
        loadClients(clientComboBox);

        ListView<Product> productListView = new ListView<>();
        productListView.setPrefHeight(150);
        productListView.setPrefWidth(200);
        loadProducts(productListView);

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");

        Label totalLabel = new Label("Total: 0.0");

        form.add(new Label("Client:"), 0, 0);
        form.add(clientComboBox, 1, 0);
        form.add(new Label("Products:"), 0, 1);
        form.add(productListView, 1, 1);
        form.add(new Label("Quantity:"), 0,2);
        form.add(quantityField,1,2);
        form.add(totalLabel,1,3);


        Button addButton = new Button("Create Command");
        Button deleteButton = new Button("Delete Command");

        HBox buttonBox = new HBox(10, addButton, deleteButton);
        buttonBox.setPadding(new Insets(0, 0, 10, 0));

        VBox formBox = new VBox(10, form, buttonBox);

        root.setBottom(formBox);

        // Observable list for managing command data
        ObservableList<Command> commandList = FXCollections.observableArrayList();
        commandTable.setItems(commandList);
        loadCommands(commandList);

        addButton.setOnAction(e->{
            Client selectedClient = clientComboBox.getValue();
            List<Product> selectedProducts = productListView.getSelectionModel().getSelectedItems();
            String quantityText = quantityField.getText();

            if(selectedClient != null && !selectedProducts.isEmpty() && !quantityText.isEmpty()){
                try{
                    int quantity = Integer.parseInt(quantityText);
                    Command newCommand = new Command();
                    newCommand.setDate(LocalDateTime.now());
                    newCommand.setIdClient(selectedClient.getIdClient());
                    double total = 0.0;
                    ObservableList<LineItem> lineItems = FXCollections.observableArrayList();
                    for (Product product : selectedProducts){
                        LineItem lineItem = new LineItem();
                        lineItem.setQuantity(quantity);
                        lineItem.setIdProduct(product.getIdProduit());
                        lineItem.setSousTotal(product.getPrix()*quantity);
                        total+=lineItem.getSousTotal();
                        lineItems.add(lineItem);
                    }
                    newCommand.setLineItems(lineItems);

                    commandService.addCommand(newCommand);
                    loadCommands(commandList);
                    totalLabel.setText("Total: 0.0");
                }catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please enter a valid quantity");
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while creating command" + ex.getMessage());
                }


            } else {
                showAlert("Missing Information", "Please fill all information");
            }
        });
        deleteButton.setOnAction(e->{
            Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();
            if(selectedCommand != null){
                try {
                    commandService.deleteCommand(selectedCommand.getIdCommande());
                    loadCommands(commandList);
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while deleting command" + ex.getMessage());
                }

            } else {
                showAlert("No Selection", "Please select a command to delete.");
            }
        });

        return root;
    }


    private static void loadCommands(ObservableList<Command> commandList) {
        try {
            commandList.clear();
            commandList.addAll(commandService.getAllCommands());
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Commands" + e.getMessage());
        }
    }

    private static void loadClients(ComboBox<Client> clientComboBox) {
        try {
            ObservableList<Client> clients = FXCollections.observableArrayList(clientService.getAllClients());
            clientComboBox.setItems(clients);
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Clients" + e.getMessage());
        }
    }

    private static void loadProducts(ListView<Product> productListView) {
        try {
            ObservableList<Product> products = FXCollections.observableArrayList(productService.getAllProducts());
            productListView.setItems(products);
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Products" + e.getMessage());
        }
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}