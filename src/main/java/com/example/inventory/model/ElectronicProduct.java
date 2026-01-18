package com.example.inventory.model;

public class ElectronicProduct extends Product {
    private final int warrantyMonths;
    private final String brand;

    public ElectronicProduct(String name, int quantity, double price, 
                            int warrantyMonths, String brand) {
        super(name, quantity, price, "Electronics");
        this.warrantyMonths = warrantyMonths;
        this.brand = brand;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    public String getBrand() {
        return brand;
    }

    @Override
    public String getProductInfo() {
        return String.format("%s, Brand: %s, Warranty: %d months", 
                           super.getProductInfo(), brand, warrantyMonths);
    }

    public boolean hasValidWarranty() {
        return warrantyMonths > 0;
    }
}