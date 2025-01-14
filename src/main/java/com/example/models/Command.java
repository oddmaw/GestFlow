package com.example.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

public class Command {
    private final IntegerProperty idCommande = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private final IntegerProperty idClient = new SimpleIntegerProperty();
    private final ListProperty<LineItem> lineItems = new SimpleListProperty<>(FXCollections.observableArrayList());

    // Default Constructor
    public Command() {}

    // Constructor with all fields
    public Command(int idCommande, LocalDateTime date, int idClient, ObservableList<LineItem> lineItems) {
        this.idCommande.set(idCommande);
        this.date.set(date);
        this.idClient.set(idClient);
        this.lineItems.set(lineItems);
    }

    // Constructor without idCommande
    public Command(LocalDateTime date, int idClient, ObservableList<LineItem> lineItems) {
        this.date.set(date);
        this.idClient.set(idClient);
        this.lineItems.set(lineItems);
    }

    // Getters and Setters with Properties
    public int getIdCommande() {
        return idCommande.get();
    }

    public void setIdCommande(int idCommande) {
        this.idCommande.set(idCommande);
    }

    public IntegerProperty idCommandeProperty() {
        return idCommande;
    }

    public LocalDateTime getDate() {
        return date.get();
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    public ObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public int getIdClient() {
        return idClient.get();
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    public IntegerProperty idClientProperty() {
        return idClient;
    }

    public ObservableList<LineItem> getLineItems() {
        return lineItems.get();
    }

    public void setLineItems(ObservableList<LineItem> lineItems) {
        this.lineItems.set(lineItems);
    }

    public ListProperty<LineItem> lineItemsProperty() {
        return lineItems;
    }

    @Override
    public String toString() {
        return "Command{" +
                "idCommande=" + idCommande.get() +
                ", date=" + date.get() +
                ", idClient=" + idClient.get() +
                ", lineItems=" + lineItems.get() +
                '}';
    }
}
