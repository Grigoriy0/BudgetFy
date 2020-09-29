package com.grigoriy0.budgetfy;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepository {
    private LiveData<List<Account>> accounts;
    AccountDAO accountDAO;

    private AccountRepository(@NonNull Application app) {
        AppDatabase db = AppDatabase.getInstance(app.getApplicationContext());
        accountDAO = db.getAccountDao();
        accounts = accountDAO.getAllAccounts();
    }

    public LiveData<List<Account>> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        new AddAccount().doInBackground(account);
    }

    public void deleteAccount(Account account) {
        new DeleteAccount().doInBackground(account);
    }

    public static AccountRepository getInstance(@NonNull Application app) {
        if (instance == null)
            instance = new AccountRepository(app);
        return instance;
    }

    private static AccountRepository instance;

    private class AddAccount extends AsyncTask<Account, Void, Void> {
        @Override
        protected Void doInBackground(Account... accounts) {
            accountDAO.insertAll(accounts);
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
