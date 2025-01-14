package com.example;

import com.example.dao.ClientDAO;
import com.example.dao.CommandDAO;
import com.example.dao.InvoiceDAO;
import com.example.dao.ProductDAO;
import com.example.database.DatabaseConnection;
import com.example.services.ClientService;
import com.example.services.CommandService;
import com.example.services.InvoiceService;
import com.example.services.ProductService;
import com.example.ui.ClientManagementUI;
import com.example.ui.ProductManagementUI;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.InputStream;


public class Main extends Application {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/company_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root123456n";
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        DatabaseConnection dbConnection = new DatabaseConnection(DB_URL, DB_USER, DB_PASSWORD);
        // Initialize DAOs with injected DatabaseConnection
        ClientDAO clientDAO = new ClientDAO(dbConnection);
        ProductDAO productDAO = new ProductDAO(dbConnection);
        InvoiceDAO invoiceDAO = new InvoiceDAO(dbConnection);
        CommandDAO commandDAO = new CommandDAO(dbConnection);
        // Initialize Services with injected DAOs
        ClientService clientService = new ClientService(clientDAO);
        ProductService productService = new ProductService(productDAO);
        InvoiceService invoiceService = new InvoiceService(invoiceDAO);
        CommandService commandService = new CommandService(commandDAO);

        TabPane tabPane = new TabPane();

        Tab productTab = new Tab("Product Management");
        ProductManagementUI.setProductService(productService);
        productTab.setContent(ProductManagementUI.getUI());
        tabPane.getTabs().add(productTab);

        Tab clientTab = new Tab("Client Management");
        ClientManagementUI.setClientService(clientService);
        clientTab.setContent(ClientManagementUI.getUI());
        tabPane.getTabs().add(clientTab);

        // 1080p Resolution or Fullscreen
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        // Set scene size based on screen size or default to 1080p if screen is too small
        double width = Math.max(1920, screenWidth);
        double height = Math.max(1080, screenHeight);

        Scene scene = new Scene(tabPane, width, height);

        //Load Styles
        InputStream cssStream = getClass().getResourceAsStream("/style.css");
        if (cssStream != null) {
            String css = new java.util.Scanner(cssStream).useDelimiter("\\A").next();
            scene.getStylesheets().add(getClass().getResource("/style.css").toExternalForm());
        }


        primaryStage.setTitle("Order Management System");
        primaryStage.setScene(scene);
        // Fullscreen without message
        primaryStage.setFullScreen(true);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                primaryStage.setFullScreen(false);
                primaryStage.setWidth(width);
                primaryStage.setHeight(height);
            }
        });
        primaryStage.show();
    }
}