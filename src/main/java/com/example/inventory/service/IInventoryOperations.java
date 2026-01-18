package com.example.inventory.service;

import com.example.inventory.model.Product;
import java.util.List;
import java.util.Optional;

public interface IInventoryOperations {
    void addProduct(Product product);
    boolean removeProduct(String productName);
    Optional<Product> findProductByName(String productName);
    List<Product> getAllProducts();
    List<Product> getProductsByCategory(String category);
    void updateProductQuantity(String productName, int newQuantity);
    void updateProductPrice(String productName, double newPrice);
    int getTotalProductCount();
    double calculateTotalInventoryValue();
}