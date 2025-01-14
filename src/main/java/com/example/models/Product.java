package com.example.models;

import javafx.beans.property.*;

public class Product {
    private final SimpleIntegerProperty idProduit = new SimpleIntegerProperty();
    private final SimpleStringProperty nom = new SimpleStringProperty();
    private final SimpleDoubleProperty prix = new SimpleDoubleProperty();
    private final SimpleIntegerProperty quantiteEnStock = new SimpleIntegerProperty();

    // Default constructor
    public Product() {}

    // Constructor without idProduit (used when the ID is auto-generated)
    public Product(String nom, double prix, int quantiteEnStock) {
        this.nom.set(nom);
        this.prix.set(prix);
        this.quantiteEnStock.set(quantiteEnStock);
    }

    // Constructor with all fields
    public Product(int idProduit, String nom, double prix, int quantiteEnStock) {
        this.idProduit.set(idProduit);
        this.nom.set(nom);
        this.prix.set(prix);
        this.quantiteEnStock.set(quantiteEnStock);
    }

    // Getter and setter for idProduit
    public int getIdProduit() {
        return idProduit.get();
    }

    public void setIdProduit(int idProduit) {
        this.idProduit.set(idProduit);
    }

    public SimpleIntegerProperty idProduitProperty() {
        return idProduit;
    }

    // Getter and setter for nom
    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    // Getter and setter for prix
    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public SimpleDoubleProperty prixProperty() {
        return prix;
    }

    // Getter and setter for quantiteEnStock
    public int getQuantiteEnStock() {
        return quantiteEnStock.get();
    }

    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock.set(quantiteEnStock);
    }

    public SimpleIntegerProperty quantiteEnStockProperty() {
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
