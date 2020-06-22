package com.example.sebsw.memoplay.model;

/**
 * Created by sebsw on 27/10/2016.
 */

public class Category {
    final String DRAWABLE = "drawable/";
    private String categoryTitle;
    private String imgUri;

    //class constructor, generatees category from title and an image uri
    public Category(String categoryTitle, String imgUri) {
        this.categoryTitle = categoryTitle;
        this.imgUri = imgUri;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public String getImgUri() {
        return DRAWABLE + imgUri;
    }

}
