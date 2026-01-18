package com.example.inventory.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase Product.
 */
public class ProductTest {

    /**
     * Verifica creación correcta de producto.
     */
    @Test
    public void testProductCreation() {
        Product product = new Product(
            "Laptop", 10, 999.99, "Electronics");
        
        assertEquals("Laptop", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(999.99, product.getPrice(), 0.01);
        assertEquals("Electronics", product.getCategory());
    }

    /**
     * Verifica cálculo de valor total.
     */
    @Test
    public void testGetTotalValue() {
        Product product = new Product(
            "Mouse", 5, 20.0, "Electronics");
        
        assertEquals(100.0, product.getTotalValue(), 0.01);
    }

    /**
     * Verifica actualización de cantidad.
     */
    @Test
    public void testSetQuantity() {
        Product product = new Product(
            "Keyboard", 10, 50.0, "Electronics");
        
        product.setQuantity(15);
        
        assertEquals(15, product.getQuantity());
    }

    /**
     * Verifica actualización de precio.
     */
    @Test
    public void testSetPrice() {
        Product product = new Product(
            "Monitor", 5, 200.0, "Electronics");
        
        product.setPrice(180.0);
        
        assertEquals(180.0, product.getPrice(), 0.01);
    }

    /**
     * Verifica formato de información.
     */
    @Test
    public void testGetProductInfo() {
        Product product = new Product(
            "Laptop", 10, 999.99, "Electronics");
        
        String info = product.getProductInfo();
        
        assertTrue(info.contains("Laptop"));
        assertTrue(info.contains("Electronics"));
        assertTrue(info.contains("10"));
    }
}