package com.example.models;

import java.time.LocalDateTime;
import java.util.List;

public class Command {
    private int idCommande;
    private LocalDateTime date;
    private int idClient;
    private List<LineItem> lineItems;


    public Command() {
    }


    public Command(int idCommande, LocalDateTime date, int idClient, List<LineItem> lineItems) {
        this.idCommande = idCommande;
        this.date = date;
        this.idClient = idClient;
        this.lineItems = lineItems;
    }


    public Command(LocalDateTime date, int idClient, List<LineItem> lineItems) {
        this.date = date;
        this.idClient = idClient;
        this.lineItems = lineItems;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
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
                "idCommande=" + idCommande +
                ", date=" + date +
                ", idClient=" + idClient +
                ", lineItems=" + lineItems +
                '}';
    }
}
