package com.example.miguel.misnotas;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Migue on 30/09/2017.
 */
public class MyUtils {
    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    public static String longDateFormat(long timeMilis, String dateFormat){
        DateFormat df = new SimpleDateFormat(dateFormat, Locale.US);
        Date dateObj = new Date(timeMilis);
        return df.format(dateObj);
    }
}