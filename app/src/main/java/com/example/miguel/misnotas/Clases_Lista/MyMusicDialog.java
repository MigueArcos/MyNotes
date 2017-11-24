package com.example.miguel.misnotas.Clases_Lista;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.SeekBar;

import com.example.miguel.misnotas.MyUtils;
import com.example.miguel.misnotas.R;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 79812 on 23/11/2017.
 */
public class MyMusicDialog extends AlertDialog.Builder {
    private String audioPath;
    private MediaPlayer mPlayer;
    private Activity activity;

    public MyMusicDialog(@NonNull Activity activity, File audio) {
        super(activity);
        mPlayer = new MediaPlayer();
        audioPath = audio.getAbsolutePath();
        this.activity = activity;
        setTitle(audio.getName());
        setPositiveButton("Detener",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPlayer.release();
                        dialog.cancel();
                    }
                });

        setNegativeButton("Pausar/Reproducir",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*if (mPlayer.isPlaying()){
                            mPlayer.pause();
                        }else{
                            mPlayer.start();
                        }*/
                        mPlayer.release();
                    }
                });

        setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mPlayer.release();
            }
        });
        setMessage("Modificado el "+MyUtils.longDateFormat(audio.lastModified(), activity.getString(R.string.date_format)));
    }

    public void startPlaying() {
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(audioPath);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mPlayer) {
                mPlayer.release();
            }
        });
        show();
    }
}