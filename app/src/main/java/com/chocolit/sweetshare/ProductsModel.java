package com.chocolit.sweetshare;

import android.net.Uri;

import java.util.ArrayList;

public class ProductsModel {


    private String PRODUCT_TITLE;
    private long PRICE;
    private String PRODUCT_CATEGORY;
    private String PRODUCT_CITY;
    private String PRODUCT_DESCRIPTION;
    private ArrayList<String> PRODUCT_IMG_LIST;
    private String ID;

    private ProductsModel(){}

    private ProductsModel(String ID, String PRODUCT_TITLE, long PRICE, String PRODUCT_CATEGORY, String PRODUCT_CITY, ArrayList<String> PRODUCT_IMG_LIST, String PRODUCT_DESCRIPTION){
        this.PRODUCT_TITLE = PRODUCT_TITLE;
        this.PRICE = PRICE;
        this.PRODUCT_CATEGORY = PRODUCT_CATEGORY;
        this.PRODUCT_CITY = PRODUCT_CITY;
        this.PRODUCT_IMG_LIST = PRODUCT_IMG_LIST;
        this.PRODUCT_DESCRIPTION = PRODUCT_DESCRIPTION;
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public long getPRICE() {
        return PRICE;
    }

    public void setPRICE(long PRICE) {
        this.PRICE = PRICE;
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

    public ArrayList<String> getPRODUCT_IMG_LIST() {
        return PRODUCT_IMG_LIST;
    }

    public void setPRODUCT_IMG_LIST(ArrayList<String> PRODUCT_IMG_LIST) {
        this.PRODUCT_IMG_LIST = PRODUCT_IMG_LIST;
    }

    public String getPRODUCT_DESCRIPTION() {
        return PRODUCT_DESCRIPTION;
    }

    public void setPRODUCT_DESCRIPTION(String PRODUCT_DESCRIPTION) {
        this.PRODUCT_DESCRIPTION = PRODUCT_DESCRIPTION;
    }

}
