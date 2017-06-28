package com.example.android.bookfinder;

/**
 * A {@link Book} object that contains details related to a single
 * book item to be displayed in BookListActivity
 */

public class Book {

    /** Book Title */
    private String mTitle;

    /** Book Author */
    private String mAuthor;

    /** Published Date */
    private String mPublishedDate;

    /** Book Categories */
    private String mCategories;

    /** Language */
    private String mLanguage;

    /** Page Count */
    private int mPageCount;

    /** Print Type */
    private String mPrintType;

    /** Retail Price */
    private double mRetailPrice;

    /** Currency Code */
    private String mCurrencyCode;

    /** Buying Link */
    private  String mBuyingLink;

    /** EPUB Tag */
    private boolean mIsEpubAvailable;

    /** PDF Tag */
    private boolean mIsPdfAvailable;

    /** Rating */
    private double mRating;

    /** Description */
    private String mDescription;

    /** Thumbnail Link */
    private String mThumbnailLink;

    /** Preview Link */
    private String mPreviewlLink;


    /**
     * Default Constructor - Constructs a new {@link Book} object
     * @param title - Title of the book
     * @param author - Author of the book
     * @param publishedDate - Date the book was published
     * @param categories - Category of the book (e.g. Health, Business, etc.)
     * @param language - Language of the book (e.g. en)
     * @param pageCount - Number of pages in the book (e.g. 150)
     * @param printType - Print Type of the book (e.g. BOOK)
     * @param retailPrice - Retail Price of the book (e.g. 15.75)
     * @param currencyCode - Currency Code used in Retail Price of the book (e.g. GBP)
     * @param buyingLink - Buying Link of the book
     * @param isEpubAvailable - Indicates whether epub version of the book is available
     * @param isPdfAvailable - Indicates whether pdf version of the book is available
     * @param rating - Rating of the book (e.g. 3.5)
     * @param description - Description of the book
     * @param thumbnailLink - Link for the book image
     * @param previewLink - Link for the book preview
     */
    public Book(String title, String author, String publishedDate, String categories,
                String language, int pageCount, String printType, double retailPrice,
                String currencyCode, String buyingLink, boolean isEpubAvailable,
                boolean isPdfAvailable, double rating, String description, String thumbnailLink,
                String previewLink) {

        mTitle = title;
        mAuthor = author;
        mPublishedDate = publishedDate;
        mCategories = categories;
        mLanguage = language;
        mPageCount = pageCount;
        mPrintType = printType;
        mRetailPrice = retailPrice;
        mCurrencyCode = currencyCode;
        mBuyingLink = buyingLink;
        mIsEpubAvailable = isEpubAvailable;
        mIsPdfAvailable = isPdfAvailable;
        mRating = rating;
        mDescription = description;
        mThumbnailLink = thumbnailLink;
        mPreviewlLink = previewLink;
    }


    /** Getter method - Title */
    public String getTitle() {
        return mTitle;
    }

    /** Setter method - Title */
    public void setTitle(String title) {
        mTitle = title;
    }

    /** Getter method - Author */
    public String getAuthor() {
        return mAuthor;
    }

    /** Setter method - Author */
    public void setAuthor(String author) {
        mAuthor = author;
    }

    /** Getter method - Published Date */
    public String getPublishedDate() {
        return mPublishedDate;
    }

    /** Setter method - Published Date */
    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    /** Getter method - Categories */
    public String getCategories() {
        return mCategories;
    }

    /** Setter method - Categories */
    public void setCategories(String categories) {
        mCategories = categories;
    }

    /** Getter method - Language */
    public String getLanguage() {
        return mLanguage;
    }

    /** Setter method - Language */
    public void setLanguage(String language) {
        mLanguage = language;
    }

    /** Getter method - Page Count */
    public int getPageCount() {
        return mPageCount;
    }

    /** Setter method - Page Count */
    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }

    /** Getter method - Print Type */
    public String getPrintType() {
        return mPrintType;
    }

    /** Setter method - Print Type */
    public void setPrintType(String printType) {
        mPrintType = printType;
    }

    /** Getter method - Epub Tag */
    public boolean isTagEpub() {
        return mIsEpubAvailable;
    }

    /** Setter method - Epub Tag */
    public void setTagEpub(boolean isEpubAvailable) {
        mIsEpubAvailable = isEpubAvailable;
    }

    /** Getter method - Pdf Tag */
    public boolean isTagPdf() {
        return mIsPdfAvailable;
    }

    /** Setter method - Pdf Tag */
    public void setTagPdf(boolean isPdfAvailable) {
        mIsPdfAvailable = isPdfAvailable;
    }

    /** Getter method - Rating */
    public double getRating() {
        return mRating;
    }

    /** Setter method - Rating */
    public void setRating(double rating) {
        mRating = rating;
    }

    /** Getter method - Retail Price */
    public double getRetailPrice() {
        return mRetailPrice;
    }

    /** Setter method - Retail Price */
    public void setRetailPrice(double retailPrice) {
        mRetailPrice = retailPrice;
    }

    /** Getter method - Currency Code */
    public String getCurrencyCode() {
        return mCurrencyCode;
    }

    /** Setter method - Currency Code */
    public void setCurrencyCode(String currencyCode) {
        mCurrencyCode = currencyCode;
    }

    /** Getter method - Buying Link */
    public String getBuyingLink() {
        return mBuyingLink;
    }

    /** Setter method - Buying Link */
    public void setBuyingLink(String buyingLink) {
        mBuyingLink = buyingLink;
    }

    /** Getter method - Description */
    public String getDescription() {
        return mDescription;
    }

    /** Setter method - Description */
    public void setDescription(String description) {
        mDescription = description;
    }

    /** Getter method - Thumbnail Link */
    public String getThumbnailLink() {
        return mThumbnailLink;
    }

    /** Setter method - Thumbnail Link */
    public void setThumbnailLink(String thumbnailLink) {
        mThumbnailLink = thumbnailLink;
    }

    /** Getter method - Preview Link */
    public String getPreviewlLink() {
        return mPreviewlLink;
    }

    /** Setter method - Preview Link */
    public void setPreviewLink(String previewLink) {
        mPreviewlLink = previewLink;
    }
}
