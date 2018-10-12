package com.example.bordia98.retailsking;

public class billing_product {
    private String product_title;
    private int product_qty;
    private float product_price;
    public String product_barcode;

    public billing_product(String product_title,int product_qty,float product_price,String productbarcode){
        this.product_title=product_title;
        this.product_qty=product_qty;
        this.product_price=product_price;
        this.product_barcode = productbarcode;
    }

    public String getProduct_title() {
        return product_title;
    }

    public void setProduct_qty(int product_qty) {
        this.product_qty = product_qty;
    }

    public int getProduct_qty() {
        return product_qty;
    }

    public float getProduct_price() {
        return product_price;
    }

    public String getProduct_barcode(){
        return product_barcode;
    }
}
