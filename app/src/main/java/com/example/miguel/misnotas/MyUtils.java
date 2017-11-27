package com.example.miguel.misnotas;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Migue on 30/09/2017.
 */
public class MyUtils {
    private static final int BUFFER_SIZE = 4096;

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

    private static void extractFile(ZipInputStream in, File outDir, String name) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(new File(outDir, name)));
        int count = -1;
        while ((count = in.read(buffer)) != -1)
            out.write(buffer, 0, count);
        out.close();
    }

    private static void mkdirs(File outDir, String path) {
        File d = new File(outDir, path);
        if (!d.exists())
            d.mkdirs();
    }

    private static String dirPart(String name) {
        int s = name.lastIndexOf(File.separatorChar);
        return s == -1 ? null : name.substring(0, s);
    }

    /***
     * Extract zipfile to outdir with complete directory structure
     * @param zipfile Input .zip file
     * @param outdir Output directory
     */
    public static void extract(File zipfile, File outdir) {
        try {
            ZipInputStream zin = new ZipInputStream(new FileInputStream(zipfile));
            ZipEntry entry;
            String name, dir;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                if (entry.isDirectory()) {
                    mkdirs(outdir, name);
                    continue;
                }
        /* this part is necessary because file entry can come before
         * directory entry where is file located
         * i.e.:
         *   /foo/foo.txt
         *   /foo/
         */
                dir = dirPart(name);
                if (dir != null)
                    mkdirs(outdir, dir);

                extractFile(zin, outdir, name);
            }
            zin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void deleteFilesInFolder(File Folder){
        if (Folder.exists()){
            File[] files = Folder.listFiles();
            for (File file : files){
                file.delete();
            }
        }
        else{
            Folder.mkdir();
        }
    }
    public static String secondsToTimeFormat(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return ((minutes < 10) ? "0" + minutes : "" + minutes) + ":" + ((seconds < 10) ? "0" + seconds : "" + seconds);
    }
}