package com.limpag.agrospread;

public class Product {
    private String name;
    private String price;
    private String type;
    private String description;
    private String imageUri;

    public Product(String name, String price, String type, String description, String imageUri) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.description = description;
        this.imageUri = imageUri;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUri() {
        return imageUri;
    }
}
