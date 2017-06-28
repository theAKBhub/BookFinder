package utilities;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import com.example.android.bookfinder.Book;
import com.example.android.bookfinder.R;
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
 * Helper methods related to requesting and receiving book data from Google API
 */

public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName();
    private static Context mContext;

    // Google Book API Keys
    private static final String API_KEY_ITEMS = "items";
    private static final String API_KEY_VOLUMEINFO = "volumeInfo";
    private static final String API_KEY_TITLE = "title";
    private static final String API_KEY_AUTHORS = "authors";
    private static final String API_KEY_PUBLISHED_DATE = "publishedDate";
    private static final String API_KEY_CATEGORIES = "categories";
    private static final String API_KEY_PAGECOUNT = "pageCount";
    private static final String API_KEY_LANGUAGE = "language";
    private static final String API_KEY_PREVIEWLINK = "previewLink";
    private static final String API_KEY_PRINT_TYPE = "printType";
    private static final String API_KEY_RATING = "averageRating";
    private static final String API_KEY_DESCRIPTION = "description";
    private static final String API_KEY_IMAGELINKS = "imageLinks";
    private static final String API_KEY_THUMBNAIL = "smallThumbnail";

    private static final String API_KEY_SALEINFO = "saleInfo";
    private static final String API_KEY_RETAIL_PRICE = "retailPrice";
    private static final String API_KEY_AMOUNT = "amount";
    private static final String API_KEY_CURRENCY = "currencyCode";
    private static final String API_KEY_BUYLINK = "buyLink";

    private static final String API_KEY_ACCESSINFO = "accessInfo";
    private static final String API_KEY_EPUB = "epub";
    private static final String API_KEY_PDF = "pdf";
    private static final String API_KEY_ISAVAILABLE = "isAvailable";

    /**
     * This is a private constructor and only meant to hold static variables and methods,
     * which can be accessed directly from the class name Utils
     */
    private QueryUtils() {
    }

    /**
     * Query the URL and return a list of {@link Book} objects.
     */
    public static List<Book> fetchBookData(String requestUrl, Context context) {

        mContext = context;

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_http_request), e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link Book}s
        List<Book> books = extractFeatureFromJson(jsonResponse);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Return the list of {@link Book}
        return books;
    }

    /**
     * This method returns new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_url_invalid), e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, mContext.getString(R.string.exception_resp_code) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_json_results), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
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
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link Book} objects retrieved from parsing a JSON response.
     */
    private static List<Book> extractFeatureFromJson(String bookJSON) {

        /** If the JSON string is empty or null, then return early. */
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        /** Create an empty ArrayList used to add books */
        List<Book> books = new ArrayList<>();

        try {
            JSONObject baseJsonResponse;        // JSON Object for the data retrieved from API request
            JSONArray bookArray;                // Array of books returned in the JSON object
            JSONObject currentBook;             // Single book at a specific position in the bookArray
            JSONObject volumeInfo;              // VolumeInfo object of the currentBook
            String title;                       // Title of the currentBook
            JSONArray authorsArray;             // Author Array of the currentBook
            String authorList = "";             // Authors of the currentBook - obtained from authorsArray
            String publishedDate = "";          // Published Date of the currentBook
            JSONArray categoriesArray;          // Categories Array of the currentBook
            String category = "";               // Category of the currentBook - obtained from categoriesArray
            String language = "";               // Language of the currentBook
            String previewLink = "";            // Preview Link of the currentBook
            int pageCount = 0;                  // PageCount of the currentBook
            String printType = "";              // Print Type of the currentBook
            double averageRating = 0.0;         // Average Rating of the currentBook
            String description = "";            // Description of the currentBook

            JSONObject imageLinks;              // JSON Object with various image links
            String thumbnailLink = "";          // Thumbnail Link of the currentBook - obtained from imageLinks

            JSONObject saleInfo;                // JSON Object for Sale Info of the currentBook
            JSONObject retailPrice;             // JSON Object for Retail Price of the currentBook - obtained from saleInfo object
            double amount = 0.00;               // Retail Price of the currentBook - obtained from retailPrice object
            String currencyCode = "";           // Currency Code of the currentBook - obtained from retailPrice object
            String buyLink = "";                // Buying Link of the currentBook - obtained from saleInfo object

            JSONObject accessInfo;              // JSON Object for Access Info of the currentBook
            JSONObject epub;                    // JSON Object for epub flag - obtained from accessInfo object
            JSONObject pdf;                     // JSON Object for pdf flag - obtained from accessInfo object
            boolean isAvailableEpub = false;    // Indicator for EPUB version availability
            boolean isAvailablePdf = false;     // Indicator for PDF version availability


            baseJsonResponse = new JSONObject(bookJSON);

            if (baseJsonResponse.has(API_KEY_ITEMS)) {
                bookArray = baseJsonResponse.getJSONArray(API_KEY_ITEMS);

                for (int i = 0; i < bookArray.length(); i++) {

                    currentBook = bookArray.getJSONObject(i);
                    volumeInfo = currentBook.getJSONObject(API_KEY_VOLUMEINFO);
                    title = volumeInfo.getString(API_KEY_TITLE);

                    // Get value for author if the key exists
                    String author = "";
                    if (volumeInfo.has(API_KEY_AUTHORS)) {
                        authorsArray = volumeInfo.getJSONArray(API_KEY_AUTHORS);

                        if (authorsArray.length() > 1) {
                            authorList = authorsArray.join(", ").replaceAll("\"", "");
                        } else if (authorsArray.length() == 1) {
                            authorList = authorsArray.getString(0);
                        } else if (authorsArray.length() == 0) {
                            authorList = "";
                        }
                    }

                    // Get value for publishedDate if the key exists
                    if (volumeInfo.has(API_KEY_PUBLISHED_DATE)) {
                        publishedDate = volumeInfo.getString(API_KEY_PUBLISHED_DATE);
                    } else {
                        publishedDate = "";
                    }

                    // Get value for category if the key exists
                    if (volumeInfo.has(API_KEY_CATEGORIES)) {
                        categoriesArray = volumeInfo.getJSONArray(API_KEY_CATEGORIES);
                        category = categoriesArray.getString(0);
                    } else {
                        category = "";
                    }

                    // Get value for language if the key exists
                    if (volumeInfo.has(API_KEY_LANGUAGE)) {
                        language = volumeInfo.getString(API_KEY_LANGUAGE);
                    } else {
                        language = "";
                    }

                    // Get value for preview link if the key exists
                    if (volumeInfo.has(API_KEY_PREVIEWLINK)) {
                        previewLink = volumeInfo.getString(API_KEY_PREVIEWLINK);
                    } else {
                        previewLink = "";
                    }

                    // Get value pageCount if the key exists
                    if (volumeInfo.has(API_KEY_PAGECOUNT)) {
                        pageCount = volumeInfo.getInt(API_KEY_PAGECOUNT);
                    } else {
                        pageCount = 0;
                    }

                    // Get value printType if the key exists
                    if (volumeInfo.has(API_KEY_PRINT_TYPE)) {
                        printType = volumeInfo.getString(API_KEY_PRINT_TYPE);
                    } else {
                        printType = "";
                    }

                    // Get value for description if the key exists
                    if (volumeInfo.has(API_KEY_DESCRIPTION)) {
                        description = volumeInfo.getString(API_KEY_DESCRIPTION);
                    } else {
                        description = "";
                    }

                    // Get value for rating if the key exists
                    if (volumeInfo.has(API_KEY_RATING)) {
                        averageRating = volumeInfo.getDouble(API_KEY_RATING);
                    } else {
                        averageRating = 0.0;
                    }

                    // Get value for smallThumbnail if the key exists
                    if (volumeInfo.has(API_KEY_IMAGELINKS)) {
                        imageLinks = volumeInfo.getJSONObject(API_KEY_IMAGELINKS);
                        if (imageLinks.has(API_KEY_THUMBNAIL)) {
                            thumbnailLink = imageLinks.getString(API_KEY_THUMBNAIL);
                        } else {
                            thumbnailLink = "";
                        }
                    }

                    // Get retail price, currency code and buying link if the corresponding keys exist
                    if (currentBook.has(API_KEY_SALEINFO)) {
                        saleInfo = currentBook.getJSONObject(API_KEY_SALEINFO);

                        if (saleInfo.has(API_KEY_RETAIL_PRICE)) {
                            retailPrice = saleInfo.getJSONObject(API_KEY_RETAIL_PRICE);

                            if (retailPrice.has(API_KEY_AMOUNT)) {
                                amount = retailPrice.getDouble(API_KEY_AMOUNT);
                                currencyCode = retailPrice.getString((API_KEY_CURRENCY));
                            } else {
                                amount = 0.00;
                            }
                        } else {
                            amount = 0.00;
                        }

                        if (saleInfo.has(API_KEY_BUYLINK)) {
                            buyLink = saleInfo.getString(API_KEY_BUYLINK);
                        } else {
                            buyLink = "";
                        }
                    }

                    // Get indicators if EPUB and PDF versions available
                    if (currentBook.has(API_KEY_ACCESSINFO)) {
                        accessInfo = currentBook.getJSONObject(API_KEY_ACCESSINFO);

                        if (accessInfo.has(API_KEY_EPUB)) {
                            epub = accessInfo.getJSONObject(API_KEY_EPUB);
                            isAvailableEpub = epub.getBoolean(API_KEY_ISAVAILABLE);
                        } else {
                            isAvailableEpub = false;
                        }

                        if (accessInfo.has(API_KEY_PDF)) {
                            pdf = accessInfo.getJSONObject(API_KEY_PDF);
                            isAvailablePdf = pdf.getBoolean(API_KEY_ISAVAILABLE);
                        } else {
                            isAvailablePdf = false;
                        }
                    }


                    // Create a new {@link Book} object with parameters obtained from JSON response
                    Book book = new Book(
                            title,
                            authorList,
                            publishedDate,
                            category,
                            language,
                            pageCount,
                            printType,
                            amount,
                            currencyCode,
                            buyLink,
                            isAvailableEpub,
                            isAvailablePdf,
                            averageRating,
                            description,
                            thumbnailLink,
                            previewLink
                    );

                    // Add the new {@link Book} object to the list of books
                    books.add(book);
                }
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, mContext.getString(R.string.exception_json_results), e);
        }

        // Return the list of books
        return books;
    }

}
