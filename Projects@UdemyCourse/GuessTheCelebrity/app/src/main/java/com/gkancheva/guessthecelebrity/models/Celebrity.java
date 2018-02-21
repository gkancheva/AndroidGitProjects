package com.gkancheva.guessthecelebrity.models;

public class Celebrity {
    private String name;
    private String urlPhoto;

    public Celebrity(String name, String url) {
        this.name = name;
        this.urlPhoto = url;
    }

    public String getName() {
        return name;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

}