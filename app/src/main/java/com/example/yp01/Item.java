package com.example.yp01;

public class Item {
    private String title;
    private String price;
    private int imageResource;

    public Item(String title, String price, int imageResource) {
        this.title = title;
        this.price = price;
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public int getImageResource() {
        return imageResource;
    }


}
