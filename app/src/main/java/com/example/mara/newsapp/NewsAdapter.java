package com.example.mara.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Mara on 28.6.2017..
 */

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * Returns a list item view that displays information about the article at the given position
     * in the list of articles.
     */

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        News currentNews = getItem(position);

        //Title of the article
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.article_title);
        nameTextView.setText(currentNews.getTitle());

        //Section
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section_name);
        sectionTextView.setText(currentNews.getSection());

        return listItemView;
    }


}



