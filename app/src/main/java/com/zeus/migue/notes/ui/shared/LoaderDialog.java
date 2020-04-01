package com.zeus.migue.notes.ui.shared;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.infrastructure.utils.Utils;

public class LoaderDialog extends DialogFragment {
    private String mTitle;
    private TextView titleText;
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.getArguments() != null) {
            Bundle bundle = this.getArguments();
            mTitle = bundle.getString("title", Utils.EMPTY_STRING);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Dialog dialog = this.getDialog();
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable((Drawable) (new ColorDrawable(Color.TRANSPARENT)));
        }

        View rootView = inflater.inflate(R.layout.dialog_loader, container, false);
        titleText = rootView.findViewById(R.id.title_text_view);
        setMessage(mTitle);
        return rootView;
    }

    public static LoaderDialog newInstance(String title) {
        LoaderDialog frag = new LoaderDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void setMessage(String title){
        titleText.setText(title);
    }
}
