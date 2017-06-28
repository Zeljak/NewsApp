package com.example.mara.newsapp;

/**
 * Created by Mara on 28.6.2017..
 */

public class News {
    private String mTitle;
    private String mUrl;
    private String mSection;

    public News(String Title, String url, String section) {
        mTitle = Title;
        mUrl = url;
        mSection = section;
    }

    //Returns the title of the article
    public String getTitle() {
        return mTitle;
    }

    //Returns the Url
    public String getUrl() {
        return mUrl;
    }

    //Returns the section
    public String getSection() {
        return mSection;
    }


}
