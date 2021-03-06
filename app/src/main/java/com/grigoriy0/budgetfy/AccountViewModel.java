package com.grigoriy0.budgetfy;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private final AccountRepository accountRepository;
    private final LiveData<List<Account>> allAccounts;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
        allAccounts = accountRepository.getAccounts();
    }

    public void insert(Account account) {
        accountRepository.addAccount(account);
    }

    public void delete(Account account) {
        accountRepository.deleteAccount(account);
    }

    public void update(Account account) {
        accountRepository.updateAccount(account);
    }

    public LiveData<List<Account>> getAllAccounts() {
        return allAccounts;
    }
}
