package utilities;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.android.bookfinder.R;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class contains common methods
 * Author : aditibhattacharya
 */

public final class Utils {

    /**
     * This is a private constructor and only meant to hold static variables and methods,
     * which can be accessed directly from the class name Utils
     */
    private void Utils() {
    }

    /**
     * Method to set custom typeface to UI elements
     */
    public static void setCustomTypeface(Context context, View view) {
        Typeface typefaceSourceSerif = Typeface.createFromAsset(context.getAssets(),
                "fonts/sourceserifpro_semibold.otf");
        Typeface typefaceSourceSerifReg = Typeface.createFromAsset(context.getAssets(),
                "fonts/sourceserifpro_regular.otf");
        Typeface typefaceSourceSans = Typeface.createFromAsset(context.getAssets(),
                "fonts/sourcesanspro_semibold.otf");

        // Get tag on the view
        String viewTag = view.getTag().toString();

        // Cast view to appropriate view element based on tag received and set typefaces
        if (viewTag.equals(context.getString(R.string.tag_textview))) {
            TextView textView = (TextView) view;
            textView.setTypeface(typefaceSourceSerif);
        } else if (viewTag.equals(context.getString(R.string.tag_textview_2))) {
            TextView textView = (TextView) view;
            textView.setTypeface(typefaceSourceSerifReg);
        } else if (viewTag.equals(context.getString(R.string.tag_edittext))) {
            EditText editText = (EditText) view;
            editText.setTypeface(typefaceSourceSerif);
        } else if (viewTag.equals(context.getString(R.string.tag_button))) {
            Button button = (Button) view;
            button.setTypeface(typefaceSourceSans);
        }
    }

    /**
     * Method to check if user input text is null
     * @return true/false
     */
    public static boolean checkEmptyString(String input) {
        if (input.length() == 0) {
            return false;
        } else  {
            return true;
        }
    }

    /**
     * Method to check if user input text is valid
     * @return true/false
     */
    public static boolean checkValidString(String input) {
        String patternName = "[a-zA-z.]+([ '-][a-zA-Z.]+)*";
        Pattern pattern = Pattern.compile(patternName);
        Matcher matcher = pattern.matcher(input);

        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }
    }

}
