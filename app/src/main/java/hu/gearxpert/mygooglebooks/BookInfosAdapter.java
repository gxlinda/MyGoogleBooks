package hu.gearxpert.mygooglebooks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by melinda.kostenszki on 2017.05.24..
 */


public class BookInfosAdapter extends RecyclerView.Adapter<BookInfosAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView authorTextView;
        TextView titleTextView;
        TextView publishedTextView;
        TextView pagesCountTextView;
        ImageView smallThumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            authorTextView = (TextView) itemView.findViewById(R.id.author);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            publishedTextView = (TextView) itemView.findViewById(R.id.published);
            pagesCountTextView = (TextView) itemView.findViewById(R.id.pagesCount);
            smallThumbnail = (ImageView) itemView.findViewById(R.id.smallThumbnail);
        }
    }

    private List<BookInfos> mBookInfos;
    private Context mContext;

    /**
     * Constructs a new {@link BookInfosAdapter}.
     *
     * @param context of the app
     * @param books   is the list of books, which is the data source of the adapter
     */
    public BookInfosAdapter(Context context, List<BookInfos> books) {
        mBookInfos = books;
        mContext = context;
    }

    private Context getContext() {
        return mContext;
    }

    // Inflating a layout from XML and returning the holder
    @Override
    public BookInfosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View bookListItemView = inflater.inflate(R.layout.book_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(bookListItemView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(BookInfosAdapter.ViewHolder viewHolder, int position) {
        // Find the book at the given position in the list of books
        BookInfos currentBook = mBookInfos.get(position);

        // Find the ImageView in the layout with the ID smallThumbnail, and set the image with Picasso image loader library

        String url = currentBook.getThumbnailUrl();
        Picasso.with(mContext)
                .load(url)
                .placeholder(R.drawable.book_placeholder) //image to display while the url image is downloading
                .error(R.drawable.error_downloading)     //if some error has occurred in downloading the image, this image would be displayed
                .into(viewHolder.smallThumbnail);

        viewHolder.authorTextView.setText(currentBook.getAuthors());
        viewHolder.titleTextView.setText(currentBook.getTitle());
        viewHolder.publishedTextView.setText(currentBook.getPublisher() + ", " + currentBook.getPublishedDate());
        viewHolder.pagesCountTextView.setText("Pages: " + currentBook.getPageCount());
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mBookInfos.size();
    }
}
