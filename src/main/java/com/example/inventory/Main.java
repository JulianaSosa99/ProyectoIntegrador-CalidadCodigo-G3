package com.example.inventory;

import com.example.inventory.model.Product;
import com.example.inventory.model.ElectronicProduct;
import com.example.inventory.model.PerishableProduct;
import com.example.inventory.service.IInventoryOperations;
import com.example.inventory.service.IProductValidator;
import com.example.inventory.service.InventoryManager;
import com.example.inventory.service.ProductValidator;
import com.example.inventory.output.IInventoryOutput;
import com.example.inventory.output.ConsoleInventoryPrinter;
import com.example.inventory.output.DetailedInventoryPrinter;
import com.example.inventory.output.SummaryInventoryPrinter;
import com.example.inventory.exception.InvalidProductException;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        IProductValidator validator = new ProductValidator();
        IInventoryOperations inventory = new InventoryManager(validator);

        try {
            System.out.println("Starting Inventory Management System...\n");

            addSampleProducts(inventory);
            
            demonstrateBasicOperations(inventory);
            
            demonstrateDifferentOutputFormats(inventory);

        } catch (InvalidProductException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void addSampleProducts(IInventoryOperations inventory) {
        System.out.println("Adding products to inventory...\n");

        Product laptop = new ElectronicProduct("Laptop Dell XPS", 5, 1200.00, 24, "Dell");
        Product mouse = new ElectronicProduct("Wireless Mouse", 20, 25.50, 12, "Logitech");
        Product keyboard = new ElectronicProduct("Mechanical Keyboard", 15, 89.99, 18, "Corsair");
        
        Product milk = new PerishableProduct("Organic Milk", 30, 3.99, 
                                            LocalDate.now().plusDays(7));
        Product bread = new PerishableProduct("Whole Wheat Bread", 50, 2.49, 
                                             LocalDate.now().plusDays(5));
        
        Product notebook = new Product("Spiral Notebook", 100, 1.99, "Stationery");
        Product pen = new Product("Ballpoint Pen Pack", 200, 5.99, "Stationery");

        inventory.addProduct(laptop);
        inventory.addProduct(mouse);
        inventory.addProduct(keyboard);
        inventory.addProduct(milk);
        inventory.addProduct(bread);
        inventory.addProduct(notebook);
        inventory.addProduct(pen);
    }

    private static void demonstrateBasicOperations(IInventoryOperations inventory) {
        System.out.println("\n Demonstrating basic operations...\n");

        inventory.updateProductQuantity("Laptop Dell XPS", 8);
        inventory.updateProductPrice("Wireless Mouse", 22.99);
        
        System.out.println("\n Searching for 'Laptop Dell XPS':");
        inventory.findProductByName("Laptop Dell XPS")
                .ifPresent(p -> System.out.println("  Found: " + p.getProductInfo()));
        
        System.out.println("\n Electronics products:");
        inventory.getProductsByCategory("Electronics")
                .forEach(p -> System.out.println("  • " + p.getName()));
    }

    private static void demonstrateDifferentOutputFormats(IInventoryOperations inventory) {
        System.out.println("\n Displaying inventory in different formats...\n");

        IInventoryOutput consolePrinter = new ConsoleInventoryPrinter();
        consolePrinter.displayProducts(inventory.getAllProducts());

        IInventoryOutput detailedPrinter = new DetailedInventoryPrinter();
        detailedPrinter.displayProducts(inventory.getAllProducts());

        IInventoryOutput summaryPrinter = new SummaryInventoryPrinter();
        summaryPrinter.displayProducts(inventory.getAllProducts());
    }
}