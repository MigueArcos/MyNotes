package com.zeus.migue.notes.ui.activity_main.fragments.accounts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.Account;
import com.zeus.migue.notes.infrastructure.dao.AccountsDao;
import com.zeus.migue.notes.infrastructure.repositories.AccountsRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.ui.shared.BaseListViewModel;

import java.util.List;

public class AccountsViewModel extends BaseListViewModel<Account, AccountDTO> {

    public AccountsViewModel(@NonNull Application application) {
        super(application);
    }

    @Override
    public LiveData<List<AccountDTO>> initItemsLiveData() {
        return ((AccountsDao) repository.getDao()).getAccounts();
    }

    @Override
    public AccountsRepository getRepository(Application application) {
        return AccountsRepository.getInstance(AppDatabase.getInstance(application).accountsDao(), Logger.getInstance(application));
    }
}
