package com.example.inventory.model;

/**
 * Representa un producto en el inventario.
 * Clase base para todos los tipos de productos.
 * 
 * @author Inventory Team
 * @version 1.0
 */
public class Product {
    private final String name;
    private int quantity;
    private double price;
    private final String category;

    /**
     * Constructor para crear un nuevo producto.
     * 
     * @param name Nombre del producto
     * @param quantity Cantidad en inventario
     * @param price Precio unitario del producto
     * @param category Categoría del producto
     */
    public Product(String name, int quantity, 
                   double price, String category) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.category = category;
    }

    /**
     * Obtiene el nombre del producto.
     * 
     * @return Nombre del producto
     */
    public String getName() {
        return name;
    }

    /**
     * Obtiene la cantidad disponible.
     * 
     * @return Cantidad en inventario
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Establece una nueva cantidad.
     * 
     * @param quantity Nueva cantidad
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Obtiene el precio unitario.
     * 
     * @return Precio del producto
     */
    public double getPrice() {
        return price;
    }

    /**
     * Establece un nuevo precio.
     * 
     * @param price Nuevo precio
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Obtiene la categoría.
     * 
     * @return Categoría del producto
     */
    public String getCategory() {
        return category;
    }

    /**
     * Calcula el valor total del inventario.
     * 
     * @return Cantidad multiplicada por precio
     */
    public double getTotalValue() {
        return quantity * price;
    }

    /**
     * Genera información formateada del producto.
     * 
     * @return String con información del producto
     */
    public String getProductInfo() {
        return String.format(
            "Product: %s [%s], Quantity: %d, Price: $%.2f", 
            name, category, quantity, price);
    }

    /**
     * Representación en String del producto.
     * 
     * @return Información del producto
     */
    @Override
    public String toString() {
        return getProductInfo();
    }
}