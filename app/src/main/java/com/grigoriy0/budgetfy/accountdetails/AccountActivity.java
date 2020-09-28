package com.grigoriy0.budgetfy.accountdetails;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grigoriy0.budgetfy.AppDatabase;
import com.grigoriy0.budgetfy.R;

import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private List<Transaction> transactions;
    private int account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        if (savedInstanceState != null) {
            account = savedInstanceState.getInt("accountId");
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            transactions = db.getTransactionDao().getByAccountId(account);

    //        transactions = Arrays.asList(new Transaction(-12.67f, Category.CAFE, 1),
    //                new Transaction(-5.50f, Category.BARBER, 1),
    //                new Transaction(-0.65f, Category.TRANSPORT, 1),
    //                new Transaction(-5.50f, Category.UNIVERSITY, 1),
    //                new Transaction(-0.65f, Category.FOOD, 1),
    //                new Transaction(-5.50f, Category.RENT, 1),
    //                new Transaction(-0.65f, Category.TRANSPORT, 1),
    //                new Transaction(-5.50f, Category.MISCELLANEOUS, 1),
    //                new Transaction(-0.65f, Category.PHONE, 1));

            showTransactions();
        }
    }

    private void showTransactions() { // show scene with transactions
        final Fragment detailsFragment = new TransactionsFragment(transactions);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.accountActivity, detailsFragment)
                .commit();
    }
}
