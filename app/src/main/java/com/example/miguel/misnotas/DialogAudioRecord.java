package com.example.miguel.misnotas;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

/**
 * Created by Miguel on 26/11/2017.
 */
public class DialogAudioRecord extends AlertDialog implements View.OnClickListener {
    private Activity activity;
    private FloatingActionButton startRecord;
    private TextView startRecordLabel, timerLabel;
    private EditText voiceNoteNameInput;
    public static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    public static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    public static final String AUDIO_RECORDER_FILE_EXT_AAC = ".aac";
    public static final String AUDIO_RECORDER_FOLDER = "audios";
    private MediaRecorder recorder = null;
    private int currentFormat = 2;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP, MediaRecorder.OutputFormat.AAC_ADTS};
    private String fileExtensions[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP, AUDIO_RECORDER_FILE_EXT_AAC};
    private String currentFile;
    private audioRecordCompletionListener listener;
    private Handler handler = new Handler();
    private Runnable handlerCallback;
    private int progress = 0;
    private boolean isPlaying = false;

    @Override
    public void onClick(View view) {
        if (!isPlaying) {
            startRecording();
            startRecordLabel.setText(activity.getString(R.string.stop_recording_label));
            startRecord.setImageResource(android.R.drawable.ic_media_pause);
            isPlaying = true;
        } else {
            stopRecording();
            dismiss();
        }
    }

    interface audioRecordCompletionListener {
        void onComplete(File voiceNote);
    }

    protected DialogAudioRecord(@NonNull Activity activity, audioRecordCompletionListener listener) {
        super(activity);
        this.listener = listener;
        this.activity = activity;
        View audioRecorderView = activity.getLayoutInflater().inflate(R.layout.dialog_audio_record, null);
        setView(audioRecorderView);
        startRecord = audioRecorderView.findViewById(R.id.start_stop_audio_record);
        startRecordLabel = audioRecorderView.findViewById(R.id.start_stop_label);
        timerLabel = audioRecorderView.findViewById(R.id.timer_label);
        voiceNoteNameInput = audioRecorderView.findViewById(R.id.voice_note_name_input);
        handlerCallback = new Runnable() {
            @Override
            public void run() {
                progress++;
                timerLabel.setText(MyUtils.secondsToTimeFormat(progress));
                handler.postDelayed(this, 1000);
            }
        };
        startRecord.setOnClickListener(this);
        setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (isPlaying) {
                    stopRecording();
                }
            }
        });
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + fileExtensions[currentFormat]);
    }

    private String getFilename(String name) {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + name + fileExtensions[currentFormat]);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        currentFile = getFilename();
        recorder.setOutputFile(currentFile);
        recorder.setOnErrorListener(errorListener);
        recorder.setOnInfoListener(infoListener);

        try {
            recorder.prepare();
            recorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        handler.postDelayed(handlerCallback, 1000);
    }

    private MediaRecorder.OnErrorListener errorListener = new MediaRecorder.OnErrorListener() {
        @Override
        public void onError(MediaRecorder mr, int what, int extra) {
            MyTxtLogger.getInstance().writeToSD("Error: " + what + ", " + extra);
        }
    };

    private MediaRecorder.OnInfoListener infoListener = new MediaRecorder.OnInfoListener() {
        @Override
        public void onInfo(MediaRecorder mr, int what, int extra) {
            MyTxtLogger.getInstance().writeToSD("Warning: " + what + ", " + extra);
        }
    };

    private void stopRecording() {
        if (null != recorder) {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
            Log.d("OldName", currentFile);
            File voiceNote = new File(currentFile);

            String newFileName = getFilename((voiceNoteNameInput.getText().toString().isEmpty())? MyUtils.longDateFormat(System.currentTimeMillis(), activity.getString(R.string.date_format_voice_note)) : voiceNoteNameInput.getText().toString());

            Log.d("NewName", newFileName);
            if (voiceNote.renameTo(new File(newFileName))){
                Log.d("Rename", "Successful");
                listener.onComplete(new File(newFileName));
            }

            handler.removeCallbacks(handlerCallback);

        }
    }
}