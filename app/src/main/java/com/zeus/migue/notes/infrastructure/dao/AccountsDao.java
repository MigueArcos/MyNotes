package com.zeus.migue.notes.infrastructure.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.data.room.entities.Account;

import java.util.List;

@Dao
public interface AccountsDao extends BaseDao<Account> {
    @Query("SELECT * FROM Accounts ORDER BY ModificationDate DESC")
    LiveData<List<AccountDTO>> getAccounts();
}