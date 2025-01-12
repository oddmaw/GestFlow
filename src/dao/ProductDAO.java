package dao;

import database.DatabaseConnection;
import models.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {


    public ProductDAO() {
    }

    public Product getProductById(int id) throws SQLException {
        String query = "SELECT * FROM products WHERE idProduit = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try(ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()) {
                    return mapProduct(resultSet);
                }
                return null;
            }
        }
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                products.add(mapProduct(resultSet));
            }
        }
        return products;
    }

    public int addProduct(Product product) throws SQLException {
        String query = "INSERT INTO products (nom, prix, quantiteEnStock) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, product.getNom());
            statement.setDouble(2, product.getPrix());
            statement.setInt(3, product.getQuantiteEnStock());
            return statement.executeUpdate();
        }
    }

    public int updateProduct(Product product) throws SQLException {
        String query = "UPDATE products SET nom = ?, prix = ?, quantiteEnStock = ? WHERE idProduit = ?";
        try(Connection connection = DatabaseConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1, product.getNom());
            statement.setDouble(2, product.getPrix());
            statement.setInt(3, product.getQuantiteEnStock());
            statement.setInt(4,product.getIdProduit());
            return statement.executeUpdate();
        }

    }

    public int deleteProduct(int id) throws SQLException {
        String query = "DELETE FROM products WHERE idProduit = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            return statement.executeUpdate();
        }
    }
    private Product mapProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product();
        product.setIdProduit(resultSet.getInt("idProduit"));
        product.setNom(resultSet.getString("nom"));
        product.setPrix(resultSet.getDouble("prix"));
        product.setQuantiteEnStock(resultSet.getInt("quantiteEnStock"));
        return product;
    }
}