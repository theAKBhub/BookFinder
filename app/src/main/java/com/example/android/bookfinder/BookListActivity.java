package com.example.android.bookfinder;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import utilities.Utils;


public class BookListActivity extends AppCompatActivity implements LoaderCallbacks<List<Book>> {

    public static final String LOG_TAG = BookListActivity.class.getName();

    /** Constant value for the BookLoader ID */
    private static final int BOOK_LOADER_ID = 1;

    /** Google API URL */
    private static final String BOOKS_REQUEST_BASE_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int NUM_RESULTS_DEFAULT = 10;

    final Context mContext = this;
    private static String mBookTitleSearched;
    private static String mBookAuthorSearched;
    private BookAdapter mAdapter;
    private TextView mEmptyStateTextView;
    public static List<Book> mListBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        // Get Intent Extras
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            mBookTitleSearched = bundle.getString("bookTitle");
            mBookAuthorSearched = bundle.getString("bookAuthor");
        }

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list_books);

        // Set empty view
        mEmptyStateTextView = (TextView) findViewById(R.id.text_empty_list);
        Utils.setCustomTypeface(mContext, mEmptyStateTextView);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes the list of books as input
        mListBook = new ArrayList<Book>();
        mAdapter = new BookAdapter(this, mListBook);

        // Create a new adapter that takes the list of books as input
        bookListView.setAdapter(mAdapter);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Hide loading indicator and show empty state view
            View progressIndicator = findViewById(R.id.progress_indicator);
            mEmptyStateTextView.setText(R.string.error_no_connection);
        }

        // Set an OnClickListener on BookListView to launch BookDetailsActivity
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Intent intent = new Intent(mContext, BookDetailsActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String maxResults = sharedPrefs.getString(
                getString(R.string.settings_maxresults_key),
                getString(R.string.settings_maxresults_default)
        );

        Uri baseUri = Uri.parse(prepareSearchQuery());
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append parameters obtained fro, SharedPreferences
        uriBuilder.appendQueryParameter("maxResults", maxResults);
        uriBuilder.appendQueryParameter("orderBy", orderBy);

        return new BookLoader(mContext, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {

        // Hide progress indicator because the data has been loaded
        View progressIndicator = findViewById(R.id.progress_indicator);
        progressIndicator.setVisibility(View.GONE);

        // Set empty state text when no books found
        mEmptyStateTextView.setText(R.string.info_no_books);

        // Clear the adapter of previous book data
        mAdapter.clear();

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    /**
     * This method prepares the final query used to fetch data from Google API
     * @return search query
     */
    public String prepareSearchQuery() {

        StringBuilder stringBuilder = new StringBuilder(BOOKS_REQUEST_BASE_URL);
        stringBuilder.append("+intitle:").append(mBookTitleSearched);

        // Add author to query if entered by user
        if ((mBookAuthorSearched != null) && (mBookAuthorSearched.length() != 0)) {
            stringBuilder.append("+inauthor:").append(mBookAuthorSearched);
        }

        return(stringBuilder.toString());
    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("title", mBookTitleSearched);
            settingsIntent.putExtra("author", mBookAuthorSearched);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
