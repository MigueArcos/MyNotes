package com.example.miguel.misnotas;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.error.AuthFailureError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.example.miguel.misnotas.Clases_Lista.VoiceNote;
import com.example.miguel.misnotas.Clases_Lista.VoiceNotesAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import static com.example.miguel.misnotas.Principal.RECORD_AUDIO_PERMISSION;

/**
 * Created by 79812 on 22/11/2017.
 */
public class FragmentVoiceNotes extends Fragment implements VoiceNotesAdapter.AdapterActions, Volley_Singleton.audioUploadListener{
    private TextView emptyListText;
    private RecyclerView list;
    private VoiceNotesAdapter adapter;
    private FloatingActionButton create;
    public static final String AUDIO_RECORDER_FILE_EXT_3GP = ".3gp";
    public static final String AUDIO_RECORDER_FILE_EXT_MP4 = ".mp4";
    public static final String AUDIO_RECORDER_FOLDER = "AudioRecorder";
    private MediaRecorder recorder = null;
    private int currentFormat = 1;
    private int output_formats[] = {MediaRecorder.OutputFormat.MPEG_4, MediaRecorder.OutputFormat.THREE_GPP};
    private String file_exts[] = {AUDIO_RECORDER_FILE_EXT_MP4, AUDIO_RECORDER_FILE_EXT_3GP};
    private List<VoiceNote> voiceNoteList;
    private String currentFile;
    private ProgressDialog progressDialog;
    private SharedPreferences ShPrSync;
    private AlertDialog.Builder message;
    /**
     * Called when the activity is first created.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_voice_notes, container, false);
        emptyListText = rootView.findViewById(R.id.empty_list_text);
        list = rootView.findViewById(R.id.list);
        adapter = new VoiceNotesAdapter(voiceNoteList, this);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                emptyListText.setVisibility(voiceNoteList.size() > 0 ? View.GONE : View.VISIBLE);
                super.onChanged();
            }
        });
        adapter.setData(generateVoiceNotesList());
        list.setAdapter(adapter);
        list.setHasFixedSize(true);
        create = rootView.findViewById(R.id.create);
        /*create.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (checkPermissions()) {
                            MyTxtLogger.getInstance().writeToSD("Start Recording");
                            startRecording();
                            Toast.makeText(getActivity(),"empece de grabar", Toast.LENGTH_LONG).show();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        if (checkPermissions()) {
                            MyTxtLogger.getInstance().writeToSD("stop Recording");
                            stopRecording();
                            Toast.makeText(getActivity(),"termine de grabar", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                return false;
            }
        });*/
        ShPrSync = getActivity().getSharedPreferences("Sync", Context.MODE_PRIVATE);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setTitle(R.string.dialog_default_title);
        message = new AlertDialog.Builder(getActivity());
        message.setTitle(R.string.dialog_default_title);
        return rootView;
    }

    private List<VoiceNote> generateVoiceNotesList() {
        voiceNoteList = new ArrayList<>();
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File myAudioFolder = new File(filepath, AUDIO_RECORDER_FOLDER);
        File[] files = myAudioFolder.listFiles();

        if (files != null) {
            for (File file : files) {

                voiceNoteList.add(new VoiceNote(file.getName(), MyUtils.longDateFormat(file.lastModified(), getString(R.string.date_format)), file));
            }
        }
        Log.d("papulino",""+voiceNoteList.size());
        return voiceNoteList;
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_PERMISSION);
                return false;
            }
        }
        return true;
    }


    @Override
    public void onClick(int position) {
        //MyMusicDialog myMusicDialog = new MyMusicDialog(getActivity(), voiceNoteList.get(position).getAudio());
        //myMusicDialog.startPlaying();
        SimpleMultiPartRequest r = Volley_Singleton.getInstance(getActivity()).uploadAudio(voiceNoteList.get(position).getAudio().getAbsolutePath(),ShPrSync.getInt("id_usuario", 0),this);
        try {
            Log.e("some", r.getEncodedUrlParams() + "\n" + r.getUrl());
        } catch (AuthFailureError authFailureError) {
            authFailureError.printStackTrace();
        }
        Volley_Singleton.getInstance(getActivity()).addToRequestQueue(r);
        progressDialog.setMessage(getString(R.string.syncing_label));
        progressDialog.show();
    }

    private String getFilename() {
        String filepath = Environment.getExternalStorageDirectory().getPath();
        File file = new File(filepath, AUDIO_RECORDER_FOLDER);

        if (!file.exists()) {
            file.mkdirs();
        }

        return (file.getAbsolutePath() + "/" + System.currentTimeMillis() + file_exts[currentFormat]);
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(output_formats[currentFormat]);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
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
            File f = new File(currentFile);
            voiceNoteList.add(new VoiceNote(f.getName(), MyUtils.longDateFormat(f.lastModified(), getString(R.string.date_format)), f));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onFileUploadSuccess(String serverMessage) {
        progressDialog.dismiss();
        message.setMessage(serverMessage);
        message.show();
    }

    @Override
    public void onFileUploadError(String serverMessage) {
        progressDialog.dismiss();
        message.setMessage(serverMessage);
        message.show();
    }
}