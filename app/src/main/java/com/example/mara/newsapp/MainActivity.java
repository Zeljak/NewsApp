package com.example.mara.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private final static String BASE_URL = "http://content.guardianapis.com/search?q=food&show-fields=thumbnail,trailText&order-date=published&api-key=test";
    private final static String BASE_URL_PAGE = "&page=";
    //Constant value for the news loader ID. We can choose an integer.
    private static final int NEWS_LOADER_ID = 1;
    private String mUrl = BASE_URL;
    private int pageNumber = 1;
    //Adapter for the list
    private NewsAdapter mAdapter;
    //TextView that displays when the list is empty
    private TextView mEmptyStateTextView;
    //Progress Bar when the page is loading
    private ProgressBar loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        loadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        final ListView newsListView = (ListView) findViewById(R.id.articles_list);
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setEmptyView(mEmptyStateTextView);
        //Set the adapter on the {@link ListView} so the list can be populated in the user interface
        newsListView.setAdapter(mAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find the current news that was clicked on
                News currentNews = mAdapter.getItem(position);
                //Convert the String URL into URI object (to pass into the Internet constructor)

                if (currentNews != null) {
                    Uri newsUri = Uri.parse(currentNews.getUrl());
                    //Create a new intent to view the news URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                    websiteIntent.setData(newsUri);

                    //Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }
            }

        });

        //Implementing scroll refresh
        newsListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount & mAdapter.getCount() > 0) {
                    pageNumber++;
                    mUrl = BASE_URL + BASE_URL_PAGE + pageNumber;

                    //Show loading indicator when the data is about to star loading
                    loadingIndicator.setVisibility(View.VISIBLE);
                    getNewsFromGuardianAPI();
                }

            }
        });
        getNewsFromGuardianAPI();
    }

    //Check internet connection and start loader
    private void getNewsFromGuardianAPI() {
        //Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            //Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_ID, null, this);
            //Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            //Otherwise, display error
            //First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mAdapter.clear();
            //Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        //Create a new loader for the given URL
        return new NewsLoader(this, mUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newses) {

        //Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        //Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_articles);
        // If there is a valid list of {@link news}, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newses != null && !newses.isEmpty()) {
            mAdapter.addAll(newses);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}