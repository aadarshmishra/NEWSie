package com.example.aadarsh.newsie;

/**
 * Created by Aadarsh on 3/24/2018.
 */

public class News {

    private String title,publisher,time,imgURL,URL;

    public News(String title, String publisher, String time, String imgURL, String URL) {
        this.title = title;
        this.publisher = publisher;
        this.time = time;
        this.imgURL = imgURL;
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
