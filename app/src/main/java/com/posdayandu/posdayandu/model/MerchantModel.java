package com.posdayandu.posdayandu.model;

public class MerchantModel {
    String url, token;

    public MerchantModel() {
    }

    public MerchantModel(String url, String token) {
        this.url = url;
        this.token = token;
    }

    @Override
    public String toString() {
        return "MerchantModel{" +
                "url='" + url + '\'' +
                ", token='" + token + '\'' +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
