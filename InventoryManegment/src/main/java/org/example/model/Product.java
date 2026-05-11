package org.example.model;

public class Product {

    private int name;
    private String seller;
    private int count;


    public Product(int name, String seller, int count) {
        this.name = name;
        this.seller = seller;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count += count;
    }
}
