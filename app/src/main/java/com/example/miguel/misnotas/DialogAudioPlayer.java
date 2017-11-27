package com.example.miguel.misnotas;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by 79812 on 23/11/2017.
 */
public class DialogAudioPlayer extends AlertDialog {
    private String audioPath;
    private MediaPlayer mPlayer;
    private Activity activity;
    private TextView voiceNoteName, voiceNoteProgress;
    private SeekBar seekBar;
    private ImageButton playPauseAudio;
    private Handler handler = new Handler();
    private Runnable handlerCallback;
    public DialogAudioPlayer(@NonNull Activity activity, File audio) {
        super(activity);
        mPlayer = new MediaPlayer();
        audioPath = audio.getAbsolutePath();
        this.activity = activity;
        setTitle(activity.getString(R.string.voice_notes_title));

        handlerCallback = new Runnable() {
            @Override
            public void run() {
                Log.d("Duration", "" + mPlayer.getDuration());
                seekBar.setProgress(mPlayer.getCurrentPosition());
                voiceNoteProgress.setText(MyUtils.secondsToTimeFormat(mPlayer.getCurrentPosition()/1000 + 1)+"/"+MyUtils.secondsToTimeFormat(mPlayer.getDuration()/1000));
                handler.postDelayed(this, 1000);
            }
        };

        View musicPlayerView = activity.getLayoutInflater().inflate(R.layout.dialog_audio_player, null);
        setView(musicPlayerView);
        voiceNoteName = musicPlayerView.findViewById(R.id.voice_note_name);
        voiceNoteProgress = musicPlayerView.findViewById(R.id.voice_note_progress);
        seekBar = musicPlayerView.findViewById(R.id.seek_bar);
        playPauseAudio = musicPlayerView.findViewById(R.id.play_pause_button);

        playPauseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mPlayer.isPlaying()) {
                    mPlayer.pause();
                    handler.removeCallbacks(handlerCallback);
                    playPauseAudio.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    mPlayer.start();
                    playPauseAudio.setImageResource(android.R.drawable.ic_media_play);
                    setTimer();
                }
            }
        });

        mPlayer = new MediaPlayer();
        voiceNoteName.setText(audio.getName());
        startPlaying();
        voiceNoteProgress.setText("00:00/" + MyUtils.secondsToTimeFormat(mPlayer.getDuration() / 1000));
        setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mPlayer.release();
                handler.removeCallbacks(handlerCallback);
            }
        });

    }


    private void setTimer() {
        handler.postDelayed(handlerCallback,1000);
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
            seekBar.setMax(mPlayer.getDuration());
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();

        setTimer();

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mPlayer) {
                //seekBar.setProgress(mPlayer.getDuration());
                mPlayer.release();
                handler.removeCallbacks(handlerCallback);
                DialogAudioPlayer.this.dismiss();
            }
        });

    }

}