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
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                //EditText has gotten Focus
                if (hasFocus) {
                    //setError(null);
                    setErrorEnabled(false);
                }
                //EditText has lost the focus
                else {
                    checkField();
                }
            }
        });

    }
    //The last field also implements this listener
    public void setAsLastField(){
        editText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    checkField();
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

    public void checkField(){
        if (!validateField()) {
            setError(errorLabel);
        } else {
            //setError(null);
            setErrorEnabled(false);
        }
    }
    public String getText(){
        return editText.getText().toString();
    }

}
