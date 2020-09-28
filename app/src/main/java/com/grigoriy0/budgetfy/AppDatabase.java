package com.grigoriy0.budgetfy;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.grigoriy0.budgetfy.accountdetails.Transaction;

@Database(entities = {Account.class, Transaction.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDAO getAccountDao();

    public abstract TransactionDAO getTransactionDao();

    public synchronized static AppDatabase getInstance(Context context) {
        if (instance == null)
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class,
                    "budgetfy_db")
                    .fallbackToDestructiveMigration()
                    .build();
        return instance;
    }

    private static AppDatabase instance;
}
