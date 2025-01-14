package com.example.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private final IntegerProperty idFacture = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private final DoubleProperty montantTotal = new SimpleDoubleProperty();
    private final IntegerProperty idClient = new SimpleIntegerProperty();
    private final DoubleProperty totalDiscount = new SimpleDoubleProperty();
    private List<LineItem> lineItems;


    public Invoice() {
    }
    public Invoice(int idFacture, LocalDateTime date, double montantTotal, int idClient, List<LineItem> lineItems, double totalDiscount) {
        this.idFacture.set(idFacture);
        this.date.set(date);
        this.montantTotal.set(montantTotal);
        this.idClient.set(idClient);
        this.lineItems = lineItems;
        this.totalDiscount.set(totalDiscount);
    }

    public Invoice(LocalDateTime date, double montantTotal, int idClient, List<LineItem> lineItems, double totalDiscount) {
        this.date.set(date);
        this.montantTotal.set(montantTotal);
        this.idClient.set(idClient);
        this.lineItems = lineItems;
        this.totalDiscount.set(totalDiscount);
    }

    public int getIdFacture() {
        return idFacture.get();
    }

    public void setIdFacture(int idFacture) {
        this.idFacture.set(idFacture);
    }
    public IntegerProperty idFactureProperty() {
        return idFacture;
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

    public double getMontantTotal() {
        return montantTotal.get();
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal.set(montantTotal);
    }
    public DoubleProperty montantTotalProperty() {
        return montantTotal;
    }
    public double getTotalDiscount() {
        return totalDiscount.get();
    }
    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount.set(totalDiscount);
    }
    public DoubleProperty totalDiscountProperty() {
        return totalDiscount;
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

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "idFacture=" + idFacture.get() +
                ", date=" + date.get() +
                ", montantTotal=" + montantTotal.get() +
                ", idClient=" + idClient.get() +
                ", lineItems=" + lineItems +
                ", totalDiscount=" + totalDiscount.get() +
                '}';
    }
}
