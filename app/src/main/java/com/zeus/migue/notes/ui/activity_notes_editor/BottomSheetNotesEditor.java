package com.zeus.migue.notes.ui.activity_notes_editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.util.Linkify;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.ColorUtils;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.ClipNoteDTO;
import com.zeus.migue.notes.data.DTO.NoteDTO;
import com.zeus.migue.notes.infrastructure.utils.Event;
import com.zeus.migue.notes.infrastructure.utils.Utils;

import java.util.Date;

public class BottomSheetNotesEditor extends BottomSheetDialogFragment {
    public interface Callback{
        void onDatabaseChangeReady();
    }
    private String originalTitle = Utils.EMPTY_STRING;
    private String originalContent = Utils.EMPTY_STRING;
    private TextView contentEdit, titleEdit;
    private Toolbar toolbar;
    private boolean editModeEnabled = false, isNewNote = true, isClipNote = false;
    private NoteDTO noteDTO;
    private NotesEditorViewModel notesEditorViewModel;
    private Callback callback;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.NotesEditorDialog);
    }

    public void setCallback(Callback callback) {
        this.callback = callback;
    }

    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View v = View.inflate(getActivity(), R.layout.dialog_notes_editor, null);
        dialog.setContentView(v);
        initializeViews(v);
        Bundle packageData = getArguments();
        if (packageData != null) {
            isNewNote = false;
            isClipNote = packageData.getBoolean("isClipNote", false);
            if (isClipNote){
                ClipNoteDTO dto = Utils.fromJson(packageData.getString("payload", "{}"), ClipNoteDTO.class, false);
                originalContent = dto.getContent();
                contentEdit.setText(originalContent);
                titleEdit.setVisibility(View.GONE);
                setEditable(contentEdit, false);
                toolbar.getMenu().findItem(R.id.edit).setVisible(false);
                toolbar.getMenu().findItem(R.id.done).setVisible(false);
            }else{
                noteDTO = Utils.fromJson(packageData.getString("payload", "{}"), NoteDTO.class, false);
                originalTitle = noteDTO.getTitle();
                originalContent = noteDTO.getContent();
                contentEdit.setText(originalContent);
                titleEdit.setText(originalTitle);
                setEditable(titleEdit, false);
                setEditable(contentEdit, false);
                toolbar.getMenu().findItem(R.id.edit).setVisible(true);
            }
        } else {
            editModeEnabled = true;
            toolbar.getMenu().findItem(R.id.edit).setVisible(false);
        }
        notesEditorViewModel = new ViewModelProvider(this).get(NotesEditorViewModel.class);
        notesEditorViewModel.getEvent().observe(this, liveDataEvent -> {
            Event event = liveDataEvent.getContentIfNotHandled();
            if (event != null) {
                if (event.getMessageType() == Event.MessageType.SHOW_IN_DIALOG) {
                    new androidx.appcompat.app.AlertDialog.Builder(getContext()).setTitle(R.string.generic_error_title).setMessage(event.getLocalResId()).show();
                } else if (event.getMessageType() == Event.MessageType.SHOW_IN_TOAST) {
                    Toast.makeText(getContext(), getString(event.getLocalResId()), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initializeViews(View v) {
        toolbar = v.findViewById(R.id.toolbar);
        toolbar.setTitle(getActivity().getString(R.string.activity_notes_editor_title));
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.inflateMenu(R.menu.dialog_notes_editor_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.done:
                    if (isClipNote) return true;
                    insertOrUpdate();
                    Utils.hideKeyboardFromView(v);
                    return true;
                case R.id.edit:
                    if (isClipNote) return true;
                    editModeEnabled = !editModeEnabled;
                    setEditable(titleEdit, editModeEnabled);
                    setEditable(contentEdit, editModeEnabled);
                    if (!editModeEnabled) Utils.hideKeyboardFromView(v);
                    return true;
                case R.id.copy_to_clipboard:
                    Utils.copyTextToClipboard(getContext(), contentEdit.getText().toString());
                    Toast.makeText(getContext(), R.string.text_copied, Toast.LENGTH_SHORT).show();
                    return true;
                default:
                    return false;
            }
        });
        toolbar.setNavigationOnClickListener(view -> {
            if (isClipNote) this.dismiss();
            if (thereAreChanges()) {
                new AlertDialog.Builder(getContext()).setTitle(R.string.dialog_warning_title).setMessage(R.string.notes_editor_dialog_confirm_loss_changes).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> {
                    this.dismiss();
                }).setNegativeButton(R.string.dialog_cancel_message, null).show();
            } else {
                this.dismiss();
            }
        });
        contentEdit = v.findViewById(R.id.content);
        titleEdit = v.findViewById(R.id.title);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) v.getParent());
        DisplayMetrics displayMetrics = getActivity().getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;
        int initialDialogHeight = (int) (screenHeight * 0.85);
        bottomSheetBehavior.setPeekHeight(initialDialogHeight, true);

        FrameLayout scrollableContent = v.findViewById(R.id.dialog_scrollable_content);
        ViewGroup.LayoutParams params = scrollableContent.getLayoutParams();
        params.height = screenHeight;
        scrollableContent.setLayoutParams(params);
    }

    private void setEditable(TextView textView, boolean isEditable) {
        ///textView.setClickable(true);
        textView.setText(textView.getText().toString());
        if (isEditable) {
            textView.setShowSoftInputOnFocus(true);
            textView.setTextColor(ColorUtils.setAlphaComponent(textView.getCurrentTextColor(), 255));
        } else {
            textView.setShowSoftInputOnFocus(false);
            textView.setTextColor(ColorUtils.setAlphaComponent(textView.getCurrentTextColor(), 96));
            Linkify.addLinks(textView, Linkify.WEB_URLS);
        }
    }

    private void insertOrUpdate() {
        String currentTitle = titleEdit.getText().toString(), currentContent = contentEdit.getText().toString();
        String date = Utils.toIso8601(new Date(), true);
        if (thereAreChanges()) {
            if (isNewNote) {
                noteDTO = new NoteDTO(currentTitle, currentContent, date, date, false);
                isNewNote = false;
                toolbar.getMenu().findItem(R.id.edit).setVisible(true);
                long id = notesEditorViewModel.insertNote(noteDTO);
                noteDTO.setId(id);
            } else {
                noteDTO.setTitle(currentTitle);
                noteDTO.setContent(currentContent);
                noteDTO.setModificationDate(date);
                notesEditorViewModel.updateNote(noteDTO);
            }
            originalContent = currentContent;
            originalTitle = currentTitle;
            editModeEnabled = false;
            setEditable(titleEdit, false);
            setEditable(contentEdit, false);
            if (callback != null){
                new Handler().postDelayed(() -> callback.onDatabaseChangeReady(), 100);
            }
            Toast.makeText(getContext(), R.string.saved_message, Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), R.string.nothing_to_save_message, Toast.LENGTH_SHORT).show();

    }

    private boolean thereAreChanges() {
        String currentTitle = titleEdit.getText().toString(), currentContent = contentEdit.getText().toString();
        return (!currentTitle.equals(originalTitle) || !currentContent.equals(originalContent)) && (!Utils.stringIsNullOrEmpty(currentTitle) || !Utils.stringIsNullOrEmpty(currentContent));
    }
}