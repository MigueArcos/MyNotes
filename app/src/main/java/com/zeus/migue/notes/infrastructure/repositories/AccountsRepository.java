package com.zeus.migue.notes.infrastructure.repositories;

import com.zeus.migue.notes.data.DTO.AccountDTO;
import com.zeus.migue.notes.data.room.entities.Account;
import com.zeus.migue.notes.infrastructure.dao.AccountsDao;
import com.zeus.migue.notes.infrastructure.services.contracts.ILogger;

public class AccountsRepository extends GenericRepository<Account, AccountDTO> {
    private static AccountsRepository instance;

    private AccountsRepository(AccountsDao accountsDao, ILogger logger) {
        super(accountsDao, logger);
    }

    public static synchronized AccountsRepository getInstance(AccountsDao accountsDao, ILogger logger) {
        if (instance == null) {
            instance = new AccountsRepository(accountsDao, logger);
        }
        return instance;
    }
}
