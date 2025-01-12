package models;

import java.time.LocalDateTime;
import java.util.List;

public class Invoice {
    private int idFacture;
    private LocalDateTime date;
    private double montantTotal;
    private int idClient;
    private List<LineItem> lineItems;


    public Invoice() {
    }
    public Invoice(int idFacture, LocalDateTime date, double montantTotal, int idClient, List<LineItem> lineItems) {
        this.idFacture = idFacture;
        this.date = date;
        this.montantTotal = montantTotal;
        this.idClient = idClient;
        this.lineItems = lineItems;
    }

    public Invoice(LocalDateTime date, double montantTotal, int idClient, List<LineItem> lineItems) {
        this.date = date;
        this.montantTotal = montantTotal;
        this.idClient = idClient;
        this.lineItems = lineItems;
    }

    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public double getMontantTotal() {
        return montantTotal;
    }

    public void setMontantTotal(double montantTotal) {
        this.montantTotal = montantTotal;
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
        return "Invoice{" +
                "idFacture=" + idFacture +
                ", date=" + date +
                ", montantTotal=" + montantTotal +
                ", idClient=" + idClient +
                ", lineItems=" + lineItems +
                '}';
    }
}