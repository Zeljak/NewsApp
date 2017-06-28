package com.example.mara.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Mara on 28.6.2017..
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    //Tag for log messages
    private static final String LOG_TAG = NewsLoader.class.getName();

    //Query URL
    private String mUrl;

    /**
     * Constructs a new {@link NewsLoader}.
     *
     * @param context of the activity
     * @param url     to load data from server
     */
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        return QueryUtils.fetchNewsData(mUrl);
    }
}
