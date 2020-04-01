package com.zeus.migue.notes.infrastructure.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.zeus.migue.notes.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Utils {
    public static SimpleDateFormat ISO_8601_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    public static SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);
    public static final String EMPTY_STRING = "";
    public static final String GLOBAL_LOG_TAG = "Log Tag";
    public static void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //Useful when dialog won't hide it with original hideKeyboard Method
    public static void hideKeyboardFromView(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void openAppSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + activity.getPackageName()));
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static void changeStatusBarColor(Activity activity, int colorResId){
        activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, colorResId));
    }

    public static void copyTextToClipboard(Context context, String url) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(context.getPackageName(), url);
        //noinspection ConstantConditions
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, context.getText(R.string.text_copied), Toast.LENGTH_SHORT).show();
    }

    public static Date fromIso8601(String isoDate, boolean useUTC) {
        try {
            ISO_8601_FORMAT.setTimeZone(useUTC ? TimeZone.getTimeZone("UTC") : TimeZone.getDefault());
            return ISO_8601_FORMAT.parse(isoDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String toIso8601(Date date, boolean useUTC) {
        if (date == null) date = new Date();
        ISO_8601_FORMAT.setTimeZone(useUTC ? TimeZone.getTimeZone("UTC") : TimeZone.getDefault());
        return ISO_8601_FORMAT.format(date);
    }
    public static String niceDateFormat(Date date) {
        if (date == null) date = new Date();
        return FORMAT.format(date);
    }
    public static boolean stringIsNullOrEmpty(String src){
        return src == null || src.equals(EMPTY_STRING);
    }

    public static <T> boolean listIsNullOrEmpty(List<T> src){
        return src == null || src.size() == 0;
    }

}
