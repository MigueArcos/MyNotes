package com.zeus.migue.notes.ui.shared;

import android.text.Editable;
import android.text.TextWatcher;


public class TextWatcherWrapper implements TextWatcher {
    private OnTextChanged onTextChanged;
    private AfterTextChange afterTextChange;
    private BeforeTextChanged beforeTextChanged;

    public TextWatcherWrapper(OnTextChanged onTextChanged, AfterTextChange afterTextChange, BeforeTextChanged beforeTextChanged) {
        this.onTextChanged = onTextChanged;
        this.afterTextChange = afterTextChange;
        this.beforeTextChanged = beforeTextChanged;
    }

    public TextWatcherWrapper(OnTextChanged onTextChanged, AfterTextChange afterTextChange) {
        this.onTextChanged = onTextChanged;
        this.afterTextChange = afterTextChange;
    }

    public TextWatcherWrapper(OnTextChanged onTextChanged) {
        this.onTextChanged = onTextChanged;
    }

    public TextWatcherWrapper(OnTextChanged onTextChanged, BeforeTextChanged beforeTextChanged) {
        this.onTextChanged = onTextChanged;
        this.beforeTextChanged = beforeTextChanged;
    }

    public TextWatcherWrapper(AfterTextChange afterTextChange, BeforeTextChanged beforeTextChanged) {
        this.afterTextChange = afterTextChange;
        this.beforeTextChanged = beforeTextChanged;
    }

    public TextWatcherWrapper(AfterTextChange afterTextChange) {
        this.afterTextChange = afterTextChange;
    }

    public TextWatcherWrapper(BeforeTextChanged beforeTextChanged) {
        this.beforeTextChanged = beforeTextChanged;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (beforeTextChanged != null)
            beforeTextChanged.beforeTextChanged(s, start, count, after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (onTextChanged != null)
            onTextChanged.onTextChanged(s, start, before, count);
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (afterTextChange != null)
            afterTextChange.afterTextChanged(s);
    }


    public interface OnTextChanged {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public interface AfterTextChange {
        void afterTextChanged(Editable s);
    }

    public interface BeforeTextChanged {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
    }

}