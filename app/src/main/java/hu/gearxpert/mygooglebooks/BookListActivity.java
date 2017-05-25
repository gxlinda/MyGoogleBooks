package hu.gearxpert.mygooglebooks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookInfos>> {

    public static final String LOG_TAG = BookListActivity.class.getName();

    /** search query entered by the user */
    public String query;

    private String GOOGLE_BOOKS_REQUEST_URL;

    public List<BookInfos> books;

    /** Adapter for the list of books */
    private BookInfosAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /**Constant value for the book loader ID. */
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        Intent intent = getIntent();
        query = intent.getStringExtra("KEYWORDS");
        GOOGLE_BOOKS_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=" + query + "&maxResults=30";

        RecyclerView bookListView = (RecyclerView) findViewById(R.id.list);
        // Initialize books
        // ???
        // Create a new adapter that takes an empty list of books as input
        mAdapter = new BookInfosAdapter(this, new ArrayList<BookInfos>());
        // Attach the adapter to the recyclerview to populate items
        bookListView.setAdapter(mAdapter);
        // Set layout manager to position the items
        bookListView.setLayoutManager(new LinearLayoutManager(this));


        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

//not working under RecyclerView?

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected book.
        bookListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current book that was clicked on
                BookInfos currentbook = mAdapter.get(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri bookUri = Uri.parse(currentbook.getInfoLinkUrl());

                // Create a new intent to view the book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.progress_bar);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<BookInfos>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new BooksLoader(this, GOOGLE_BOOKS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<BookInfos>> loader, List<BookInfos> books) {

        View circle = findViewById(R.id.progress_bar);
        circle.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        // Clear the adapter of previous book data
//        mAdapter.clear();

        // If there is a valid list of {@link BookInfo}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookInfos>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }

}


