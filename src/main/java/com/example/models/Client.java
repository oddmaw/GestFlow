package com.example.models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Client {
    private final IntegerProperty idClient = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty telephone = new SimpleStringProperty();


    public Client() {
    }
    public Client(int idClient, String nom, String email, String telephone) {
        this.idClient.set(idClient);
        this.nom.set(nom);
        this.email.set(email);
        this.telephone.set(telephone);
    }
    public Client(String nom, String email, String telephone) {
        this.nom.set(nom);
        this.email.set(email);
        this.telephone.set(telephone);
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

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }
    public StringProperty nomProperty() {
        return nom;
    }


    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }
    public StringProperty emailProperty() {
        return email;
    }


    public String getTelephone() {
        return telephone.get();
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }
    public StringProperty telephoneProperty() {
        return telephone;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient.get() +
                ", nom='" + nom.get() + '\'' +
                ", email='" + email.get() + '\'' +
                ", telephone='" + telephone.get() + '\'' +
                '}';
    }
}