package com.example.ui;

import com.example.models.Client;
import com.example.models.Invoice;
import com.example.models.LineItem;
import com.example.models.Product;
import com.example.services.ClientService;
import com.example.services.InvoiceService;
import com.example.services.ProductService;
import com.example.utils.CSVExporter;
import com.example.utils.PDFExporter;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.StringConverter;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.itextpdf.text.DocumentException; // Added import
public class InvoiceManagementUI {
    private static InvoiceService invoiceService;
    private static ClientService clientService;
    private static ProductService productService;

    public static void setInvoiceService(InvoiceService service) {
        invoiceService = service;
    }

    public static void setClientService(ClientService service) {
        clientService = service;
    }

    public static void setProductService(ProductService service) {
        productService = service;
    }

    public static BorderPane getUI(Tab tab) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));


        TableView<Invoice> invoiceTable = new TableView<>();

        TableColumn<Invoice, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idFacture"));
        TableColumn<Invoice, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        TableColumn<Invoice, Double> totalColumn = new TableColumn<>("Total");
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("montantTotal"));
        TableColumn<Invoice, String> clientNameColumn = new TableColumn<>("Client");
        clientNameColumn.setCellValueFactory(cellData -> {
            try {
                Client client = clientService.getClientById(cellData.getValue().getIdClient());
                return Bindings.concat(client.getNom());
            } catch (SQLException e) {
                return Bindings.concat("Error");
            }
        });
        invoiceTable.getColumns().addAll(idColumn, dateColumn, totalColumn, clientNameColumn);
        root.setCenter(invoiceTable);


        HBox searchBox = new HBox(10);

        searchBox.setAlignment(Pos.CENTER_LEFT);
        Label searchLabel = new Label("Search:");
        ComboBox<String> searchTypeComboBox = new ComboBox<>(FXCollections.observableArrayList("Client", "Date"));
        searchTypeComboBox.setValue("Client"); // Default search type
        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term");
        searchBox.getChildren().addAll(searchLabel, searchTypeComboBox, searchField);
        root.setTop(searchBox);
        BorderPane.setAlignment(searchBox, Pos.TOP_LEFT);
        BorderPane.setMargin(searchBox, new Insets(0, 0, 10, 0));


        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(10));

        Label clientLabel = new Label("Client:");
        ComboBox<Client> clientComboBox = new ComboBox<>();
        clientComboBox.setPromptText("Select a Client");
        clientComboBox.setCellFactory(lv -> new ListCell<Client>() {
            @Override
            protected void updateItem(Client client, boolean empty) {
                super.updateItem(client, empty);
                setText(empty ? null : client.getNom());
            }
        });
        clientComboBox.setConverter(new StringConverterClient());
        loadClients(clientComboBox);

        Label productsLabel = new Label("Products:");
        TableView<ProductWrapper> productTableView = new TableView<>();
        productTableView.setPrefHeight(450);
        productTableView.setPrefWidth(400);
        productTableView.setEditable(true);

        TableColumn<ProductWrapper, String> productNameColumn = new TableColumn<>("Product");
        productNameColumn.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().getProduct().getNom()));
        productNameColumn.setMinWidth(150);


        TableColumn<ProductWrapper, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        quantityColumn.setPrefWidth(120);
        final Label finalTotalLabel = new Label("Total: 0.00");
        quantityColumn.setCellFactory(tc -> {
            TextFieldTableCell<ProductWrapper, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
            cell.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal && cell.isEditing()) {
                    try{
                        cell.commitEdit(Integer.parseInt(cell.getText()));
                        updateTotalLabel(productTableView.getItems(), finalTotalLabel);
                    } catch (NumberFormatException ex){
                        cell.cancelEdit();
                    }
                }
            });
            return cell;
        });
        quantityColumn.setOnEditCommit(event -> {
            ProductWrapper wrapper = event.getTableView().getItems().get(event.getTablePosition().getRow());
            wrapper.setQuantity(event.getNewValue());
            updateTotalLabel(productTableView.getItems(), finalTotalLabel);
        });
        quantityColumn.setMinWidth(80);

        TableColumn<ProductWrapper, Double> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty().asObject());
        subtotalColumn.setMinWidth(120);
        subtotalColumn.setEditable(false);

        productTableView.getColumns().addAll(productNameColumn, quantityColumn, subtotalColumn);
        productTableView.setItems(FXCollections.observableArrayList());
        loadProductsForSelection(productTableView);
        productTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        HBox totalBox = new HBox(10, finalTotalLabel);
        totalBox.setAlignment(Pos.BOTTOM_LEFT);

        // Discount
        HBox discountBox = new HBox(10);
        discountBox.setAlignment(Pos.CENTER_LEFT);
        Label discountLabel = new Label("Discount:");
        TextField discountValueField = new TextField();
        discountValueField.setPromptText("Discount Value");
        discountBox.getChildren().addAll(discountLabel,  discountValueField);


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        Button addButton = new Button("Create Invoice");
        Button deleteButton = new Button("Delete Invoice");
        Button clearButton = new Button("Clear");
        Button exportCsvButton = new Button("Export CSV");
        Button exportPdfButton = new Button("Export PDF");
        buttonBox.getChildren().addAll(addButton, deleteButton, clearButton, exportCsvButton, exportPdfButton);


        leftPanel.getChildren().addAll(new VBox(10, clientLabel, clientComboBox),
                new VBox(10, productsLabel, productTableView),
                discountBox,
                totalBox,
                buttonBox);
        root.setLeft(leftPanel);
        BorderPane.setMargin(leftPanel, new Insets(0, 10, 0, 0));

        final ObservableList<Invoice> invoiceList = FXCollections.observableArrayList();
        invoiceTable.setItems(invoiceList);
        loadInvoices(invoiceList);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            searchInvoices(newVal, invoiceList, searchTypeComboBox.getValue());
            if (newVal == null || newVal.isEmpty()){
                loadInvoices(invoiceList);
            }
        });

        tab.setOnSelectionChanged(event ->{
            if(tab.isSelected()){
                loadClients(clientComboBox);
                loadProductsForSelection(productTableView);
            }
        });
        productTableView.getItems().addListener((javafx.collections.ListChangeListener.Change<? extends ProductWrapper> c) -> {
            while (c.next()) {
                if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        productTableView.getItems().get(i).updateSubtotal();
                    }
                }
            }
            updateTotalLabel(productTableView.getItems(), finalTotalLabel);
        });
        addButton.setOnAction(e -> {
            Client selectedClient = clientComboBox.getValue();
            ObservableList<ProductWrapper> selectedProductWrappers = productTableView.getItems().filtered(pw -> pw.getQuantity() > 0);
            String discountValue = discountValueField.getText();

            if (selectedClient != null && !selectedProductWrappers.isEmpty()) {
                Invoice newInvoice = new Invoice();
                newInvoice.setDate(LocalDateTime.now());
                newInvoice.setIdClient(selectedClient.getIdClient());
                double total = 0.0;
                List<LineItem> lineItems = new ArrayList<>();
                for (ProductWrapper wrapper : selectedProductWrappers) {
                    LineItem lineItem = new LineItem();
                    lineItem.setQuantity(wrapper.getQuantity());
                    lineItem.setIdProduct(wrapper.getProduct().getIdProduit());
                    lineItem.setSousTotal(wrapper.getSubtotal());
                    total+=lineItem.getSousTotal();
                    lineItems.add(lineItem);
                }

                if(!discountValue.isEmpty()){
                    try{
                        double discount = Double.parseDouble(discountValue);
                        total = total *(1-(discount/100));
                        newInvoice.setMontantTotal(total);
                        newInvoice.setTotalDiscount(discount);
                    } catch(NumberFormatException ex){
                        showAlert("Invalid Input", "Please use a valid number for discount.");
                        return;
                    }
                } else {
                    newInvoice.setTotalDiscount(0);
                }
                newInvoice.setMontantTotal(total);
                newInvoice.setLineItems(lineItems);
                try {
                    int totalQuantity = 0;
                    for (LineItem item : lineItems) {
                        totalQuantity += item.getQuantity();
                    }
                    if (totalQuantity >= 10) {
                        newInvoice.setMontantTotal(newInvoice.getMontantTotal() - newInvoice.getMontantTotal() * 0.3);
                    }
                    invoiceService.addInvoice(newInvoice);
                    loadInvoices(invoiceList);
                    clearInvoiceForm(clientComboBox, productTableView, finalTotalLabel);
                    if (totalQuantity >= 10) {
                        showAlert("Success", "Invoice created successfully (Discount 30% on the total cost).");
                    } else {
                        showAlert("Success", "Invoice created successfully.");
                    }
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while creating invoice: " + ex.getMessage());
                }
            } else {
                showAlert("Missing Information", "Please select a client and at least one product with a quantity.");
            }
        });

        deleteButton.setOnAction(e -> {
            Invoice selectedInvoice = invoiceTable.getSelectionModel().getSelectedItem();
            if (selectedInvoice != null) {
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation");
                confirmation.setHeaderText("Delete Invoice");
                confirmation.setContentText("Are you sure you want to delete this invoice?");
                Optional<ButtonType> result = confirmation.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    try {
                        invoiceService.deleteInvoice(selectedInvoice.getIdFacture());
                        loadInvoices(invoiceList);
                        showAlert("Success", "Invoice deleted successfully.");
                    } catch (SQLException ex) {
                        showAlert("SQL Error", "Error while deleting invoice: " + ex.getMessage());
                    }
                }
            } else {
                showAlert("No Selection", "Please select an invoice to delete.");
            }
        });
        clearButton.setOnAction(e -> clearInvoiceForm(clientComboBox, productTableView, finalTotalLabel));
        exportCsvButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Invoices CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(root.getScene().getWindow());
            if (file != null) {
                try {
                    CSVExporter.exportInvoices(invoiceList, file.getAbsolutePath());
                    showAlert("Success", "Invoices exported to CSV.");
                } catch (IOException ex) {
                    showAlert("IO Error", "Error exporting invoices: " + ex.getMessage());
                }
            }
        });
        exportPdfButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Invoices PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(root.getScene().getWindow());
            if(file != null){
                try{
                    PDFExporter.exportInvoices(invoiceList, file.getAbsolutePath());
                    showAlert("Success", "Invoices exported to PDF.");
                } catch(IOException ex){
                    showAlert("IO Error", "Error exporting invoices: " + ex.getMessage());
                } catch (DocumentException ex){
                    showAlert("PDF Error", "Error exporting invoices: " + ex.getMessage());
                }
            }
        });
        return root;
    }
    private static void clearInvoiceForm(ComboBox<Client> clientComboBox, TableView<ProductWrapper> productTableView, Label totalLabel) {
        clientComboBox.setValue(null);
        productTableView.getItems().forEach(pw -> pw.setQuantity(0));
        updateTotalLabel(productTableView.getItems(), totalLabel);
    }


    private static void updateTotalLabel(ObservableList<ProductWrapper> products, Label totalLabel) {
        double total = products.stream()
                .mapToDouble(ProductWrapper::getSubtotal)
                .sum();
        totalLabel.setText(String.format("Total: %.2f", total));
    }

    private static void searchInvoices(String text, ObservableList<Invoice> invoiceList, String searchType) {
        try {
            invoiceList.clear();
            List<Invoice> invoices = invoiceService.getAllInvoices();
            for (Invoice invoice : invoices) {
                boolean match = false;
                if (searchType.equals("Client")) {
                    try{
                        Client client = clientService.getClientById(invoice.getIdClient());
                        if (client != null && client.getNom().toLowerCase().contains(text.toLowerCase())) {
                            match = true;
                        }
                    } catch(SQLException ex){
                        System.err.println("Error while getting client: " + ex.getMessage());
                    }
                } else if (searchType.equals("Date")) {
                    if (invoice.getDate().toLocalDate().toString().contains(text)) {
                        match = true;
                    }
                }
                if (match) {
                    invoiceList.add(invoice);
                }
            }
        } catch (SQLException ex) {
            showAlert("SQL Error", "Error loading invoices: " + ex.getMessage());
        }
    }
    private static void loadInvoices(ObservableList<Invoice> invoiceList) {
        try {
            invoiceList.clear();
            invoiceList.addAll(invoiceService.getAllInvoices());
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Invoices: " + e.getMessage());
        }
    }
    private static void loadClients(ComboBox<Client> clientComboBox) {
        try {
            ObservableList<Client> clients = clientService.getAllClients().stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
            clientComboBox.setItems(clients);
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Clients: " + e.getMessage());
        }
    }
    private static void loadProductsForSelection(TableView<ProductWrapper> productTableView) {
        try {
            ObservableList<Product> products = productService.getAllProducts().stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
            ObservableList<ProductWrapper> productWrappers = products.stream()
                    .map(ProductWrapper::new)
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            productTableView.setItems(productWrappers);
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Products: " + e.getMessage());
        }
    }

    public static class ProductWrapper {
        private final Product product;
        private final IntegerProperty quantity = new SimpleIntegerProperty(0);
        private final SimpleDoubleProperty subtotal = new SimpleDoubleProperty(0.0);

        public ProductWrapper(Product product) {
            this.product = product;
            this.quantity.addListener((obs, oldVal, newVal) -> updateSubtotal());
            updateSubtotal();
        }
        private void updateSubtotal() {
            this.subtotal.set(product.getPrix() * quantity.get());
        }

        public Product getProduct() {
            return product;
        }

        public int getQuantity() {
            return quantity.get();
        }

        public IntegerProperty quantityProperty() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity.set(quantity);
            updateSubtotal();
        }

        public double getSubtotal() {
            return subtotal.get();
        }

        public SimpleDoubleProperty subtotalProperty() {
            return subtotal;
        }
    }


    private static class StringConverterClient extends StringConverter<Client> {
        @Override
        public String toString(Client client) {
            if (client == null) {
                return null;
            }
            return client.getNom();
        }

        @Override
        public Client fromString(String s) {
            return null;
        }
    }


    private static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
