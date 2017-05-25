package hu.gearxpert.mygooglebooks;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by melinda.kostenszki on 2017.05.24..
 *
 * Helper class related to requesting and receiving data of books from Google Books API
 */

public final class QueryUtils {

    /** Tag for the log messages */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Google Books API dataset and return a list of {@link BookInfos} objects.
     */
    public static List<BookInfos> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link BookInfo}s
        List<BookInfos> books = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link BookInfo}s
        return books;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl)  {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //return early if url is null
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "HTTP error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retreiving the book JSON results.", e);

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) { //inputStream represents a stream of bytes
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // helps us read text from an inputStream
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link BookInfos} objects that has been built up from
     * parsing the given JSON response.
     */
    public static List<BookInfos> extractFeatureFromJson(String bookJSON) {

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        List<BookInfos> books = new ArrayList<>();

        // Try to parse the  JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(bookJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of volumes (= books).
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");

            for(int i = 0; i<itemsArray.length(); i++) {
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                String title = "(no title)";
                if (volumeInfo.has("title")) {
                    title = volumeInfo.getString("title");
                }

                StringBuilder authorList = new StringBuilder();
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    authorList.append(authors.getString(0));
                    for (int j = 1; j < authors.length(); j++) {
                        authorList.append(", " + authors.getString(j)); //if there is more than 1 author, we list them all, devided by commas
                    }
                } else {
                    authorList.append("(unknown author)");
                }

                String publisher = "(unknown publisher)";
                if (volumeInfo.has("publisher")) {
                    publisher = volumeInfo.getString("publisher");
                }

                String publishedDate = "(no date available)";
                if (volumeInfo.has("publishedDate")) {
                    publishedDate = volumeInfo.getString("publishedDate");
                }

                String pageCount = "unknown";
                if (volumeInfo.has("pageCount")) {
                    pageCount = volumeInfo.getString("pageCount");
                }

                String infoLinkUrl = volumeInfo.getString("infoLink");
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");

                String smallThumbnailUrl = "";
                if (imageLinks.has("smallThumbnail")) {
                    smallThumbnailUrl = imageLinks.getString("smallThumbnail");
                }

                BookInfos actualBook = new BookInfos(title, authorList.toString(), publisher, publishedDate, pageCount, smallThumbnailUrl, infoLinkUrl);
                books.add(actualBook);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

}