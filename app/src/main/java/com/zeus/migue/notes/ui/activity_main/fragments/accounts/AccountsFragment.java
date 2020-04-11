package com.zeus.migue.notes.ui.activity_main.fragments.accounts;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zeus.migue.notes.R;
import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.data.room.entities.Account;
import com.zeus.migue.notes.ui.shared.recyclerview.BaseListFragment;

public class AccountsFragment extends BaseListFragment<Account, AccountDTO, AccountsViewModel> {
    public static AccountsFragment newInstance() {
        AccountsFragment myFragment = new AccountsFragment();
        Bundle args = new Bundle();
        args.putBoolean("hasOptionsMenu", false);
        myFragment.setArguments(args);
        return myFragment;
    }


    @Override
    public void handleItemSwipe(AccountDTO dto, int position, int swipeDir) {
        new AlertDialog.Builder(getActivity()).setTitle(R.string.dialog_warning_title).setMessage(R.string.activity_main_fragment_deleted_notes_cannot_recover_notes_warning).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> viewModel.deleteItem(dto)).setNegativeButton(R.string.dialog_cancel_message, (dialog, which) -> recoverDataAtPosition(dto, position)).setCancelable(false).show();
        adapter.getItems().remove(position);
        adapter.notifyItemRemoved(position + 1);
    }

    @Override
    protected void recoverDataAtPosition(AccountDTO data, int position) {
        adapter.getItems().add(position, data);
        adapter.notifyItemInserted(position + 1);
    }

    @Override
    public AccountsViewModel initializeViewModel() {
        return new ViewModelProvider(this).get(AccountsViewModel.class);
    }

    @Override
    public void observeItems() {
        viewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            emptyListLabel.setVisibility(View.GONE);
            adapter.updateData(items);
            list.scrollToPosition(0);
        });
    }

    @Override
    public AccountsAdapter getAdapter() {
        return new AccountsAdapter(this::handleItemSwipe);
    }

    @Override
    public int getViewResId() {
        return R.layout.activity_main_fragment_notes;
    }

    @Override
    public void onChangeSearchViewState(boolean isShown) {

    }

    @Override
    protected void initializeViews(View rootView) {
        emptyListLabel = rootView.findViewById(R.id.empty_list_label);
        loader = rootView.findViewById(R.id.loader);
        FloatingActionButton create = rootView.findViewById(R.id.create_new_fab);

        create.setOnClickListener(v -> {
            AccountDialog accountDialog = new AccountDialog(getActivity(), null);
            accountDialog.setTitle(R.string.fragment_my_money_dialog_accounts_title).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> {
                if (!accountDialog.accountIsValid()) {
                    Toast.makeText(getContext(), R.string.invalid_arguments, Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.insertItem(accountDialog.getCurrentAccount());
                }
            }).setNegativeButton(R.string.dialog_cancel_message, null).show();
        });
        list = rootView.findViewById(R.id.list);
        adapter = getAdapter();
        adapter.setItemClickListener((data, position) -> {
            AccountDialog accountDialog = new AccountDialog(getActivity(), data.toJson(false));
            accountDialog.setTitle(R.string.fragment_my_money_dialog_accounts_title).setPositiveButton(R.string.dialog_ok_message, (dialog, which) -> {
                if (!accountDialog.accountIsValid()) {
                    Toast.makeText(getContext(), R.string.invalid_arguments, Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.updateItem(accountDialog.getCurrentAccount());
                }
            }).setNegativeButton(R.string.dialog_cancel_message, null).show();
        });

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        list.setHasFixedSize(true);
        list.setAdapter(adapter);
        //setHasOptionsMenu(true);
    }
}