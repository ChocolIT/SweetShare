package com.chocolit.sweetshare;

public class UserOrdersModel {
    private long START_DATE, END_DATE;
    private String FIRST_IMAGE;
    private String PRODUCT_TITLE;
    private String CONTACT_PHONE_NUMBER;

    UserOrdersModel (){};

    public UserOrdersModel(long START_DATE, long END_DATE, String FIRST_IMAGE, String PRODUCT_TITLE, String CONTACT_PHONE_NUMBER) {
        this.START_DATE = START_DATE;
        this.END_DATE = END_DATE;
        this.FIRST_IMAGE = FIRST_IMAGE;
        this.PRODUCT_TITLE = PRODUCT_TITLE;
    }

    public long getSTART_DATE() {
        return START_DATE;
    }

    public void setSTART_DATE(long START_DATE) {
        this.START_DATE = START_DATE;
    }

    public long getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(long END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getFIRST_IMAGE() {
        return FIRST_IMAGE;
    }

    public void setFIRST_IMAGE(String FIRST_IMAGE) {
        this.FIRST_IMAGE = FIRST_IMAGE;
    }

    public String getPRODUCT_TITLE() {
        return PRODUCT_TITLE;
    }

    public void setPRODUCT_TITLE(String PRODUCT_TITLE) {
        this.PRODUCT_TITLE = PRODUCT_TITLE;
    }

    public String getCONTACT_PHONE_NUMBER() {
        return CONTACT_PHONE_NUMBER;
    }

    public void setCONTACT_PHONE_NUMBER(String CONTACT_PHONE_NUMBER) {
        this.CONTACT_PHONE_NUMBER = CONTACT_PHONE_NUMBER;
    }
}
