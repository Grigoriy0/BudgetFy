package com.grigoriy0.budgetfy.accountdetails;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.grigoriy0.budgetfy.AppDatabase;

import java.util.List;

public class TransactionRepository {
    private TransactionDAO transactionDAO;
    private LiveData<List<Transaction>> transactions;
    private int accountId;

    public TransactionRepository(@NonNull Application app, int accountId) {
        AppDatabase db = AppDatabase.getInstance(app.getApplicationContext());
        transactionDAO = db.getTransactionDao();
        transactions = transactionDAO.getByAccountId(accountId);
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        transaction.setAccountId(accountId);
        new AddTransactionTask().execute(transaction);
    }

    public void deleteAllTransactions() {
        new DeleteAllTransactionTask().execute(accountId);
    }

    public void removeTransaction(Transaction transaction) {
        new DeleteTransactionTask().execute(transaction);
    }

    public void updateTransaction(Transaction transaction) {
        transaction.setAccountId(accountId);
        new UpdateTransactionTask().execute(transaction);
    }

    private class AddTransactionTask extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected Void doInBackground(Transaction... accounts) {
            transactionDAO.insert(accounts);
            return null;
        }
    }

    private class UpdateTransactionTask extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected Void doInBackground(Transaction... accounts) {
            transactionDAO.update(accounts[0]);
            return null;
        }
    }

    private class DeleteTransactionTask extends AsyncTask<Transaction, Void, Void> {
        @Override
        protected Void doInBackground(Transaction... accounts) {
            transactionDAO.delete(accounts[0]);
            return null;
        }
    }

    private class DeleteAllTransactionTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... id) {
            transactionDAO.deleteById(Integer.parseInt(id[0].toString()));
            return null;
        }
    }
}
