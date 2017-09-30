package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by user on 2017-09-28.
 */

public class BookLoader extends AsyncTaskLoader<List<Book>> {

    private static final String LOG_TAG = BookLoader.class.getName();
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.i(LOG_TAG, "onStartLoading() method executed");
    }

    @Override
    public List<Book> loadInBackground() {
        Log.i(LOG_TAG, "loadInBackground() method executed");
        if(mUrl == null || mUrl.isEmpty()) {
            return null;
        }
        List<Book> result = QueryUtils.fetchBookData(mUrl);
        return result;
    }
}
