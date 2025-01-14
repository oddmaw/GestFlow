package com.example.models;

public class Product {
    private int idProduit;
    private String nom;
    private double prix;
    private int quantiteEnStock;

    public Product() {
    }

    public Product(int idProduit, String nom, double prix, int quantiteEnStock) {
        this.idProduit = idProduit;
        this.nom = nom;
        this.prix = prix;
        this.quantiteEnStock = quantiteEnStock;
    }


    public Product(String nom, double prix, int quantiteEnStock) {
        this.nom = nom;
        this.prix = prix;
        this.quantiteEnStock = quantiteEnStock;
    }

    public int getIdProduit() {
        return idProduit;
    }

    public void setIdProduit(int idProduit) {
        this.idProduit = idProduit;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getQuantiteEnStock() {
        return quantiteEnStock;
    }

    public void setQuantiteEnStock(int quantiteEnStock) {
        this.quantiteEnStock = quantiteEnStock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduit=" + idProduit +
                ", nom='" + nom + '\'' +
                ", prix=" + prix +
                ", quantiteEnStock=" + quantiteEnStock +
                '}';
    }
}
