package com.example.lab8.model;

public class Product {
    private String name;
    private int price;
    private String productID;
    private int quantity;

    public Product() {
    }

    public Product(String name, int price, String productID, int quantity) {
        this.name = name;
        this.price = price;
        this.productID = productID;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public String getProductID() { return productID; }
    public void setProductID(String productID) { this.productID = productID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", productID='" + productID + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
