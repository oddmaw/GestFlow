package com.example.services;

import com.example.dao.ProductDAO;
import com.example.models.Product;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    public Product getProductById(int id) throws SQLException {
        return productDAO.getProductById(id);
    }
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.getAllProducts();
    }
    public int addProduct(Product product) throws SQLException {
        return productDAO.addProduct(product);
    }
    public int updateProduct(Product product) throws SQLException {
        return productDAO.updateProduct(product);
    }
    public int deleteProduct(int id) throws SQLException {
        return productDAO.deleteProduct(id);
    }
}
