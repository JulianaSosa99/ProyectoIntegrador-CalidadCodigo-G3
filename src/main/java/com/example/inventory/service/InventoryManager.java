package com.example.inventory.service;

import com.example.inventory.model.Product;
import com.example.inventory.exception
    .InvalidProductException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Gestor principal del inventario de productos.
 * Implementa operaciones CRUD y consultas.
 * 
 * @author Inventory Team
 * @version 1.0
 */
public class InventoryManager 
        implements IInventoryOperations {
    private final List<Product> products;
    private final IProductValidator validator;

    /**
     * Constructor del gestor de inventario.
     * 
     * @param validator Validador de productos
     */
    public InventoryManager(IProductValidator validator) {
        this.products = new ArrayList<>();
        this.validator = validator;
    }

    /**
     * Agrega un producto al inventario.
     * Si ya existe, suma cantidades.
     * 
     * @param product Producto a agregar
     * @throws InvalidProductException si es inválido
     */
    @Override
    public void addProduct(Product product) {
        validator.validateProduct(product);
        
        Optional<Product> existingProduct = 
            findProductByName(product.getName());
        
        if (existingProduct.isPresent()) {
            mergeProductQuantity(
                existingProduct.get(), 
                product.getQuantity());
        } else {
            products.add(product);
            System.out.println(
                "✓ Product added: " + product.getName());
        }
    }

    /**
     * Fusiona cantidad de producto existente.
     * 
     * @param existing Producto existente
     * @param additionalQuantity Cantidad adicional
     */
    private void mergeProductQuantity(Product existing, 
                                      int additionalQuantity) {
        int newQuantity = existing.getQuantity() 
            + additionalQuantity;
        validator.validateQuantity(newQuantity);
        existing.setQuantity(newQuantity);
        System.out.println(
            "✓ Quantity updated: " + existing.getName());
    }

    /**
     * Elimina un producto del inventario.
     * 
     * @param productName Nombre del producto
     * @return true si se eliminó, false si no existe
     */
    @Override
    public boolean removeProduct(String productName) {
        boolean removed = products.removeIf(
            p -> p.getName()
                .equalsIgnoreCase(productName));
        
        if (removed) {
            System.out.println(
                "✓ Product removed: " + productName);
        } else {
            System.out.println(
                "✗ Product not found: " + productName);
        }
        
        return removed;
    }

    /**
     * Busca un producto por nombre.
     * 
     * @param productName Nombre a buscar
     * @return Optional con el producto si existe
     */
    @Override
    public Optional<Product> findProductByName(
            String productName) {
        return products.stream()
                .filter(p -> p.getName()
                    .equalsIgnoreCase(productName))
                .findFirst();
    }

    /**
     * Obtiene todos los productos.
     * 
     * @return Lista de todos los productos
     */
    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    /**
     * Filtra productos por categoría.
     * 
     * @param category Categoría a buscar
     * @return Lista de productos de la categoría
     */
    @Override
    public List<Product> getProductsByCategory(
            String category) {
        return products.stream()
                .filter(p -> p.getCategory()
                    .equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    /**
     * Actualiza cantidad de un producto.
     * 
     * @param productName Nombre del producto
     * @param newQuantity Nueva cantidad
     * @throws InvalidProductException si no existe
     */
    @Override
    public void updateProductQuantity(String productName, 
                                      int newQuantity) {
        validator.validateQuantity(newQuantity);
        
        Optional<Product> product = 
            findProductByName(productName);
        if (product.isPresent()) {
            product.get().setQuantity(newQuantity);
            System.out.println(
                "✓ Quantity updated: " + productName);
        } else {
            throw new InvalidProductException(
                "Product not found: " + productName);
        }
    }

    /**
     * Actualiza precio de un producto.
     * 
     * @param productName Nombre del producto
     * @param newPrice Nuevo precio
     * @throws InvalidProductException si no existe
     */
    @Override
    public void updateProductPrice(String productName, 
                                   double newPrice) {
        validator.validatePrice(newPrice);
        
        Optional<Product> product = 
            findProductByName(productName);
        if (product.isPresent()) {
            product.get().setPrice(newPrice);
            System.out.println(
                "✓ Price updated: " + productName);
        } else {
            throw new InvalidProductException(
                "Product not found: " + productName);
        }
    }

    /**
     * Obtiene el total de productos diferentes.
     * 
     * @return Número total de productos
     */
    @Override
    public int getTotalProductCount() {
        return products.size();
    }

    /**
     * Calcula valor total del inventario.
     * 
     * @return Suma de todos los valores
     */
    @Override
    public double calculateTotalInventoryValue() {
        return products.stream()
                .mapToDouble(Product::getTotalValue)
                .sum();
    }
}