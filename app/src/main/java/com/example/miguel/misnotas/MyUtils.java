package com.example.miguel.misnotas;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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
    public static final String GLOBAL_LOG_TAG = "Notes By Migue :D";

    public static void hideKeyboard(Activity activity){
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String formatDate(String dateFormatId){
        DateFormat formatter = new SimpleDateFormat(dateFormatId, Locale.US);
        return formatter.format(new Date());
    }

    public static String getTime12HoursFormat(String time24Hrs) {

        int hour = Integer.parseInt(time24Hrs.substring(0, 2));
        boolean isPm = hour > 11;
        return (isPm ? String.valueOf((hour + 11) % 12 + 1) : time24Hrs).concat(isPm ? "p.m" : "a.m");
    }
}