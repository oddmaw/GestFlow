package com.example.models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private SimpleIntegerProperty idFacture;
    private SimpleObjectProperty<LocalDateTime> date;
    private SimpleDoubleProperty montantTotal;
    private SimpleIntegerProperty idClient;
    private ListProperty<LineItem> lineItems;

    // Default constructor
    public Invoice() {
        this.idFacture = new SimpleIntegerProperty();
        this.date = new SimpleObjectProperty<>();
        this.montantTotal = new SimpleDoubleProperty();
        this.idClient = new SimpleIntegerProperty();
        this.lineItems = new SimpleListProperty<>();
    }

    // Constructor with all fields
    public Invoice(int idFacture, LocalDateTime date, double montantTotal, int idClient, List<LineItem> lineItems) {
        this.idFacture = new SimpleIntegerProperty(idFacture);
        this.date = new SimpleObjectProperty<>(date);
        this.montantTotal = new SimpleDoubleProperty(montantTotal);
        this.idClient = new SimpleIntegerProperty(idClient);
        this.lineItems = new SimpleListProperty<>(FXCollections.observableArrayList(lineItems));
    }

    // Constructor without idFacture (for when it is auto-generated)
    public Invoice(LocalDateTime date, double montantTotal, int idClient, List<LineItem> lineItems) {
        this.idFacture = new SimpleIntegerProperty();  // Will be set by DB
        this.date = new SimpleObjectProperty<>(date);
        this.montantTotal = new SimpleDoubleProperty(montantTotal);
        this.idClient = new SimpleIntegerProperty(idClient);
        this.lineItems = new SimpleListProperty<>(FXCollections.observableArrayList(lineItems));
    }

    // Getter and setter for idFacture
    public int getIdFacture() {
        return idFacture.get();
    }

    public SimpleIntegerProperty idFactureProperty() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture.set(idFacture);
    }

    // Getter and setter for date
    public LocalDateTime getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDateTime> dateProperty() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date.set(date);
    }

    // Getter and setter for montantTotal
    public double getMontantTotal() {
        return montantTotal.get();
    }

    public SimpleDoubleProperty montantTotalProperty() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal.set(montantTotal);
    }

    // Getter and setter for idClient
    public int getIdClient() {
        return idClient.get();
    }

    public SimpleIntegerProperty idClientProperty() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    // Getter and setter for lineItems
    public List<LineItem> getLineItems() {
        return lineItems.get();
    }

    public ListProperty<LineItem> lineItemsProperty() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems.setAll(lineItems);
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "idFacture=" + idFacture.get() +
                ", date=" + date.get() +
                ", montantTotal=" + montantTotal.get() +
                ", idClient=" + idClient.get() +
                ", lineItems=" + lineItems.get() +
                '}';
    }
}
