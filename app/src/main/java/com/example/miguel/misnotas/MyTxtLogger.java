package com.example.miguel.misnotas;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Migue on 24/09/2017.
 */
public class MyTxtLogger {
    private static MyTxtLogger instance;
    private MyTxtLogger(){

    }
    public static MyTxtLogger getInstance(){
        if (instance == null){
            instance=new MyTxtLogger();
        }
        return instance;
    }
    public void writeToSD(String text){
        try{
            File myLog = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "MyNotesAppLogger.txt");
            //The second parameter indicates wheter you want to append text or create new file (true = append)
            FileOutputStream fos = new FileOutputStream(myLog,true);
            PrintWriter pw = new PrintWriter(fos);
            pw.println(getDate()+" -> "+text);
            pw.flush();
            pw.close();
            fos.close();
        }
        catch (Exception e){
            Log.e("Files", "Error when trying to write to SD");
        }
    }
    private String getDate(){
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(new Date());
    }

}