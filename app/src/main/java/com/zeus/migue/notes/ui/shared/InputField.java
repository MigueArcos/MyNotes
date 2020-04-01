package com.zeus.migue.notes.ui.shared;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

public class InputField extends TextInputLayout {
    private EditText editText;
    private Pattern regex;
    private String errorLabel;

    public InputField(Context context) {
        super(context);
    }

    public InputField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRegex(Pattern regex) {
        this.regex = regex;
    }

    public void setErrorLabel(String errorLabel) {
        this.errorLabel = errorLabel;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initializeComponents();
    }

    private void initializeComponents() {
        editText = this.getEditText();
        editText.setOnFocusChangeListener((view, hasFocus) -> {
            //EditText has gotten Focus
            if (hasFocus) {
                //setError(null);
                setErrorEnabled(false);
            }
            //EditText has lost the focus
            else {
                checkField();
            }
        });

    }

    //The last field also implements this listener
    public void setAsLastField() {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                    event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                checkField();
            }
            return false; // pass on to other listeners.
        });
    }

    public boolean textIsValid() {
        if (regex != null && !regex.matcher(editText.getText().toString()).matches()) {
            return false;
        }
        return true;
    }

    public void checkField() {
        if (!textIsValid()) {
            setError(errorLabel);
        } else {
            //setError(null);
            setErrorEnabled(false);
        }
    }

    public String getText() {
        return editText.getText().toString();
    }

    public void setText(String text) {
        assert getEditText() != null;
        getEditText().setText(text);
    }
}
