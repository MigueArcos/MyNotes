package com.example.miguel.misnotas.mymoney;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.miguel.misnotas.InputField;
import com.example.miguel.misnotas.R;
import com.example.miguel.misnotas.models.Finance;

/**
 * Created by 79812 on 30/04/2018.
 */

public class FinanceDialog extends AlertDialog.Builder implements TextWatcher {

    private InputField nameEdit, valueEdit;
    private AlertDialog dialog;
    protected FinanceDialog(Activity activity) {
        super(activity);
        View financeView = activity.getLayoutInflater().inflate(R.layout.dialog_create_finance, null);
        setView(financeView);
        nameEdit = financeView.findViewById(R.id.finance_name);
        valueEdit = financeView.findViewById(R.id.finance_value);
    }

    public void createDialog(){
        dialog = this.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                nameEdit.getEditText().addTextChangedListener(FinanceDialog.this);
                valueEdit.getEditText().addTextChangedListener(FinanceDialog.this);
            }
        });
        dialog.show();
    }
    public void setName(String name) {
        nameEdit.setText(name);
    }

    public void setValue(int value) {
        valueEdit.setText(String.valueOf(value));
    }

    public String getName() {
        return nameEdit.getText();
    }

    public int getValue() {
        return Integer.parseInt(valueEdit.getText());
    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (nameEdit.getText().length() == 0 || valueEdit.getText().length() == 0) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        } else {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        }
    }

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    public void afterTextChanged(Editable s) {
    }
}
