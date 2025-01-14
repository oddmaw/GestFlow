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

        TextField clientNameField = new TextField();
        clientNameField.setPromptText("Client Name");
        TextField clientEmailField = new TextField();
        clientEmailField.setPromptText("Client Email");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Client Phone");

        Button addClientButton = new Button("Add Client");
        addClientButton.setOnAction(event -> addClient(clientNameField.getText(), clientEmailField.getText(), phoneField.getText()));

        // CRUD buttons for Clients
        HBox clientCRUDButtons = new HBox(10);
        Button updateClientButton = new Button("Update Client");
        updateClientButton.setOnAction(event -> updateClient(clientListView.getSelectionModel().getSelectedItem()));

        Button deleteClientButton = new Button("Delete Client");
        deleteClientButton.setOnAction(event -> deleteClient(clientListView.getSelectionModel().getSelectedItem()));

        clientCRUDButtons.getChildren().addAll(updateClientButton, deleteClientButton);

        clientLayout.getChildren().addAll(clientListView, clientNameField, clientEmailField, phoneField, addClientButton, clientCRUDButtons);
        clientsTab.setContent(clientLayout);

        // Commands Tab
        VBox commandLayout = new VBox(10);
        ListView<Command> commandListView = new ListView<>(commands);

        TextField commandClientIdField = new TextField();
        commandClientIdField.setPromptText("Client ID");

        Button addCommandButton = new Button("Add Command");
        addCommandButton.setOnAction(event -> addCommand(commandClientIdField.getText()));

        HBox commandCRUDButtons = new HBox(10);
        Button updateCommandButton = new Button("Update Command");
        updateCommandButton.setOnAction(event -> updateCommand(commandListView.getSelectionModel().getSelectedItem()));

        Button deleteCommandButton = new Button("Delete Command");
        deleteCommandButton.setOnAction(event -> deleteCommand(commandListView.getSelectionModel().getSelectedItem()));

        commandCRUDButtons.getChildren().addAll(updateCommandButton, deleteCommandButton);

        commandLayout.getChildren().addAll(commandListView, commandClientIdField, addCommandButton, commandCRUDButtons);
        commandsTab.setContent(commandLayout);

        // Invoices Tab
        VBox invoiceLayout = new VBox(10);
        ListView<Invoice> invoiceListView = new ListView<>(invoices);

        TextField invoiceAmountField = new TextField();
        invoiceAmountField.setPromptText("Invoice Amount");

        Button addInvoiceButton = new Button("Add Invoice");
        addInvoiceButton.setOnAction(event -> addInvoice(invoiceAmountField.getText()));

        HBox invoiceCRUDButtons = new HBox(10);
        Button updateInvoiceButton = new Button("Update Invoice");
        updateInvoiceButton.setOnAction(event -> updateInvoice(invoiceListView.getSelectionModel().getSelectedItem()));

        Button deleteInvoiceButton = new Button("Delete Invoice");
        deleteInvoiceButton.setOnAction(event -> deleteInvoice(invoiceListView.getSelectionModel().getSelectedItem()));

        invoiceCRUDButtons.getChildren().addAll(updateInvoiceButton, deleteInvoiceButton);

        invoiceLayout.getChildren().addAll(invoiceListView, invoiceAmountField, addInvoiceButton, invoiceCRUDButtons);
        invoicesTab.setContent(invoiceLayout);

        // Products Tab
        VBox productLayout = new VBox(10);
        ListView<Product> productListView = new ListView<>(products);

        TextField productNameField = new TextField();
        productNameField.setPromptText("Product Name");
        TextField productPriceField = new TextField();
        productPriceField.setPromptText("Product Price");

        Button addProductButton = new Button("Add Product");
        addProductButton.setOnAction(event -> addProduct(productNameField.getText(), productPriceField.getText()));

        HBox productCRUDButtons = new HBox(10);
        Button updateProductButton = new Button("Update Product");
        updateProductButton.setOnAction(event -> updateProduct(productListView.getSelectionModel().getSelectedItem()));

        Button deleteProductButton = new Button("Delete Product");
        deleteProductButton.setOnAction(event -> deleteProduct(productListView.getSelectionModel().getSelectedItem()));

        productCRUDButtons.getChildren().addAll(updateProductButton, deleteProductButton);

        productLayout.getChildren().addAll(productListView, productNameField, productPriceField, addProductButton, productCRUDButtons);
        productsTab.setContent(productLayout);

        // Scene and Stage
        Scene scene = new Scene(tabPane, 800, 600);
        stage.setTitle("JavaFX CRUD Application");
        stage.setScene(scene);
        stage.show();

        // Initial load of data
        loadClients();
        loadCommands();
        loadInvoices();
        loadProducts();
    }

    // Client CRUD operations
    private void loadClients() {
        try {
            List<Client> clientList = clientService.getAllClients();
            clients.setAll(clientList);
        } catch (SQLException e) {
            showError("Error loading clients", e.getMessage());
        }
    }

    private void addClient(String name, String email, String telephone) {
        Client client = new Client(name, email, telephone);
        try {
            clientService.addClient(client);
            loadClients();
        } catch (SQLException e) {
            showError("Error adding client", e.getMessage());
        }
    }

    private void updateClient(Client client) {
        if (client != null) {
            try {
                clientService.updateClient(client);
                loadClients();
            } catch (SQLException e) {
                showError("Error updating client", e.getMessage());
            }
        }
    }

    private void deleteClient(Client client) {
        if (client != null) {
            try {
                clientService.deleteClient(client.getIdClient());
                loadClients();
            } catch (SQLException e) {
                showError("Error deleting client", e.getMessage());
            }
        }
    }

    // Command CRUD operations
    private void loadCommands() {
        try {
            List<Command> commandList = commandService.getAllCommands();
            commands.setAll(commandList);
        } catch (SQLException e) {
            showError("Error loading commands", e.getMessage());
        }
    }

    private void addCommand(String clientId) {
        Command command = new Command(LocalDateTime.now(), Integer.parseInt(clientId), FXCollections.observableArrayList());
        try {
            commandService.addCommand(command);
            loadCommands();
        } catch (SQLException e) {
            showError("Error adding command", e.getMessage());
        }
    }

    private void updateCommand(Command command) {
        if (command != null) {
            try {
                commandService.updateCommand(command);
                loadCommands();
            } catch (SQLException e) {
                showError("Error updating command", e.getMessage());
            }
        }
    }

    private void deleteCommand(Command command) {
        if (command != null) {
            try {
                commandService.deleteCommand(command.getIdClient());
                loadCommands();
            } catch (SQLException e) {
                showError("Error deleting command", e.getMessage());
            }
        }
    }

    // Invoice CRUD operations
    private void loadInvoices() {
        try {
            List<Invoice> invoiceList = invoiceService.getAllInvoices();
            invoices.setAll(invoiceList);
        } catch (SQLException e) {
            showError("Error loading invoices", e.getMessage());
        }
    }

    private void addInvoice(String amount) {
        Invoice invoice = new Invoice(LocalDateTime.now(), Double.parseDouble(amount), 1, List.of());
        try {
            invoiceService.addInvoice(invoice);
            loadInvoices();
        } catch (SQLException e) {
            showError("Error adding invoice", e.getMessage());
        }
    }

    private void updateInvoice(Invoice invoice) {
        if (invoice != null) {
            try {
                invoiceService.updateInvoice(invoice);
                loadInvoices();
            } catch (SQLException e) {
                showError("Error updating invoice", e.getMessage());
            }
        }
    }

    private void deleteInvoice(Invoice invoice) {
        if (invoice != null) {
            try {
                invoiceService.deleteInvoice(invoice.getIdClient());
                loadInvoices();
            } catch (SQLException e) {
                showError("Error deleting invoice", e.getMessage());
            }
        }
    }

    // Product CRUD operations
    private void loadProducts() {
        try {
            List<Product> productList = productService.getAllProducts();
            products.setAll(productList);
        } catch (SQLException e) {
            showError("Error loading products", e.getMessage());
        }
    }

    private void addProduct(String name, String price) {
        Product product = new Product(name, Double.parseDouble(price));
        try {
            productService.addProduct(product);
            loadProducts();
        } catch (SQLException e) {
            showError("Error adding product", e.getMessage());
        }
    }

    private void updateProduct(Product product) {
        if (product != null) {
            try {
                productService.updateProduct(product);
                loadProducts();
            } catch (SQLException e) {
                showError("Error updating product", e.getMessage());
            }
        }
    }

    private void deleteProduct(Product product) {
        if (product != null) {
            try {
                productService.deleteProduct(product.getId());
                loadProducts();
            } catch (SQLException e) {
                showError("Error deleting product", e.getMessage());
            }
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
