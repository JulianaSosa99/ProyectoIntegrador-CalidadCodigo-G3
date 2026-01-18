package com.example.inventory.output;

import com.example.inventory.model.Product;
import com.example.inventory.model.ElectronicProduct;
import com.example.inventory.model.PerishableProduct;
import java.util.List;

/**
 * Impresora detallada de inventario.
 * Muestra información completa de cada producto.
 */
public class DetailedInventoryPrinter 
        implements IInventoryOutput {
    
    @Override
    public void displayProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println(" Inventory is empty.");
            return;
        }

        System.out.println("\n" + "=".repeat(70));
        System.out.println(" DETAILED INVENTORY REPORT");
        System.out.println("=".repeat(70));
        
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf(
                "\n[%d] %s\n", i + 1, product.getName());
            System.out.println(
                "    Category: " + product.getCategory());
            System.out.printf(
                "    Price: $%.2f | Quantity: %d\n", 
                product.getPrice(), product.getQuantity());
            System.out.printf(
                "    Total Value: $%.2f\n", 
                product.getTotalValue());
            
            printAdditionalDetails(product);
        }
        
        System.out.println("\n" + "=".repeat(70) + "\n");
    }

    /**
     * Imprime detalles específicos según tipo.
     * 
     * @param product Producto a detallar
     */
    private void printAdditionalDetails(Product product) {
        if (product instanceof ElectronicProduct) {
            ElectronicProduct electronic = 
                (ElectronicProduct) product;
            System.out.println(
                "    Brand: " + electronic.getBrand());
            System.out.println(
                "    Warranty: " 
                + electronic.getWarrantyMonths() 
                + " months");
        } else if (product instanceof PerishableProduct) {
            PerishableProduct perishable = 
                (PerishableProduct) product;
            System.out.println(
                "    Expiration: " 
                + perishable.getExpirationDate());
            System.out.println(
                "    Days until expiration: " 
                + perishable.getDaysUntilExpiration());
            System.out.println(
                "    Status: " 
                + (perishable.isExpired() 
                    ? " EXPIRED" : "✓ Fresh"));
        }
    }
}