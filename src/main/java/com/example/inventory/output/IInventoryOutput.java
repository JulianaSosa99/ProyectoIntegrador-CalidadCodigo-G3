package com.example.inventory.output;

import com.example.inventory.model.Product;
import java.util.List;

public interface IInventoryOutput {
    void displayProducts(List<Product> products);
}