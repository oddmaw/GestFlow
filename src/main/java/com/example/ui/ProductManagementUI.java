package com.example.ui;

import com.example.models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductManagementUI {
    public static BorderPane getUI() {
        BorderPane root = new BorderPane();

        // TableView to display products
        TableView<Product> productTable = new TableView<>();

        TableColumn<Product, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProduitProperty().asObject());

        TableColumn<Product, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nomProperty());

        TableColumn<Product, Double> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());

        TableColumn<Product, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteEnStockProperty().asObject());

        productTable.getColumns().addAll(idColumn, nameColumn, priceColumn, stockColumn);

        // Form for product details
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);

        TextField nameField = new TextField();
        TextField priceField = new TextField();
        TextField stockField = new TextField();

        form.add(new Label("Name:"), 0, 0);
        form.add(nameField, 1, 0);
        form.add(new Label("Price:"), 0, 1);
        form.add(priceField, 1, 1);
        form.add(new Label("Stock:"), 0, 2);
        form.add(stockField, 1, 2);

        // Buttons for actions
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        HBox buttonBox = new HBox(10, addButton, updateButton, deleteButton);

        // Observable list for managing product data
        ObservableList<Product> productList = FXCollections.observableArrayList();
        productTable.setItems(productList);

        // Event handling
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                if (!name.isEmpty()) {
                    productList.add(new Product(name, price, stock));
                    nameField.clear();
                    priceField.clear();
                    stockField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please ensure price and stock are valid numbers.");
            }
        });

        updateButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    selectedProduct.setNom(nameField.getText());
                    selectedProduct.setPrix(Double.parseDouble(priceField.getText()));
                    selectedProduct.setQuantiteEnStock(Integer.parseInt(stockField.getText()));
                    productTable.refresh();
                    nameField.clear();
                    priceField.clear();
                    stockField.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please ensure price and stock are valid numbers.");
                }
            } else {
                showAlert("No Selection", "Please select a product to update.");
            }
        });

        deleteButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                productList.remove(selectedProduct);
            } else {
                showAlert("No Selection", "Please select a product to delete.");
            }
        });

        // Layout setup
        VBox layout = new VBox(10, form, buttonBox);
        root.setCenter(productTable);
        root.setBottom(layout);

        return root;
    }

    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
