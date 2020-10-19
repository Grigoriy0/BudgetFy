package com.grigoriy0.budgetfy;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionDAO;

import java.util.UUID;

@Database(entities = {Account.class, Transaction.class}, version = AppDatabase.DATABASE_VERSION, exportSchema = false)
@TypeConverters({Account.UUIDConverter.class, Transaction.DateConverter.class, Transaction.CategoryConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "com.grigoriy0.budgetfy_db";
    public static final int DATABASE_VERSION = 1;
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
                    DATABASE_NAME)
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
            accountDAO.insertAll(new Account(UUID.randomUUID(), "Cache", 0, Account.Type.WALLET));
            return null;
        }
    }
}
