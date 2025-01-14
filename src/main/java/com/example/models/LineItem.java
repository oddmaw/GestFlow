package com.example.models;

public class LineItem {
    private int idLigne;
    private int idProduct;
    private int quantity;
    private double sousTotal;
    private int idFacture;
    private int idCommande;


    public LineItem() {
    }


    public LineItem(int idLigne, int idProduct, int quantity, double sousTotal, int idFacture) {
        this.idLigne = idLigne;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.sousTotal = sousTotal;
        this.idFacture = idFacture;
    }


    public LineItem(int idLigne, int idProduct, int quantity, double sousTotal, int idFacture, int idCommande) {
        this.idLigne = idLigne;
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.sousTotal = sousTotal;
        this.idFacture = idFacture;
        this.idCommande = idCommande;
    }
    public LineItem(int idProduct, int quantity, double sousTotal, int idFacture) {
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.sousTotal = sousTotal;
        this.idFacture = idFacture;
    }

    public LineItem(int idProduct, int quantity, double sousTotal, int idFacture, int idCommande) {
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.sousTotal = sousTotal;
        this.idFacture = idFacture;
        this.idCommande = idCommande;
    }


    public LineItem(int idProduct, int quantity, double sousTotal) {
        this.idProduct = idProduct;
        this.quantity = quantity;
        this.sousTotal = sousTotal;
    }

    public int getIdLigne() {
        return idLigne;
    }

    public void setIdLigne(int idLigne) {
        this.idLigne = idLigne;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSousTotal() {
        return sousTotal;
    }

    public void setSousTotal(double sousTotal) {
        this.sousTotal = sousTotal;
    }


    public int getIdFacture() {
        return idFacture;
    }

    public void setIdFacture(int idFacture) {
        this.idFacture = idFacture;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "idLigne=" + idLigne +
                ", idProduct=" + idProduct +
                ", quantity=" + quantity +
                ", sousTotal=" + sousTotal +
                ", idFacture=" + idFacture +
                ", idCommande=" + idCommande +
                '}';
    }
}
