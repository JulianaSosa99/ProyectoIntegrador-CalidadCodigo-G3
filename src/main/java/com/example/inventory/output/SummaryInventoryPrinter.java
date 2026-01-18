package com.example.inventory.output;

import com.example.inventory.model.Product;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SummaryInventoryPrinter implements IInventoryOutput {
    
    @Override
    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.println(" INVENTORY SUMMARY");
        System.out.println("=".repeat(50));
        
        displayOverallStatistics(products);
        displayCategoryBreakdown(products);
        
        System.out.println("=".repeat(50) + "\n");
    }

    private void displayOverallStatistics(List<Product> products) {
        int totalProducts = products.size();
        int totalQuantity = products.stream()
                .mapToInt(Product::getQuantity)
                .sum();
        double totalValue = products.stream()
                .mapToDouble(Product::getTotalValue)
                .sum();
        double averagePrice = products.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0);

        System.out.println("\n  Total Products: " + totalProducts);
        System.out.println("  Total Items: " + totalQuantity);
        System.out.printf("  Total Inventory Value: $%.2f\n", totalValue);
        System.out.printf("  Average Price: $%.2f\n", averagePrice);
    }

    private void displayCategoryBreakdown(List<Product> products) {
        Map<String, Long> categoryCount = products.stream()
                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()));

        System.out.println("\n  Products by Category:");
        categoryCount.forEach((category, count) -> 
            System.out.printf("    • %s: %d products\n", category, count));
    }
}