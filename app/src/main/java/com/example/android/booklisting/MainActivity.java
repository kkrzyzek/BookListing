package com.example.android.booklisting;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    private String bookApiRequestUrl = "";
    private static final String LOG_TAG = MainActivity.class.getName();
    private BookAdapter mAdapter;
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_view);
        final TextView mEmptyText = (TextView) findViewById(R.id.no_items_text_view);

        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        ListView booksListView = (ListView) findViewById(R.id.books_list_view);
        booksListView.setAdapter(mAdapter);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Book currentBook = mAdapter.getItem(position);

                String url = currentBook.getCanonicalVolumeLink();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        final LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
        Log.i(LOG_TAG, "initLoader() method executed");

        final ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    mProgressBar.setVisibility(View.VISIBLE);

                    EditText searchForBooks = (EditText) findViewById(R.id.search_edit_text_view);
                    String phraseToSearch = searchForBooks.getText().toString();

                    if(!phraseToSearch.isEmpty() && phraseToSearch.trim().length() > 0) {
                        phraseToSearch = phraseToSearch.replace(" ", "+");
                        bookApiRequestUrl =
                                "https://www.googleapis.com/books/v1/volumes?q=&maxResults=15";
                        bookApiRequestUrl = new StringBuffer(bookApiRequestUrl)
                                .insert(bookApiRequestUrl.length()-14, phraseToSearch).toString();
                        mEmptyText.setVisibility(View.GONE);
                    } else {
                        bookApiRequestUrl="";
                        mAdapter.clear();
                        mEmptyText.setVisibility(View.VISIBLE);
                        mEmptyText.setText("No books to display.");
                    }
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mEmptyText.setText("No internet connection.");
                }

                getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                Log.i(LOG_TAG, "restartLoader() method within onClick method executed, url="+
                        bookApiRequestUrl);

//                FetchBooks task = new FetchBooks();
//                task.execute(newRequestUrl);
            }
        });

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "onCreateLoader() method executed.");
        return new BookLoader(this, bookApiRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();

        if (books != null && !books.isEmpty()) {
           mAdapter.addAll(books);
        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar_view);
        progressBar.setVisibility(View.GONE);

        Log.i(LOG_TAG, "onLoadFinished() method executed.");
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
        Log.i(LOG_TAG, "onLoaderReset() method executed");
    }

    //AsyncTask -> now Loader in use instead
//    private class FetchBooks extends AsyncTask<String, Void, List<Book>> {
//
//        private TextView noItemsTextView = (TextView) findViewById(R.id.no_items_text_view);
//
//        @Override
//        protected List<Book> doInBackground(String... urls) {
//            if(urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//            List<Book> result = QueryUtils.fetchBookData(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Book> books) {
//            mAdapter.clear();
//
//            if(books != null && !books.isEmpty()) {
//                mAdapter.addAll(books);
//
//                noItemsTextView.setVisibility(View.GONE);
//            }else {
//                noItemsTextView.setVisibility(View.VISIBLE);
//            }
//        }
//
//    }
}

