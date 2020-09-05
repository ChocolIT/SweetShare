package com.chocolit.sweetshare;

import android.util.Log;

public class ReviewsModel {
    private String REVIEWER_NAME;
    private String REVIEW_CONTENT;
    private long RATING;

    private ReviewsModel() {}

    private ReviewsModel (String REVIEWER_NAME, String REVIEW_CONTENT, long RATING) {
        this.REVIEWER_NAME = REVIEWER_NAME;
        this.REVIEW_CONTENT = REVIEW_CONTENT;
        this.RATING = RATING;
    }

    public String getREVIEWER_NAME() {
        return REVIEWER_NAME;
    }

    public void setREVIEWER_NAME(String REVIEWER_NAME) {
        this.REVIEWER_NAME = REVIEWER_NAME;
    }

    public String getREVIEW_CONTENT() {
        return REVIEW_CONTENT;
    }

    public void setREVIEW_CONTENT(String REVIEW_CONTENT) {
        this.REVIEW_CONTENT = REVIEW_CONTENT;
    }

    public long getRATING() {
        return RATING;
    }

    public void setRATING(long RATING) {
        this.RATING = RATING;
    }
}
