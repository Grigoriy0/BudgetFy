package com.grigoriy0.budgetfy.accountdetails;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.grigoriy0.budgetfy.AppDatabase;

import java.util.List;
import java.util.UUID;

public class TransactionRepository {
    private final TransactionDAO transactionDao;
    private final LiveData<List<Transaction>> transactions;
    private final UUID accountId;

    public TransactionRepository(@NonNull Application app, UUID accountId) {
        AppDatabase db = AppDatabase.getInstance(app.getApplicationContext());
        transactionDao = db.getTransactionDao();
        transactions = transactionDao.getByAccountId(accountId);
        this.accountId = accountId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.accountId = accountId;
        new AddTransactionTask().execute(transaction);
    }

    public void deleteAllTransactions() {
        new DeleteAllTransactionTask().execute(accountId);
    }

    public void deleteTransaction(Transaction transaction) {
        new DeleteTransactionTask().execute(transaction);
    }

    public void updateTransaction(Transaction transaction) {
        transaction.accountId = accountId;
        new UpdateTransactionTask().execute(transaction);
    }

    private class AddTransactionTask extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected Void doInBackground(Transaction... accounts) {
            transactionDao.insert(accounts);
            return null;
        }
    }

    private class UpdateTransactionTask extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected Void doInBackground(Transaction... accounts) {
            transactionDao.update(accounts[0]);
            return null;
        }
    }

    private class DeleteTransactionTask extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected Void doInBackground(Transaction... accounts) {
            transactionDao.delete(accounts[0]);
            return null;
        }
    }

    private class DeleteAllTransactionTask extends AsyncTask<UUID, Void, Void> {
        @Override
        protected Void doInBackground(UUID... id) {
            transactionDao.deleteById(id[0]);
            return null;
        }
    }
}
