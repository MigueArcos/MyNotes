package com.example.miguel.misnotas.notes.deleted_notes;

/**
 * Created by 79812 on 13/07/2018.
 */

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Note;

import java.util.ArrayList;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private ArrayList<String> stringArrayList;
    private EditText titleEdit, contentEdit;
    private Note tNote;
    private DialogResultListener dialogResultListener;
    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.Theme_Design_BottomSheetDialog);
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dialogResultListener.onResultReady(tNote);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.bottom_sheet, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);

        // Set an OnMenuItemClickListener to handle menu item clicks
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.drawable.back_arrow:
                        Toast.makeText(getContext(), "You have clicked back arrow of this fragment using an standalone toolbar", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        // Inflate a menu to be displayed in the toolbar
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        toolbar.setNavigationOnClickListener(view -> this.dismiss());

        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setTitle(getString(R.string.app_name));

        titleEdit = rootView.findViewById(R.id.title);
        contentEdit = rootView.findViewById(R.id.content);
        contentEdit.setKeyListener(null);
        titleEdit.setKeyListener(null);
        if (getArguments() != null){
            tNote = getArguments().getParcelable("selectedNote");
            titleEdit.setText(tNote.getTitle());
            contentEdit.setText(tNote.getContent());
        }


        return rootView;
    }

    public void setDialogResultListener(DialogResultListener dialogResultListener){
        this.dialogResultListener = dialogResultListener;
    }

    public interface DialogResultListener{
        void onResultReady(Note tResult);
    }


}