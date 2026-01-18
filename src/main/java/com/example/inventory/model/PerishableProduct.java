package com.example.inventory.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PerishableProduct extends Product {
    private final LocalDate expirationDate;

    public PerishableProduct(String name, int quantity, double price, 
                            LocalDate expirationDate) {
        super(name, quantity, price, "Perishable");
        this.expirationDate = expirationDate;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public long getDaysUntilExpiration() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expirationDate);
    }

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    @Override
    public String getProductInfo() {
        String status = isExpired() ? "EXPIRED" : 
                       getDaysUntilExpiration() < 7 ? "EXPIRING SOON" : "Fresh";
        return String.format("%s, Expiration: %s [%s]", 
                           super.getProductInfo(), expirationDate, status);
    }
}