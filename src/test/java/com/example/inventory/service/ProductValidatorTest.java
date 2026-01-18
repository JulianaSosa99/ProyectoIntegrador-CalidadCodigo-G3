package com.example.inventory.service;

import com.example.inventory.model.Product;
import com.example.inventory.exception
    .InvalidProductException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para ProductValidator.
 */
public class ProductValidatorTest {
    private IProductValidator validator;

    /**
     * Configuración inicial.
     */
    @BeforeEach
    public void setUp() {
        validator = new ProductValidator();
    }

    /**
     * Verifica validación de producto válido.
     */
    @Test
    public void testValidateValidProduct() {
        Product product = new Product(
            "ValidProduct", 10, 50.0, "Category");
        
        assertDoesNotThrow(
            () -> validator.validateProduct(product));
    }

    /**
     * Verifica rechazo de nombre vacío.
     */
    @Test
    public void testValidateEmptyName() {
        assertThrows(InvalidProductException.class, 
            () -> validator.validateProductName(""));
    }

    /**
     * Verifica rechazo de nombre nulo.
     */
    @Test
    public void testValidateNullName() {
        assertThrows(InvalidProductException.class, 
            () -> validator.validateProductName(null));
    }

    /**
     * Verifica rechazo de cantidad negativa.
     */
    @Test
    public void testValidateNegativeQuantity() {
        assertThrows(InvalidProductException.class, 
            () -> validator.validateQuantity(-5));
    }

    /**
     * Verifica rechazo de precio negativo.
     */
    @Test
    public void testValidateNegativePrice() {
        assertThrows(InvalidProductException.class, 
            () -> validator.validatePrice(-10.0));
    }

    /**
     * Verifica aceptación de cantidad cero.
     */
    @Test
    public void testValidateZeroQuantity() {
        assertDoesNotThrow(
            () -> validator.validateQuantity(0));
    }
}