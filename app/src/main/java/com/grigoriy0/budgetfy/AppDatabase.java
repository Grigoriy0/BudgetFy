package com.grigoriy0.budgetfy;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.grigoriy0.budgetfy.accountdetails.Transaction;

@Database(entities = {Account.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute(); // set default account
        }
    };

    public abstract AccountDAO getAccountDao();

    public abstract TransactionDAO getTransactionDao();


    public synchronized static AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "budgetfy_db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        return instance;
    }

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private AccountDAO accountDAO;
        private PopulateDBAsyncTask(AppDatabase db) {
            accountDAO = db.getAccountDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            accountDAO.insertAll(new Account(1, "Cache", 0, 0, Account.Type.WALLET));
            return null;
        }
    }
}
