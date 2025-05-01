package com.gamestore.grt.dto;

public class GameDto {
    private String title;
    private String storeUrl;

    public GameDto(String title, String storeUrl){
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
