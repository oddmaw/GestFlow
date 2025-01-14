package com.example.models;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty idProduit = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final DoubleProperty prix = new SimpleDoubleProperty();
    private final IntegerProperty quantiteEnStock = new SimpleIntegerProperty();

    public Product() {}

    public Product(String nom, double prix, int quantiteEnStock) {
        this.nom.set(nom);
        this.prix.set(prix);
        this.quantiteEnStock.set(quantiteEnStock);
    }

    public Product(int idProduit, String nom, double prix, int quantiteEnStock) {
        this.idProduit.set(idProduit);
        this.nom.set(nom);
        this.prix.set(prix);
        this.quantiteEnStock.set(quantiteEnStock);
    }

    public int getIdProduit() {
        return idProduit.get();
    }

    public void setIdProduit(int idProduit) {
        this.idProduit.set(idProduit);
    }

    public IntegerProperty idProduitProperty() {
        return idProduit;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public DoubleProperty prixProperty() {
        return prix;
    }

    public int getQuantiteEnStock() {
        return quantiteEnStock.get();
    }

    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock.set(quantiteEnStock);
    }

    public IntegerProperty quantiteEnStockProperty() {
        return quantiteEnStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduit=" + idProduit.get() +
                ", nom='" + nom.get() + '\'' +
                ", prix=" + prix.get() +
                ", quantiteEnStock=" + quantiteEnStock.get() +
                '}';
    }
}
