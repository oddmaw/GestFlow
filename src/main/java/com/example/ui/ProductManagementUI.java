package com.example.ui;

import com.example.models.Product;
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

public class ProductManagementUI {
    private static ProductService productService;

    public static void setProductService(ProductService service) {
        productService = service;
    }

    public static BorderPane getUI() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10)); // Add padding around the content

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
        root.setCenter(productTable);

        // Form for product details
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10)); // Add padding to the form

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
        buttonBox.setPadding(new Insets(0, 0, 10, 0)); // Add padding to button box

        VBox formBox = new VBox(10, form, buttonBox); // Wrap form and buttons in a VBox

        root.setBottom(formBox);


        // Observable list for managing product data
        ObservableList<Product> productList = FXCollections.observableArrayList();
        productTable.setItems(productList);
        loadProducts(productList);

        // Event handling
        addButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());
                if (!name.isEmpty()) {
                    Product newProduct = new Product(name, price, stock);
                    productService.addProduct(newProduct);
                    loadProducts(productList);
                    nameField.clear();
                    priceField.clear();
                    stockField.clear();
                }
            } catch (NumberFormatException ex) {
                showAlert("Invalid Input", "Please ensure price and stock are valid numbers.");
            } catch (SQLException ex) {
                showAlert("SQL Error", "Error while adding Product" + ex.getMessage());
            }
        });

        updateButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    selectedProduct.setNom(nameField.getText());
                    selectedProduct.setPrix(Double.parseDouble(priceField.getText()));
                    selectedProduct.setQuantiteEnStock(Integer.parseInt(stockField.getText()));
                    productService.updateProduct(selectedProduct);
                    loadProducts(productList);
                    nameField.clear();
                    priceField.clear();
                    stockField.clear();
                } catch (NumberFormatException ex) {
                    showAlert("Invalid Input", "Please ensure price and stock are valid numbers.");
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while updating product" + ex.getMessage());
                }
            } else {
                showAlert("No Selection", "Please select a product to update.");
            }
        });

        deleteButton.setOnAction(e -> {
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
            if (selectedProduct != null) {
                try {
                    productService.deleteProduct(selectedProduct.getIdProduit());
                    loadProducts(productList);
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while deleting product" + ex.getMessage());
                }
            } else {
                showAlert("No Selection", "Please select a product to delete.");
            }
        });
        return root;
    }

    private static void loadProducts(ObservableList<Product> productList) {
        try {
            productList.clear();
            productList.addAll(productService.getAllProducts());
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