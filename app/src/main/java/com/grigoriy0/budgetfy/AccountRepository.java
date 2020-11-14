package com.grigoriy0.budgetfy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepository {
    private final AccountDAO accountDAO;
    private final LiveData<List<Account>> accounts;

    public AccountRepository(@NonNull Application app) {
        AppDatabase db = AppDatabase.getInstance(app.getApplicationContext());
        accountDAO = db.getAccountDao();
        accounts = accountDAO.getAllAccounts();
    }

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        new AddAccountTask().execute(account);
    }

    public void deleteAccount(Account account) {
        new DeleteAccountTask().execute(account);
    }

    public void updateAccount(Account account) {
        new UpdateAccountTask().execute(account);
    }

    private class AddAccountTask extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.insertAll(accounts);
            return null;
        }
    }

    private class UpdateAccountTask extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.update(accounts[0]);
            return null;
        }
    }

    private class DeleteAccountTask extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.delete(accounts[0]);
            return null;
        }
    }
}
