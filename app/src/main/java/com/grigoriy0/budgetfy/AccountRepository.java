package com.grigoriy0.budgetfy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepository {
    AccountDAO accountDAO;
    private LiveData<List<Account>> accounts;

    public AccountRepository(@NonNull Application app) {
        AppDatabase db = AppDatabase.getInstance(app.getApplicationContext());
        accountDAO = db.getAccountDao();
        accounts = accountDAO.getAllAccounts();
    }

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        new AddAccount().execute(account);
    }

    public void deleteAccount(Account account) {
        new DeleteAccount().execute(account);
    }

    public void updateAccount(Account account) {
        new UpdateAccount().execute(account);
    }

    private class AddAccount extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.insertAll(accounts);
            return null;
        }
    }

    private class UpdateAccount extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.update(accounts[0]);
            return null;
        }
    }

    private class DeleteAccount extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.delete(accounts[0]);
            return null;
        }
    }
}
