package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import com.example.ui.ClientManagementUI;
import com.example.ui.ProductManagementUI;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        // TabPane for navigation
        TabPane tabPane = new TabPane();

        // Add tabs for different functionalities
        Tab clientTab = new Tab("Client Management", ClientManagementUI.getUI());
        Tab productTab = new Tab("Product Management", ProductManagementUI.getUI());
        // Add more tabs for other functionalities as needed

        tabPane.getTabs().addAll(clientTab, productTab);

        // Scene and Stage setup
        Scene scene = new Scene(tabPane, 800, 600);
        primaryStage.setTitle("Business Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
