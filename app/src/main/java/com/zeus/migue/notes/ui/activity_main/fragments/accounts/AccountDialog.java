package com.zeus.migue.notes.ui.activity_main.fragments.accounts;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.infrastructure.utils.Utils;
import com.zeus.migue.notes.ui.shared.InputField;
import com.zeus.migue.notes.ui.shared.TextWatcherWrapper;

import java.util.Date;

public class AccountDialog extends AlertDialog.Builder {
    private AccountDTO account;
    private InputField accountNameEdit, accountValueEdit;

    protected AccountDialog(Activity activity, String payload) {
        super(activity);
        View financeView = activity.getLayoutInflater().inflate(R.layout.dialog_add_account, null);
        setView(financeView);
        accountNameEdit = financeView.findViewById(R.id.account_name);
        accountValueEdit = financeView.findViewById(R.id.account_value);
        if (!Utils.stringIsNullOrEmpty(payload)) {
            AccountDTO account = Utils.fromJson(payload, AccountDTO.class, false);
            this.account = account;
            accountNameEdit.setText(account.getName());
            accountValueEdit.setText(Utils.formatDouble(account.getTotal()));
        }
    }
    @NonNull
    @Override
    public AlertDialog create() {
        AlertDialog dialog = super.create();
        TextWatcherWrapper.AfterTextChange afterTextChange = s -> {
            if (accountIsValid()) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
            } else {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        };
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            accountNameEdit.getEditText().addTextChangedListener(new TextWatcherWrapper(afterTextChange));
            accountValueEdit.getEditText().addTextChangedListener(new TextWatcherWrapper(afterTextChange));
        });
        return dialog;
    }

    public AccountDTO getCurrentAccount() {
        if (account == null) {
            String isoDate = Utils.toIso8601(new Date(), true);
            account = new AccountDTO(accountNameEdit.getText(), Double.parseDouble(accountValueEdit.getText()), isoDate, isoDate);
        } else {
            account.setName(accountNameEdit.getText());
            account.setTotal(Double.parseDouble(accountValueEdit.getText()));
        }
        return account;
    }

    public boolean accountIsValid() {
        return !Utils.stringIsNullOrEmpty(accountNameEdit.getText()) && !Utils.stringIsNullOrEmpty(accountValueEdit.getText());
    }
}
