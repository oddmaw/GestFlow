package com.example.models;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class LineItem {
    private final IntegerProperty idLigne = new SimpleIntegerProperty();
    private final IntegerProperty idProduct = new SimpleIntegerProperty();
    private final IntegerProperty quantity = new SimpleIntegerProperty();
    private final DoubleProperty sousTotal = new SimpleDoubleProperty();
    private final IntegerProperty idFacture = new SimpleIntegerProperty();
    private final IntegerProperty idCommande = new SimpleIntegerProperty();


    public LineItem() {
    }


    public LineItem(int idLigne, int idProduct, int quantity, double sousTotal, int idFacture) {
        this.idLigne.set(idLigne);
        this.idProduct.set(idProduct);
        this.quantity.set(quantity);
        this.sousTotal.set(sousTotal);
        this.idFacture.set(idFacture);
    }


    public LineItem(int idLigne, int idProduct, int quantity, double sousTotal, int idFacture, int idCommande) {
        this.idLigne.set(idLigne);
        this.idProduct.set(idProduct);
        this.quantity.set(quantity);
        this.sousTotal.set(sousTotal);
        this.idFacture.set(idFacture);
        this.idCommande.set(idCommande);
    }
    public LineItem(int idProduct, int quantity, double sousTotal, int idFacture) {
        this.idProduct.set(idProduct);
        this.quantity.set(quantity);
        this.sousTotal.set(sousTotal);
        this.idFacture.set(idFacture);
    }

    public LineItem(int idProduct, int quantity, double sousTotal, int idFacture, int idCommande) {
        this.idProduct.set(idProduct);
        this.quantity.set(quantity);
        this.sousTotal.set(sousTotal);
        this.idFacture.set(idFacture);
        this.idCommande.set(idCommande);
    }


    public LineItem(int idProduct, int quantity, double sousTotal) {
        this.idProduct.set(idProduct);
        this.quantity.set(quantity);
        this.sousTotal.set(sousTotal);
    }

    public int getIdLigne() {
        return idLigne.get();
    }

    public void setIdLigne(int idLigne) {
        this.idLigne.set(idLigne);
    }
    public IntegerProperty idLigneProperty() {
        return idLigne;
    }

    public int getIdProduct() {
        return idProduct.get();
    }

    public void setIdProduct(int idProduct) {
        this.idProduct.set(idProduct);
    }
    public IntegerProperty idProductProperty() {
        return idProduct;
    }


    public int getQuantity() {
        return quantity.get();
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public double getSousTotal() {
        return sousTotal.get();
    }

    public void setSousTotal(double sousTotal) {
        this.sousTotal.set(sousTotal);
    }
    public DoubleProperty sousTotalProperty() {
        return sousTotal;
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

    public int getIdCommande() {
        return idCommande.get();
    }

    public void setIdCommande(int idCommande) {
        this.idCommande.set(idCommande);
    }
    public IntegerProperty idCommandeProperty() {
        return idCommande;
    }

    @Override
    public String toString() {
        return "LineItem{" +
                "idLigne=" + idLigne.get() +
                ", idProduct=" + idProduct.get() +
                ", quantity=" + quantity.get() +
                ", sousTotal=" + sousTotal.get() +
                ", idFacture=" + idFacture.get() +
                ", idCommande=" + idCommande.get() +
                '}';
    }
}