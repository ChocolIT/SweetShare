package com.chocolit.sweetshare;

public class ProductsModel {


    private String PRODUCT_TITLE;
    private long PRODUCT_PRICE;
    private String PRODUCT_CATEGORY;
    private String PRODUCT_CITY;

    private ProductsModel(){

    }
    private ProductsModel(String PRODUCT_TITLE, long PRODUCT_PRICE, String PRODUCT_CATEGORY, String PRODUCT_CITY){
        this.PRODUCT_TITLE = PRODUCT_TITLE;
        this.PRODUCT_PRICE = PRODUCT_PRICE;
        this.PRODUCT_CATEGORY = PRODUCT_CATEGORY;
        this.PRODUCT_CITY = PRODUCT_CITY;
    }

    public long getPRODUCT_PRICE() {
        return PRODUCT_PRICE;
    }

    public void setPRODUCT_PRICE(long PRODUCT_PRICE) {
        this.PRODUCT_PRICE = PRODUCT_PRICE;
    }

    public String getPRODUCT_CATEGORY() {
        return PRODUCT_CATEGORY;
    }

    public void setPRODUCT_CATEGORY(String PRODUCT_CATEGORY) {
        this.PRODUCT_CATEGORY = PRODUCT_CATEGORY;
    }

    public String getPRODUCT_CITY() {
        return PRODUCT_CITY;
    }

    public void setPRODUCT_CITY(String PRODUCT_CITY) {
        this.PRODUCT_CITY = PRODUCT_CITY;
    }

    public String getPRODUCT_TITLE() {
        return PRODUCT_TITLE;
    }

    public void setPRODUCT_TITLE(String PRODUCT_TITLE) {
        this.PRODUCT_TITLE = PRODUCT_TITLE;
    }
}
