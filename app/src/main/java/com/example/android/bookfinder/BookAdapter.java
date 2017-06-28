package com.example.android.bookfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import utilities.Utils;

/**
 * A {@link BookAdapter} will create a list item layout for each book
 * in the data source (a list of {@link Book} objects) to be displayed in a ListView
 */

public class BookAdapter extends ArrayAdapter<Book> {

    private static final String LOG_TAG = BookAdapter.class.getName();
    private static final String CURRENCY_GBP = "GBP";
    private static final String CURRENCY_USD = "USD";
    private static final String CURRENCY_EUR = "EUR";

    private static Context mContext;

    /**
     * Constructor to create a new {@link BookAdapter} object
     * @param context
     * @param books
     */
    public BookAdapter(Context context, List<Book> books) {
        super(context, 0, books);
        mContext = context;
    }

    /**
     * This class describes the view items to create a list item
     */
    public static class BookViewHolder {

        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewPrice;
        ImageView imageViewRating;
        ImageView imageViewBook;

        // Find various views within ListView and set custom typeface on them
        public BookViewHolder(View itemView) {
            textViewTitle = (TextView) itemView.findViewById(R.id.text_book_title);
            textViewAuthor = (TextView) itemView.findViewById(R.id.text_book_author);
            textViewPrice = (TextView) itemView.findViewById(R.id.text_book_price);
            imageViewRating = (ImageView) itemView.findViewById(R.id.image_book_rating);
            imageViewBook = (ImageView) itemView.findViewById(R.id.image_book);

            Utils.setCustomTypeface(mContext, textViewTitle);
            Utils.setCustomTypeface(mContext, textViewAuthor);
            Utils.setCustomTypeface(mContext, textViewPrice);
        }
    }

    /**
     * Returns a list item view that displays information about the book at the given position
     * in the list of books.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        double price = 0;
        String image = "";
        String bookPrice = "";
        String ratingsImage = "";
        BookViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list_item, parent, false);
            holder = new BookViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (BookViewHolder) convertView.getTag();
        }

        // Find book at the given position in the list
        Book currentBook = getItem(position);


        /** Set data to respective views within ListView */

        // Set Title
        holder.textViewTitle.setText(currentBook.getTitle());

        // Set Author
        holder.textViewAuthor.setText(currentBook.getAuthor());

        // Set Price and Currency Code if available, else set default value
        price = currentBook.getRetailPrice();
        if (price != 0.00) {
            switch (currentBook.getCurrencyCode()) {
                case CURRENCY_GBP:
                    bookPrice = (getContext().getString(R.string.label_gbp, price));
                    break;
                case CURRENCY_USD:
                    bookPrice = (getContext().getString(R.string.label_usd, price));
                    break;
                case CURRENCY_EUR:
                    bookPrice = (getContext().getString(R.string.label_euro, price));
                    break;
            }
        } else {
            bookPrice = getContext().getString(R.string.info_not_priced);
        }
        holder.textViewPrice.setText(bookPrice);

        // Set ratings icons
        ratingsImage = getRatingImageResource(currentBook.getRating());
        int resId = mContext.getResources().getIdentifier(ratingsImage, "drawable", mContext.getPackageName());
        holder.imageViewRating.setImageResource(resId);

        // Set Image if available
        image = currentBook.getThumbnailLink();
        if (image != null && image.length() > 0) {
            Picasso.with(getContext()).load(currentBook.getThumbnailLink()).into(holder.imageViewBook);
        } else {
            Picasso.with(getContext()).load(R.drawable.image_not_found).into(holder.imageViewBook);
        }

        return convertView;
    }

    /**
     * This method gets rating resource id depending on the book's rating
     * @param rating
     * @return resource name
     */
    private String getRatingImageResource(double rating) {
        String ratingResource;

        switch (String.valueOf(rating)) {
            case "0.5":
                ratingResource = getContext().getString(R.string.rating_half);
                break;
            case "1.0":
                ratingResource = getContext().getString(R.string.rating_one);
                break;
            case "1.5":
                ratingResource = getContext().getString(R.string.rating_one_half);
                break;
            case "2.0":
                ratingResource = getContext().getString(R.string.rating_two);
                break;
            case "2.5":
                ratingResource = getContext().getString(R.string.rating_two_half);
                break;
            case "3.0":
                ratingResource = getContext().getString(R.string.rating_three);
                break;
            case "3.5":
                ratingResource = getContext().getString(R.string.rating_three_half);
                break;
            case "4.0":
                ratingResource = getContext().getString(R.string.rating_four);
                break;
            case "4.5":
                ratingResource = getContext().getString(R.string.rating_four_half);
                break;
            case "5.0":
                ratingResource = getContext().getString(R.string.rating_five);
                break;
            default:
                ratingResource = getContext().getString(R.string.rating_none);
                break;
        }

        return ratingResource;
    }
}
