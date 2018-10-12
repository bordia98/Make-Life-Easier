package com.example.bordia98.retailsking;

//class for the product object to store the details
public class product {

    private String product_title;
    private int product_qty;
    private float product_price;

    public product(String product_title,int product_qty,float product_price){
        this.product_title=product_title;
        this.product_qty=product_qty;
        this.product_price=product_price;
    }

    public String getProduct_title() {
        return product_title;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public float getProduct_price() {
        return product_price;
    }
}
