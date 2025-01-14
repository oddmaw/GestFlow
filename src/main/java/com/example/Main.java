package com.example;

import com.example.dao.*;
import com.example.models.*;
import com.example.services.*;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Main extends Application {

    private ClientService clientService;
    private CommandService commandService;
    private InvoiceService invoiceService;
    private ProductService productService;

    private ObservableList<Client> clients = FXCollections.observableArrayList();
    private ObservableList<Command> commands = FXCollections.observableArrayList();
    private ObservableList<Invoice> invoices = FXCollections.observableArrayList();
    private ObservableList<Product> products = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        // Initialize services
        clientService = new ClientService(new ClientDAO());
        commandService = new CommandService(new CommandDAO());
        invoiceService = new InvoiceService(new InvoiceDAO());
        productService = new ProductService(new ProductDAO());

        // UI Elements
        TabPane tabPane = new TabPane();

        Tab clientsTab = new Tab("Clients");
        Tab commandsTab = new Tab("Commands");
        Tab invoicesTab = new Tab("Invoices");
        Tab productsTab = new Tab("Products");

        tabPane.getTabs().addAll(clientsTab, commandsTab, invoicesTab, productsTab);

        // Clients Tab
        VBox clientLayout = new VBox(10);
        ListView<Client> clientListView = new ListView<>(clients);
        Button loadClientsButton = new Button("Load Clients");
        loadClientsButton.setOnAction(event -> loadClients());
        Button addClientButton = new Button("Add Client");
        addClientButton.setOnAction(event -> addClient());
        clientLayout.getChildren().addAll(clientListView, loadClientsButton, addClientButton);
        clientsTab.setContent(clientLayout);

        // Commands Tab
        VBox commandLayout = new VBox(10);
        ListView<Command> commandListView = new ListView<>(commands);
        Button loadCommandsButton = new Button("Load Commands");
        loadCommandsButton.setOnAction(event -> loadCommands());
        Button addCommandButton = new Button("Add Command");
        addCommandButton.setOnAction(event -> addCommand());
        commandLayout.getChildren().addAll(commandListView, loadCommandsButton, addCommandButton);
        commandsTab.setContent(commandLayout);

        // Invoices Tab
        VBox invoiceLayout = new VBox(10);
        ListView<Invoice> invoiceListView = new ListView<>(invoices);
        Button loadInvoicesButton = new Button("Load Invoices");
        loadInvoicesButton.setOnAction(event -> loadInvoices());
        Button addInvoiceButton = new Button("Add Invoice");
        addInvoiceButton.setOnAction(event -> addInvoice());
        invoiceLayout.getChildren().addAll(invoiceListView, loadInvoicesButton, addInvoiceButton);
        invoicesTab.setContent(invoiceLayout);

        // Products Tab
        VBox productLayout = new VBox(10);
        ListView<Product> productListView = new ListView<>(products);
        Button loadProductsButton = new Button("Load Products");
        loadProductsButton.setOnAction(event -> loadProducts());
        Button addProductButton = new Button("Add Product");
        addProductButton.setOnAction(event -> addProduct());
        productLayout.getChildren().addAll(productListView, loadProductsButton, addProductButton);
        productsTab.setContent(productLayout);

        // Scene and Stage
        Scene scene = new Scene(tabPane, 800, 600);
        stage.setTitle("JavaFX CRUD Application");
        stage.setScene(scene);
        stage.show();
    }

    // Load clients from the database
    private void loadClients() {
        try {
            List<Client> clientList = clientService.getAllClients();
            clients.setAll(clientList);
        } catch (SQLException e) {
            showError("Error loading clients", e.getMessage());
        }
    }

    // Add a new client (for simplicity, we are using hardcoded values)
    private void addClient() {
        Client client = new Client("John Doe", "john@example.com", "0606963061");
        try {
            clientService.addClient(client);
            loadClients();
        } catch (SQLException e) {
            showError("Error adding client", e.getMessage());
        }
    }

    // Load commands from the database
    private void loadCommands() {
        try {
            List<Command> commandList = commandService.getAllCommands();
            commands.setAll(commandList);
        } catch (SQLException e) {
            showError("Error loading commands", e.getMessage());
        }
    }

    // Add a new command (for simplicity, we are using hardcoded values)
    private void addCommand() {
        Command command = new Command(LocalDateTime.now(), 1, FXCollections.observableArrayList());
        try {
            commandService.addCommand(command);
            loadCommands();
        } catch (SQLException e) {
            showError("Error adding command", e.getMessage());
        }
    }

    // Load invoices from the database
    private void loadInvoices() {
        try {
            List<Invoice> invoiceList = invoiceService.getAllInvoices();
            invoices.setAll(invoiceList);
        } catch (SQLException e) {
            showError("Error loading invoices", e.getMessage());
        }
    }

    // Add a new invoice (for simplicity, we are using hardcoded values)
    private void addInvoice() {
        Invoice invoice = new Invoice(LocalDateTime.now(), 100.0, 1, List.of());
        try {
            invoiceService.addInvoice(invoice);
            loadInvoices();
        } catch (SQLException e) {
            showError("Error adding invoice", e.getMessage());
        }
    }

    // Load products from the database
    private void loadProducts() {
        try {
            List<Product> productList = productService.getAllProducts();
            products.setAll(productList);
        } catch (SQLException e) {
            showError("Error loading products", e.getMessage());
        }
    }

    // Add a new product (for simplicity, we are using hardcoded values)
    private void addProduct() {
        Product product = new Product("Product 1", 19.99, 10);
        try {
            productService.addProduct(product);
            loadProducts();
        } catch (SQLException e) {
            showError("Error adding product", e.getMessage());
        }
    }

    // Helper function to display error messages
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
