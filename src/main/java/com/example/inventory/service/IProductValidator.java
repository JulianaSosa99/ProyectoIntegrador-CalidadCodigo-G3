package com.example.inventory.service;

import com.example.inventory.model.Product;

public interface IProductValidator {
    void validateProduct(Product product);
    void validateProductName(String name);
    void validateQuantity(int quantity);
    void validatePrice(double price);
}