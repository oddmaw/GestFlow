package com.example.ui;

import com.example.models.Client;
import com.example.models.Command;
import com.example.models.LineItem;
import com.example.models.Product;
import com.example.services.ClientService;
import com.example.services.CommandService;
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

import com.itextpdf.text.DocumentException;

public class CommandManagementUI {
    private static CommandService commandService;
    private static ClientService clientService;
    private static ProductService productService;

    public static void setCommandService(CommandService service) {
        CommandManagementUI.commandService = service;
    }

    public static void setClientService(ClientService service) {
        CommandManagementUI.clientService = service;
    }

    public static void setProductService(ProductService service) {
        CommandManagementUI.productService = service;
    }

    public static BorderPane getUI(Tab tab) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        TableView<Command> commandTable = new TableView<>();
        TableColumn<Command, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("idCommande"));

        TableColumn<Command, LocalDateTime> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Command, String> clientNameColumn = new TableColumn<>("Client");
        clientNameColumn.setCellValueFactory(cellData -> {
            try {
                Client client = clientService.getClientById(cellData.getValue().getIdClient());
                return Bindings.concat(client.getNom());
            } catch (SQLException e) {
                return Bindings.concat("Error");
            }
        });

        commandTable.getColumns().addAll(idColumn, dateColumn, clientNameColumn);
        root.setCenter(commandTable);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        Label searchLabel = new Label("Search:");
        ComboBox<String> searchTypeComboBox = new ComboBox<>(FXCollections.observableArrayList("Client", "Product", "Date"));
        searchTypeComboBox.setValue("Client"); // Default search type
        TextField searchField = new TextField();
        searchField.setPromptText("Enter search term");
        searchBox.getChildren().addAll(searchLabel, searchTypeComboBox, searchField);
        root.setTop(searchBox);
        BorderPane.setAlignment(searchBox, Pos.TOP_LEFT);
        BorderPane.setMargin(searchBox, new Insets(0, 0, 10, 0));

        // Form for Creating Commands
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
        productNameColumn.setPrefWidth(150);

        TableColumn<ProductWrapper, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        final Label totalLabel = new Label("Total: 0.00"); // Initialize with 0.00
        quantityColumn.setCellFactory(tc -> {
            TextFieldTableCell<ProductWrapper, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter());
            cell.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (!newVal && cell.isEditing()) {
                    try{
                        cell.commitEdit(Integer.parseInt(cell.getText()));
                        updateTotalLabel(productTableView.getItems(), totalLabel);
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
            updateTotalLabel(productTableView.getItems(), totalLabel);
        });
        quantityColumn.setPrefWidth(120);

        TableColumn<ProductWrapper, Double> subtotalColumn = new TableColumn<>("Subtotal");
        subtotalColumn.setCellValueFactory(cellData -> cellData.getValue().subtotalProperty().asObject());
        subtotalColumn.setPrefWidth(120);
        subtotalColumn.setEditable(false);

        productTableView.getColumns().addAll(productNameColumn, quantityColumn, subtotalColumn);
        productTableView.setItems(FXCollections.observableArrayList());
        loadProductsForSelection(productTableView);
        productTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        HBox totalBox = new HBox(10, totalLabel);
        totalBox.setAlignment(Pos.BOTTOM_LEFT);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.BOTTOM_LEFT);
        Button addButton = new Button("Create Command");
        Button deleteButton = new Button("Delete Command");
        Button clearButton = new Button("Clear");
        Button exportCsvButton = new Button("Export CSV");
        Button exportPdfButton = new Button("Export PDF");
        buttonBox.getChildren().addAll(addButton, deleteButton, clearButton, exportCsvButton, exportPdfButton);

        leftPanel.getChildren().addAll(new VBox(10, clientLabel, clientComboBox),
                new VBox(10, productsLabel, productTableView),
                totalBox,
                buttonBox);
        root.setLeft(leftPanel);
        BorderPane.setMargin(leftPanel, new Insets(0, 10, 0, 0));

        final ObservableList<Command> commandList = FXCollections.observableArrayList();
        commandTable.setItems(commandList);
        loadCommands(commandList);

        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            searchCommands(newVal, commandList,searchTypeComboBox.getValue());
            if (newVal == null || newVal.isEmpty()){
                loadCommands(commandList);
            }
        });

        // Refresh data when tab is selected
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
            updateTotalLabel(productTableView.getItems(), totalLabel);
        });
        addButton.setOnAction(e->{
            Client selectedClient = clientComboBox.getValue();
            ObservableList<ProductWrapper> selectedProductWrappers = productTableView.getItems().filtered(pw -> pw.getQuantity() > 0);
            if(selectedClient != null && !selectedProductWrappers.isEmpty()){

                Command newCommand = new Command();
                newCommand.setDate(LocalDateTime.now());
                newCommand.setIdClient(selectedClient.getIdClient());
                double totalCalc = 0.0;
                List<LineItem> lineItems = new ArrayList<>();
                for (ProductWrapper wrapper : selectedProductWrappers){
                    LineItem lineItem = new LineItem();
                    lineItem.setQuantity(wrapper.getQuantity());
                    lineItem.setIdProduct(wrapper.getProduct().getIdProduit());
                    lineItem.setSousTotal(wrapper.getSubtotal());
                    totalCalc += lineItem.getSousTotal();
                    lineItems.add(lineItem);
                }
                newCommand.setLineItems(lineItems);
                try{
                    commandService.addCommand(newCommand);
                    loadCommands(commandList);
                    clearCommandForm(clientComboBox, productTableView, totalLabel);
                    showAlert("Success", "Command created successfully");
                } catch (SQLException ex) {
                    showAlert("SQL Error", "Error while creating command" + ex.getMessage());
                }
            }  else {
                showAlert("Missing Information", "Please select a client and at least one product with a quantity");
            }
        });

        deleteButton.setOnAction(e->{
            Command selectedCommand = commandTable.getSelectionModel().getSelectedItem();
            if(selectedCommand != null){
                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                confirmation.setTitle("Confirmation");
                confirmation.setHeaderText("Delete Command");
                confirmation.setContentText("Are you sure you want to delete this command?");

                Optional<ButtonType> result = confirmation.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK){
                    try {
                        commandService.deleteCommand(selectedCommand.getIdCommande());
                        loadCommands(commandList);
                        showAlert("Success", "Command deleted successfully.");
                    } catch (SQLException ex) {
                        showAlert("SQL Error", "Error while deleting command" + ex.getMessage());
                    }
                }
            }else{
                showAlert("No Selection", "Please select a command to delete.");
            }
        });
        clearButton.setOnAction(e-> clearCommandForm(clientComboBox, productTableView, totalLabel));
        exportCsvButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Commands CSV");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(root.getScene().getWindow());
            if (file != null) {
                try {
                    CSVExporter.exportCommands(commandList, file.getAbsolutePath());
                    showAlert("Success", "Commands exported to CSV.");
                } catch (IOException ex) {
                    showAlert("IO Error", "Error exporting commands: " + ex.getMessage());
                }
            }
        });
        exportPdfButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Commands PDF");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            File file = fileChooser.showSaveDialog(root.getScene().getWindow());
            if(file != null){
                try{
                    PDFExporter.exportCommands(commandList, file.getAbsolutePath());
                    showAlert("Success", "Commands exported to PDF.");
                } catch(IOException ex){
                    showAlert("IO Error", "Error exporting commands: " + ex.getMessage());
                } catch (DocumentException ex){
                    showAlert("PDF Error", "Error exporting commands: " + ex.getMessage());
                }
            }
        });
        return root;
    }
    private static void clearCommandForm(ComboBox<Client> clientComboBox, TableView<ProductWrapper> productTableView, Label totalLabel) {
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

    private static void searchCommands(String text, ObservableList<Command> commandList, String searchType) {
        try {
            commandList.clear();
            List<Command> commands = commandService.getAllCommands();
            for(Command command : commands){
                boolean match = false;
                if (searchType.equals("Client")) {
                    try {
                        Client client = clientService.getClientById(command.getIdClient());
                        if (client != null && client.getNom().toLowerCase().contains(text.toLowerCase())) {
                            match = true;
                        }
                    } catch (SQLException ex) {
                        System.err.println("Error while getting client: " + ex.getMessage());
                    }
                } else if (searchType.equals("Product")) {
                    for(LineItem item : command.getLineItems()){
                        try{
                            Product product = productService.getProductById(item.getIdProduct());
                            if (product != null && product.getNom().toLowerCase().contains(text.toLowerCase())) {
                                match = true;
                                break;
                            }
                        } catch (SQLException ex) {
                            System.err.println("Error while getting product: " + ex.getMessage());
                        }
                    }
                } else if (searchType.equals("Date")) {
                    if (command.getDate().toLocalDate().toString().contains(text)) {
                        match = true;
                    }
                }
                if (match) {
                    commandList.add(command);
                }
            }
        } catch (SQLException ex) {
            showAlert("SQL Error", "Error loading commands: " + ex.getMessage());
        }
    }
    private static void loadCommands(ObservableList<Command> commandList) {
        try {
            commandList.clear();
            commandList.addAll(commandService.getAllCommands());
        } catch (SQLException e) {
            showAlert("SQL Error", "Error Loading Commands: " + e.getMessage());
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