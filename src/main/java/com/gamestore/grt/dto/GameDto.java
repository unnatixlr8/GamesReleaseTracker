package com.gamestore.grt.dto;

public class GameDto {
    private String title;
    private String storeUrl;
    private String dsDate;

    public GameDto(String title, String storeUrl){
        this.title = title;
        this.storeUrl = storeUrl;
    }
    public GameDto(String title, String storeUrl, String dsDate){
        this.title = title;
        this.storeUrl = storeUrl;
        this.dsDate = dsDate;
    }

    public String getTitle() {
        return title;
    }

    public String getStoreUrl() {
        return storeUrl;
    }

    public String getDsDate(){ return dsDate;}
}
