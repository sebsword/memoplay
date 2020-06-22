package com.example.sebsw.memoplay.model;

/**
 * Created by sebsw on 06/11/2016.
 */

public class Image {
    int id;
    String imageName;
    String pathName;
    String tags;

    //class constructor generates image from ID, name, path and Image tags
    public Image(int id, String imageName, String pathName, String tags) {
        this.id = id;
        this.imageName = imageName;
        this.pathName = pathName;
        this.tags = tags;
    }

}