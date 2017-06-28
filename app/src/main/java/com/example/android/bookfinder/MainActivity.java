package com.example.android.bookfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import utilities.Utils;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String LOG_TAG = MainActivity.class.getName();

    final Context mContext = this;
    private String mBookTitle;
    private String mBookAuthor;

    // UI Components
    private TextView mTextSearchInfo;
    private EditText mEditBookTitle;
    private EditText mEditBookAuthor;
    private Button mButtonSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Initialize UI components */
        mTextSearchInfo = (TextView) findViewById(R.id.text_search_info);
        mEditBookTitle = (EditText) findViewById(R.id.edit_book_title);
        mEditBookAuthor = (EditText) findViewById(R.id.edit_book_author);
        mButtonSearch = (Button) findViewById(R.id.button_search_books);

        /** Set custom font on views */
        setCustomTypeface();

        /** Set OnClickListner on button view */
        mButtonSearch.setOnClickListener(this);

        /** Add TextChangeListener to EditText fields */
        mEditBookTitle.addTextChangedListener(new QuizTextWatcher(mEditBookTitle));
        mEditBookAuthor.addTextChangedListener(new QuizTextWatcher(mEditBookAuthor));

    }

    /**
     * This method sets custom font for all views
     */
    public void setCustomTypeface() {
        Utils.setCustomTypeface(mContext, mTextSearchInfo);
        Utils.setCustomTypeface(mContext, mEditBookTitle);
        Utils.setCustomTypeface(mContext, mEditBookAuthor);
        Utils.setCustomTypeface(mContext, mButtonSearch);
    }

    /**
     * Invokes methods for individual call to action buttons
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_search_books:
                showSearchResults();
                break;
        }
    }

    /**
     * This method launches BookListActivity after validating Book Title and Author inputs
     */
    public void showSearchResults() {
        if (validateInput()) {
            Intent intent = new Intent(mContext, BookListActivity.class);

            // Replace spaces with + sign for URL to be used to fetch data
            intent.putExtra("bookTitle", mBookTitle.replaceAll(" ", "+"));
            intent.putExtra("bookAuthor", mBookAuthor.replaceAll(" ", "+"));
            startActivity(intent);
        }
    }

    /**
     * This method checks if an input string contains number or invalid characters.
     */
    public boolean validateInput() {
        mBookTitle = mEditBookTitle.getText().toString().trim();
        mBookAuthor = mEditBookAuthor.getText().toString().trim();

        /** Check if Book Title is entered; else set error */
        if (!Utils.checkEmptyString(mBookTitle)) {
            mEditBookTitle.setBackgroundResource(R.color.colorError);
            mEditBookTitle.setError(getString(R.string.error_empty_string));
            return false;
        }

        /** Check if Book Author is valid if entered; it is an optional field */
        if (Utils.checkEmptyString(mBookAuthor)) {
            // Book Author not empty so check if input is valid
            if (!Utils.checkValidString(mBookAuthor)) {
                mEditBookAuthor.setBackgroundResource(R.color.colorError);
                mEditBookAuthor.setError(getString(R.string.error_invalid_string));
                return false;
            }
        }

        return true;
    }

    /**
     * Extends TextWatcher class for EditText fields
     */
    private class QuizTextWatcher implements TextWatcher {
        private View view;

        private QuizTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            mEditBookTitle.setBackgroundResource(R.color.colorLatte);
            mEditBookAuthor.setBackgroundResource(R.color.colorLatte);
        }

        public void afterTextChanged(Editable editable) {
        }
    }
}
