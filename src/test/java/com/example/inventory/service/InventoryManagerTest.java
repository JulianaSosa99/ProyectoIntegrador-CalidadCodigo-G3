package com.example.inventory.service;

import com.example.inventory.model.Product;
import com.example.inventory.exception
    .InvalidProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

/**
 * Pruebas unitarias para InventoryManager.
 */
public class InventoryManagerTest {
    private IInventoryOperations inventory;
    private IProductValidator validator;

    /**
     * Configuración inicial de cada prueba.
     */
    @BeforeEach
    public void setUp() {
        validator = new ProductValidator();
        inventory = new InventoryManager(validator);
    }

    /**
     * Verifica agregar producto correctamente.
     */
    @Test
    public void testAddProduct() {
        Product product = new Product(
            "Laptop", 5, 1000.0, "Electronics");
        
        inventory.addProduct(product);
        
        assertEquals(1, inventory.getTotalProductCount());
    }

    /**
     * Verifica fusión de productos duplicados.
     */
    @Test
    public void testAddDuplicateProduct() {
        Product p1 = new Product(
            "Mouse", 10, 25.0, "Electronics");
        Product p2 = new Product(
            "Mouse", 5, 25.0, "Electronics");
        
        inventory.addProduct(p1);
        inventory.addProduct(p2);
        
        assertEquals(1, inventory.getTotalProductCount());
        Optional<Product> found = 
            inventory.findProductByName("Mouse");
        assertTrue(found.isPresent());
        assertEquals(15, found.get().getQuantity());
    }

    /**
     * Verifica eliminación de producto.
     */
    @Test
    public void testRemoveProduct() {
        Product product = new Product(
            "Keyboard", 10, 50.0, "Electronics");
        
        inventory.addProduct(product);
        boolean removed = 
            inventory.removeProduct("Keyboard");
        
        assertTrue(removed);
        assertEquals(0, inventory.getTotalProductCount());
    }

    /**
     * Verifica búsqueda de producto existente.
     */
    @Test
    public void testFindProductByName() {
        Product product = new Product(
            "Monitor", 3, 300.0, "Electronics");
        
        inventory.addProduct(product);
        Optional<Product> found = 
            inventory.findProductByName("Monitor");
        
        assertTrue(found.isPresent());
        assertEquals("Monitor", found.get().getName());
    }

    /**
     * Verifica búsqueda de producto inexistente.
     */
    @Test
    public void testFindNonExistentProduct() {
        Optional<Product> found = 
            inventory.findProductByName("Ghost");
        
        assertFalse(found.isPresent());
    }

    /**
     * Verifica filtrado por categoría.
     */
    @Test
    public void testGetProductsByCategory() {
        inventory.addProduct(new Product(
            "Laptop", 5, 1000.0, "Electronics"));
        inventory.addProduct(new Product(
            "Mouse", 10, 25.0, "Electronics"));
        inventory.addProduct(new Product(
            "Notebook", 20, 5.0, "Stationery"));
        
        List<Product> electronics = 
            inventory.getProductsByCategory("Electronics");
        
        assertEquals(2, electronics.size());
    }

    /**
     * Verifica actualización de cantidad.
     */
    @Test
    public void testUpdateProductQuantity() {
        Product product = new Product(
            "Pen", 50, 1.0, "Stationery");
        
        inventory.addProduct(product);
        inventory.updateProductQuantity("Pen", 100);
        
        Optional<Product> updated = 
            inventory.findProductByName("Pen");
        assertTrue(updated.isPresent());
        assertEquals(100, updated.get().getQuantity());
    }

    /**
     * Verifica actualización de precio.
     */
    @Test
    public void testUpdateProductPrice() {
        Product product = new Product(
            "Eraser", 30, 0.50, "Stationery");
        
        inventory.addProduct(product);
        inventory.updateProductPrice("Eraser", 0.75);
        
        Optional<Product> updated = 
            inventory.findProductByName("Eraser");
        assertTrue(updated.isPresent());
        assertEquals(0.75, updated.get().getPrice(), 0.01);
    }

    /**
     * Verifica cálculo de valor total.
     */
    @Test
    public void testCalculateTotalInventoryValue() {
        inventory.addProduct(new Product(
            "Item1", 10, 10.0, "Category1"));
        inventory.addProduct(new Product(
            "Item2", 5, 20.0, "Category2"));
        
        double total = 
            inventory.calculateTotalInventoryValue();
        
        assertEquals(200.0, total, 0.01);
    }

    /**
     * Verifica excepción con producto inválido.
     */
    @Test
    public void testAddInvalidProduct() {
        Product invalid = new Product(
            "", 10, 50.0, "Category");
        
        assertThrows(InvalidProductException.class, 
            () -> inventory.addProduct(invalid));
    }
}