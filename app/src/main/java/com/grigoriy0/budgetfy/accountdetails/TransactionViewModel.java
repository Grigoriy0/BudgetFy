package com.grigoriy0.budgetfy.accountdetails;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.UUID;

public class TransactionViewModel extends AndroidViewModel {
    private TransactionRepository repository;
    private LiveData<List<Transaction>> accountTransactions;

    public TransactionViewModel(@NonNull Application application) {
        super(application);
    }

    public void init(UUID accountId) {
        repository = new TransactionRepository(getApplication(), accountId);
        accountTransactions = repository.getTransactions();
    }

    public void insert(Transaction transaction) {
        repository.addTransaction(transaction);
    }

    public void update(Transaction transaction) {
        repository.updateTransaction(transaction);
    }

    public void delete(Transaction transaction) {
        repository.deleteTransaction(transaction);
    }

    public void deleteAll() {
        repository.deleteAllTransactions();
    }

    public LiveData<List<Transaction>> getAccountTransactions() {
        return accountTransactions;
    }
}
