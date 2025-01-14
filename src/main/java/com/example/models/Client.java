package com.example.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Client {
    private SimpleIntegerProperty idClient;
    private SimpleStringProperty nom;
    private SimpleStringProperty email;
    private SimpleStringProperty telephone;

    public Client() {
        this.idClient = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.telephone = new SimpleStringProperty();
    }

    public Client(String nom, String email, String telephone) {
        this.idClient = new SimpleIntegerProperty();
        this.nom = new SimpleStringProperty(nom);
        this.email = new SimpleStringProperty(email);
        this.telephone = new SimpleStringProperty(telephone);
    }

    public int getIdClient() {
        return idClient.get();
    }

    public SimpleIntegerProperty idClientProperty() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient.set(idClient);
    }

    public String getNom() {
        return nom.get();
    }

    public SimpleStringProperty nomProperty() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTelephone() {
        return telephone.get();
    }

    public SimpleStringProperty telephoneProperty() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone.set(telephone);
    }

    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + idClient +
                ", nom=" + nom +
                ", email=" + email +
                ", telephone=" + telephone +
                '}';
    }
}
