package hu.gearxpert.mygooglebooks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by melinda.kostenszki on 2017.05.24..
 *  * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */

public class BookInfosAdapter extends ArrayAdapter<BookInfos> {

    /**
     * Constructs a new {@link BookInfosAdapter}.
     *
     * @param context of the app
     * @param books is the list of books, which is the data source of the adapter
     */
    public BookInfosAdapter(Context context, List<BookInfos> books) {
        super(context, 0, books);
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
        }

        // Find the book at the given position in the list of books
        BookInfos currentBook = getItem(position);

        // Find the ImageView in the layout with the ID smallThumbnail, and set the image with Picasso image loader library
        ImageView smallThumbnail = (ImageView) listItemView.findViewById(R.id.smallThumbnail);
        String url = currentBook.getThumbnailUrl();
        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.book_placeholder) //image to display while the url image is downloading
                .error(R.drawable.error_downloading)      //if some error has occurred in downloading the image, this image would be displayed
                .into(smallThumbnail);

        // Find the TextView in the layout with the ID author, and set the value
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        authorTextView.setText(currentBook.getAuthors());

        // Find the TextView in the layout with the ID title, and set the value
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        titleTextView.setText(currentBook.getTitle());

        // Find the TextView in the layout with the ID published, and set the value
        TextView publishedTextView = (TextView) listItemView.findViewById(R.id.published);
        publishedTextView.setText(currentBook.getPublisher() + ", " + currentBook.getPublishedDate());

        // Find the TextView in the layout with the ID pagesCount, and set the value
        TextView pagesCountTextView = (TextView) listItemView.findViewById(R.id.pagesCount);
        pagesCountTextView.setText("Pages: " + currentBook.getPageCount());

        // Return the list item view that is now showing the appropriate data
        return listItemView;
    }
}
