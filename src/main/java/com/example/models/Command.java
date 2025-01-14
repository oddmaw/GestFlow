package com.example.models;

import javafx.beans.property.*;

import java.time.LocalDateTime;
import java.util.List;

public class Command {
    private final IntegerProperty idCommande = new SimpleIntegerProperty();
    private final ObjectProperty<LocalDateTime> date = new SimpleObjectProperty<>();
    private final IntegerProperty idClient = new SimpleIntegerProperty();
    private List<LineItem> lineItems;


    public Command() {
    }


    public Command(int idCommande, LocalDateTime date, int idClient, List<LineItem> lineItems) {
        this.idCommande.set(idCommande);
        this.date.set(date);
        this.idClient.set(idClient);
        this.lineItems = lineItems;
    }


    public Command(LocalDateTime date, int idClient, List<LineItem> lineItems) {
        this.date.set(date);
        this.idClient.set(idClient);
        this.lineItems = lineItems;
    }

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


    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    @Override
    public String toString() {
        return "Command{" +
                "idCommande=" + idCommande.get() +
                ", date=" + date.get() +
                ", idClient=" + idClient.get() +
                ", lineItems=" + lineItems +
                '}';
    }
}