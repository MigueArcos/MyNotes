package com.example.miguel.misnotas;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

/**
 * Created by Miguel Ángel López Arcos on 14/02/2018.
 */

public class InputField extends LinearLayout implements View.OnFocusChangeListener {
    private TextInputLayout label;
    private EditText editText;
    private Pattern regex;
    private String errorLabel;

    public InputField(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(LinearLayout.VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        inflater.inflate(R.layout.input_field, this, true);
    }

    public void setRegex(Pattern regex) {
        this.regex = regex;
    }

    public void setErrorLabel(String errorLabel) {
        this.errorLabel = errorLabel;
    }

    public void setHint(String hint){
        editText.setHint(hint);
    }

    public EditText getEditText(){
       return editText;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        initializeComponents();
    }

    private void initializeComponents() {
        label = this.findViewById(R.id.label);
        editText = this.findViewById(R.id.edit_text);
        editText.setOnFocusChangeListener(this);
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null) return false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (!validateField()) {
                        label.setError(errorLabel);
                    } else {
                        label.setError(null);
                        label.setErrorEnabled(false);
                    }

                }
                return false; // pass on to other listeners.
            }
        });
    }
    public boolean validateField(){
        if (regex != null && !regex.matcher(editText.getText().toString()).matches()){
            return false;
        }
        return true;
    }

    public String getText(){
        return editText.getText().toString();
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        //EditText has gotten Focus
        if (hasFocus) {
            label.setError(null);
            label.setErrorEnabled(false);
        }
        //EditText has lost the focus
        else {
            if (!validateField()) {
                label.setError(errorLabel);
            }
        }
    }
}
