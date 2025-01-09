package org.example;

import java.util.Date;

public class ShortUrl {
    private final String url;
    private final String originalUrl;
    private int urlClickLimit;
    private int clickValue = 0;
    private Date dateCreate;

    public ShortUrl(String hashValue, String originalUrl, Date dateCreate, int urlClickLimit) {
        this.url = "http://short.url/" + hashValue;
        this.originalUrl = originalUrl;
        this.urlClickLimit = urlClickLimit;
        this.dateCreate = dateCreate;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getUrl() {
        return url;
    }

    public Date getDate() {
        return dateCreate;
    }

    public Boolean getClickValue() {
        return urlClickLimit - clickValue <= 0;
    }

    public void setUrlClickLimit(int urlClickLimit) {
        clickValue = 0;
        dateCreate = new Date();
        this.urlClickLimit = urlClickLimit;
    }

    public void setUrlClickValue() {
        clickValue += 1;
        System.out.println("Ссылка действует еще " + (urlClickLimit - clickValue) + " раз");
    }
}
