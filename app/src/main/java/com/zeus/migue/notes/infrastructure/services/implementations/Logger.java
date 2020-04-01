package com.zeus.migue.notes.infrastructure.services.implementations;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class Logger implements ILogger {
    private static Logger instance;
    private String externalStoragePath;

    private Logger(Context appContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            externalStoragePath = appContext.getExternalFilesDir(null).getAbsolutePath();
        } else {
            externalStoragePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        }
    }

    public static synchronized Logger getInstance(Context context) {
        if (instance == null) {
            instance = new Logger(context.getApplicationContext());
        }
        return instance;
    }


    @Override
    public void log(String text) {
        try {
            File myLog = new File(externalStoragePath, "AppLogger.txt");
            //The second parameter indicates whether you want to append text or create new file (true = append)
            FileOutputStream fos = new FileOutputStream(myLog, true);
            PrintWriter pw = new PrintWriter(fos);
            pw.println(Utils.toIso8601(new Date(), false) + " -> " + text);
            pw.flush();
            pw.close();
            fos.close();
        } catch (Exception e) {
            Log.e("Files", "ErrorCode when trying to write to SD");
        }
    }
}

