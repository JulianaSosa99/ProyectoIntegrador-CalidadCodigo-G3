package com.example.inventory.output;

import com.example.inventory.model.Product;
import java.util.List;

public class ConsoleInventoryPrinter implements IInventoryOutput {
    
    @Override
    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(" Inventory is empty.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("INVENTORY LIST");
        System.out.println("=".repeat(60));
        
        products.forEach(product -> 
            System.out.println("  • " + product.getProductInfo()));
        
        System.out.println("=".repeat(60) + "\n");
    }
}