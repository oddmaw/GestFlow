package com.example.services;

import com.example.dao.ClientDAO;
import com.example.models.Client;

import java.sql.SQLException;
import java.util.List;

public class ClientService {
    private ClientDAO clientDAO;

    public ClientService(ClientDAO clientDAO) {
        this.clientDAO = clientDAO;
    }
    public Client getClientById(int id) throws SQLException {
        return clientDAO.getClientById(id);
    }
    public List<Client> getAllClients() throws SQLException {
        return clientDAO.getAllClients();
    }
    public int addClient(Client client) throws SQLException {
        return clientDAO.addClient(client);
    }
    public int updateClient(Client client) throws SQLException {
        return clientDAO.updateClient(client);
    }
    public int deleteClient(int id) throws SQLException {
        return clientDAO.deleteClient(id);
    }
}
