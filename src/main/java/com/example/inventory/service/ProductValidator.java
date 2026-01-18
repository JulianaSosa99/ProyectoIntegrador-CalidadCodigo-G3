package com.example.inventory.service;

import com.example.inventory.model.Product;
import com.example.inventory.exception.InvalidProductException;

public class ProductValidator implements IProductValidator {
    
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 100;
    private static final double MIN_PRICE = 0.01;
    private static final int MAX_QUANTITY = 100000;

    @Override
    public void validateProduct(Product product) {
        if (product == null) {
            throw new InvalidProductException("Product cannot be null");
        }
        validateProductName(product.getName());
        validateQuantity(product.getQuantity());
        validatePrice(product.getPrice());
    }

    @Override
    public void validateProductName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidProductException("Product name cannot be empty");
        }
        if (name.length() < MIN_NAME_LENGTH) {
            throw new InvalidProductException(
                String.format("Product name must be at least %d characters", MIN_NAME_LENGTH));
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidProductException(
                String.format("Product name cannot exceed %d characters", MAX_NAME_LENGTH));
        }
    }

    @Override
    public void validateQuantity(int quantity) {
        if (quantity < 0) {
            throw new InvalidProductException("Quantity cannot be negative");
        }
        if (quantity > MAX_QUANTITY) {
            throw new InvalidProductException(
                String.format("Quantity cannot exceed %d", MAX_QUANTITY));
        }
    }

    @Override
    public void validatePrice(double price) {
        if (price < 0) {
            throw new InvalidProductException("Price cannot be negative");
        }
        if (price < MIN_PRICE && price > 0) {
            throw new InvalidProductException(
                String.format("Price must be at least $%.2f", MIN_PRICE));
        }
    }
}