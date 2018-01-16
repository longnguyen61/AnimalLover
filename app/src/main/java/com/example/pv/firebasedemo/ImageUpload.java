package com.example.pv.firebasedemo;

/**
 * Created by PV on 1/16/2018.
 */

public class ImageUpload {

    public String name;
    public String description;
    public String url;



    public ImageUpload(){

    }
    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public ImageUpload(String name, String url, String description) {
        this.name = name;
        this.url = url;
        this.description = description;

    }
}
