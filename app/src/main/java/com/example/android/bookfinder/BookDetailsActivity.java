package com.example.android.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import utilities.Utils;


public class BookDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = MainActivity.class.getName();

    final Context mContext = this;

    // UI Components
    private ImageView mImageThumbnail;
    private TextView mTextTitle;
    private TextView mTextAuthor;
    private TextView mTextRating;
    private TextView mTextPrice;
    private TextView mTextDetails;
    private TextView mTextDesc;
    private Button mButtonBuy;
    private Button mButtonPreview;

    private Book mSelectedBook;
    private String mBuyingLink;
    private String mPreviewLink;
    private String mPublishedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        int listPosition;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        /** Get Intent Extras */
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            listPosition = bundle.getInt("position");

            // Get the book that was clicked on in Book List
            mSelectedBook = BookListActivity.mListBook.get(listPosition);
        }

        /** Initialize UI components */
        mImageThumbnail = (ImageView) findViewById(R.id.image_thumbnail);
        mTextTitle = (TextView) findViewById(R.id.text_title);
        mTextAuthor = (TextView) findViewById(R.id.text_author);
        mTextRating = (TextView) findViewById(R.id.text_rating);
        mTextPrice = (TextView) findViewById(R.id.text_price);
        mTextDetails = (TextView) findViewById(R.id.text_details);
        mTextDesc = (TextView) findViewById(R.id.text_desc);
        mButtonBuy = (Button) findViewById(R.id.button_buy_book);
        mButtonPreview = (Button) findViewById(R.id.button_preview_book);

        /** Set custom font on views */
        setCustomTypeface();

        /** Set OnClickListner on button view */
        mButtonBuy.setOnClickListener(this);
        mButtonPreview.setOnClickListener(this);

        /** Display Book Details */
        displayBookDetails();

    }

    /**
     * This method sets custom font for all views
     */
    public void setCustomTypeface() {
        Utils.setCustomTypeface(mContext, mTextTitle);
        Utils.setCustomTypeface(mContext, mTextAuthor);
        Utils.setCustomTypeface(mContext, mTextRating);
        Utils.setCustomTypeface(mContext, mTextPrice);
        Utils.setCustomTypeface(mContext, mTextDetails);
        Utils.setCustomTypeface(mContext, mTextDesc);
        Utils.setCustomTypeface(mContext, mButtonBuy);
    }

    /**
     * Invokes methods for individual call to action buttons
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_buy_book:
                launchBuyingLink();
                break;
            case R.id.button_preview_book:
                launchPreviewLink();
                break;
        }
    }

    /**
     * This method invokes individual methods to populate various views on the screen
     */
    public void displayBookDetails() {
        displayImage();         // Display image of the book
        displayTitle();         // Display book title
        displayAuthor();        // Display author of the book
        displayRating();        // Display average rating of the book
        displayPrice();         // Display price of the book
        setBuyingLink();        // Set buying link for button
        setPreviewLink();       // Set preview link for button
        formatDate();           // Get formatted Published Date
        displayDetails();       // Display book details
        displayDescription();   // Display book description
    }

    /**
     * Method to display book image using Picasso library
     */
    public void displayImage() {
        String image = mSelectedBook.getThumbnailLink();

        if (image != null && image.length() > 0) {
            Picasso.with(mContext).load(image).into(mImageThumbnail);
        } else {
            Picasso.with(mContext).load(R.drawable.image_not_found).into(mImageThumbnail);
        }
    }

    /**
     * Method to display Book Title
     */
    public void displayTitle() {
        mTextTitle.setText(mSelectedBook.getTitle());
    }

    /**
     * Method to display Author
     */
    public void displayAuthor() {
        String author = mSelectedBook.getAuthor();

        if (author != null && author.length() > 0) {
            mTextAuthor.setText(author);
        } else {
            mTextAuthor.setText(getString(R.string.info_no_author));
        }
    }

    /**
     * Method to display Rating
     */
    public void displayRating() {
        double rating = mSelectedBook.getRating();
        String output;

        if (rating != 0.00) {
            output = String.format(getString(R.string.label_rating), rating);
        } else {
            output = getString(R.string.info_no_rating);
        }

        mTextRating.setText(output);
    }

    /**
     * Method to display Price
     */
    public void displayPrice() {
        double price = mSelectedBook.getRetailPrice();
        String retailPrice;

        if (price != 0.00) {
            retailPrice = mSelectedBook.getCurrencyCode() + " " + price;
        } else {
            retailPrice = getString(R.string.info_no_price);
        }

        mTextPrice.setText(retailPrice);
    }

    /**
     * Method to set buying link
     */
    public void setBuyingLink() {
        mBuyingLink = mSelectedBook.getBuyingLink();

        if (mBuyingLink != null && mBuyingLink.length() == 0) {
            mButtonBuy.setVisibility(View.GONE);
        }
    }

    /**
     * Method to set preview link
     */
    public void setPreviewLink() {
        mPreviewLink = mSelectedBook.getPreviewlLink();

        if (mPreviewLink != null && mPreviewLink.length() == 0) {
            mButtonPreview.setVisibility(View.GONE);
        }
    }

    /**
     * Method to open website using buying link
     */
    public void launchBuyingLink() {
        Uri bookBuyingURL = Uri.parse(mBuyingLink);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, bookBuyingURL);
        startActivity(webIntent);
    }

    /**
     * Method to open website using preview link
     */
    public void launchPreviewLink() {
        Uri bookPreviewURL = Uri.parse(mPreviewLink);
        Intent webIntent = new Intent(Intent.ACTION_VIEW, bookPreviewURL);
        startActivity(webIntent);
    }

    /**
     * Method to check if Published Date exists, then formats it if the extracted date is a valid date
     */
    public void formatDate() {
        String date = mSelectedBook.getPublishedDate();
        String dateNew = "";
        mPublishedDate = "";

        if (date != null && date.length() != 0) {
            if (date.length() == 4 || date.length() == 7 || date.length() == 10 ) { // check if date is YYYY or YYYY-MM or YYYY-MM-DD
                dateNew = date;
            } else if (date.length() > 10) {
                dateNew = date.substring(0, 10);
            }

            // Format dateNew
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat newFormat = new SimpleDateFormat("MMM yyyy");
            try {
                Date dt = inputFormat.parse(dateNew);
                mPublishedDate = newFormat.format(dt);
            }
            catch(ParseException pe) {
                Log.e(LOG_TAG, mContext.getString(R.string.exception_date_format), pe);
            }
        }
    }

    /**
     * Method to display book details
     */
    public void displayDetails() {
        String category;
        String printType;
        String language;
        int pageCount;

        StringBuilder sbDetails = new StringBuilder();

        // Add Published Date
        if (mPublishedDate != null && mPublishedDate.length() > 0) {
            sbDetails.append(String.format(getString(R.string.label_published), mPublishedDate));
            sbDetails.append(getString(R.string.newline));
        }

        // Add category
        category = mSelectedBook.getCategories();
        if (category != null && category.length() > 0) {
            sbDetails.append(String.format(getString(R.string.label_category), category));
            sbDetails.append(getString(R.string.newline));
        }

        // Add Print Type
        printType = mSelectedBook.getPrintType();
        if (printType != null && printType.length() > 0) {
            sbDetails.append(String.format(getString(R.string.label_print_type), printType));
            sbDetails.append(getString(R.string.newline));
        }

        // Add Language
        if ((mSelectedBook.getLanguage() != null) && (mSelectedBook.getLanguage().length() > 0)) {
            Locale locale = new Locale(mSelectedBook.getLanguage());
            language = locale.getDisplayLanguage(locale);
            sbDetails.append(String.format(getString(R.string.label_language), language));
            sbDetails.append(getString(R.string.newline));
        }

        // Add Page Count
        pageCount = mSelectedBook.getPageCount();
        if (pageCount != 0) {
            sbDetails.append(String.format(getString(R.string.label_page_count), pageCount));
            sbDetails.append(getString(R.string.newline));
        }

        // Add EPUB Tag
        if (mSelectedBook.isTagEpub()) {
            sbDetails.append(getString(R.string.label_epub)).append(getString(R.string.label_avail));
            sbDetails.append(getString(R.string.newline));
        } else {
            sbDetails.append(getString(R.string.label_epub)).append(getString(R.string.label_noavail));
            sbDetails.append(getString(R.string.newline));
        }

        // Add PDF Tag
        if (mSelectedBook.isTagPdf()) {
            sbDetails.append(getString(R.string.label_pdf)).append(getString(R.string.label_avail));
            sbDetails.append(getString(R.string.newline));
        } else {
            sbDetails.append(getString(R.string.label_pdf)).append(getString(R.string.label_noavail));
            sbDetails.append(getString(R.string.newline));
        }

        mTextDetails.setText(sbDetails.toString());
    }

    /**
     * Method to display Book description
     */
    public void displayDescription() {
        String description = mSelectedBook.getDescription();
        if (description != null && description.length() > 0) {
            mTextDesc.setText(description);
        } else {
            mTextDesc.setText(getString(R.string.info_no_desc));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
