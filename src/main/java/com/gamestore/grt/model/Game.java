package com.gamestore.grt.model;

public class Game {
    private String title;
    private String storeUrl;

    public Game(String title, String storeUrl){
        this.title = title;
        this.storeUrl = storeUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getStoreUrl() {
        return storeUrl;
    }
}
