package hu.gearxpert.mygooglebooks;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by melinda.kostenszki on 2017.05.24..
 */

public class BooksLoader extends AsyncTaskLoader<List<BookInfos>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BooksLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link BooksLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public BooksLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<BookInfos> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        List<BookInfos> bookList = QueryUtils.fetchBookData(mUrl);
        return bookList;
    }
}