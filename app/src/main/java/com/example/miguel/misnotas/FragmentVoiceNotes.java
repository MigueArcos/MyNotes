package com.example.miguel.misnotas;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

import com.example.miguel.misnotas.Clases_Lista.VoiceNote;
import com.example.miguel.misnotas.Clases_Lista.VoiceNotesAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.miguel.misnotas.DialogAudioRecord.AUDIO_RECORDER_FOLDER;
import static com.example.miguel.misnotas.Principal.RECORD_AUDIO_PERMISSION;

/**
 * Created by 79812 on 22/11/2017.
 */
public class FragmentVoiceNotes extends Fragment implements VoiceNotesAdapter.AdapterActions, DialogAudioRecord.audioRecordCompletionListener {
    private TextView emptyListText;
    private RecyclerView list;
    private VoiceNotesAdapter adapter;
    private FloatingActionButton create;
    private List<VoiceNote> voiceNoteList;
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
        list.setHasFixedSize(true);
        create = rootView.findViewById(R.id.create);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*VolleyDownloader request = Volley_Singleton.getInstance(getActivity()).downloadAudio("1511419031948.3gp", ShPrSync.getInt("id_usuario", 0));
                Volley_Singleton.getInstance(getActivity()).addToRequestQueue(request);*/

                if (checkPermissions()) {
                    DialogAudioRecord dialogAudioRecord = new DialogAudioRecord(getActivity(), FragmentVoiceNotes.this);
                    dialogAudioRecord.show();
                }

            }
        });
        message = new AlertDialog.Builder(getActivity());
        message.setTitle(getString(R.string.dialog_default_title));
        ShPrSync = getActivity().getSharedPreferences("Sync", Context.MODE_PRIVATE);
        return rootView;
    }

    @Override
    public void onResume() {
        adapter.setData(generateVoiceNotesList());
        list.setAdapter(adapter);
        super.onResume();
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
        Log.d("papulino", "" + voiceNoteList.size());
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
        DialogAudioPlayer myMusicDialog = new DialogAudioPlayer(getActivity(), voiceNoteList.get(position).getAudio());
        myMusicDialog.show();
    }

    @Override
    public void onLongClick(int position) {
        message.setMessage(R.string.label_question_delete_voice_note);
        message.setPositiveButton(getString(R.string.positive_button_label), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Volley_Singleton.getInstance(getActivity()).deleteVoiceNote(ShPrSync.getInt("id_usuario", 0), voiceNoteList.get(position).getAudio().getName(), new Volley_Singleton.audioDeletionListener() {
                    @Override
                    public void onNoteDeletionSuccess(String serverMessage) {
                        voiceNoteList.get(position).getAudio().delete();
                        onResume();
                    }

                    @Override
                    public void onNoteDeletionError(String serverMessage) {
                        message.setMessage(serverMessage);
                        message.show();
                    }
                });
            }
        });
        message.setNegativeButton(getString(R.string.negative_button_label),null);
        message.show();
    }


    @Override
    public void onComplete(File voiceNote) {
        voiceNoteList.add(new VoiceNote(voiceNote.getName(), MyUtils.longDateFormat(voiceNote.lastModified(), getString(R.string.date_format)), voiceNote));
        Database.getInstance(getActivity()).createNewVoiceNote(voiceNote.getName());
        adapter.notifyDataSetChanged();
    }
}