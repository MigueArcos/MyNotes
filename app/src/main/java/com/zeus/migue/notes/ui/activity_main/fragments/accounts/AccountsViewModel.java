package com.zeus.migue.notes.ui.activity_main.fragments.accounts;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.data.room.AppDatabase;
import com.zeus.migue.notes.data.room.entities.Account;
import com.zeus.migue.notes.infrastructure.dao.AccountsDao;
import com.zeus.migue.notes.infrastructure.errors.CustomError;
import com.zeus.migue.notes.infrastructure.repositories.AccountsRepository;
import com.zeus.migue.notes.infrastructure.services.implementations.Logger;
import com.zeus.migue.notes.infrastructure.utils.LiveDataEvent;
import com.zeus.migue.notes.ui.shared.recyclerview.BaseListViewModel;

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

    public long insertAccount(AccountDTO account) {
        try {
            return repository.insert(account);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
            return -1;
        }
    }

    public void updateAccount(AccountDTO account){
        try {
            repository.update(account);
        } catch (CustomError customError) {
            customError.printStackTrace();
            eventData.setValue(new LiveDataEvent<>(customError.getEvent()));
        }
    }
}
