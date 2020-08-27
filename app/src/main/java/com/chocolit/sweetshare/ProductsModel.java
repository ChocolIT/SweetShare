package com.chocolit.sweetshare;

public class ProductsModel {


    private String name;
    private long price;
    private String categoryName;
    private String city;

    private ProductsModel(){

    }
    private ProductsModel(String name, long price, String categoryName, String city){
        this.name = name;
        this.price = price;
        this.categoryName = categoryName;
        this.city = city;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
