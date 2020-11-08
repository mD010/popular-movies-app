package com.ydeng.popular_movies_app.movie_db.model;

public enum SortCriteria {
    POPULARITY("popularity", "popular"),
    TOP_RATED("top rated", "top_rated");

    private String value;
    private String subUrl;

    SortCriteria(String value, String subUrl) {
        this.value = value;
        this.subUrl = subUrl;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSubUrl() {
        return subUrl;
    }

    public void setSubUrl(String subUrl) {
        this.subUrl = subUrl;
    }
}
