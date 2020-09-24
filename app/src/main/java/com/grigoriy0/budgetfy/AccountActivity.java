package com.grigoriy0.budgetfy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionsFragment;

import java.util.Arrays;
import java.util.List;

public class AccountActivity extends AppCompatActivity {

    private List<Transaction> transactions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        transactions = Arrays.asList(new Transaction(-12.67f, Category.CAFE),
                new Transaction(-5.50f, Category.BARBER),
                new Transaction(-0.65f, Category.TRANSPORT),
                new Transaction(-5.50f, Category.UNIVERSITY),
                new Transaction(-0.65f, Category.FOOD),
                new Transaction(-5.50f, Category.RENT),
                new Transaction(-0.65f, Category.TRANSPORT),
                new Transaction(-5.50f, Category.MISCELLANEOUS),
                new Transaction(-0.65f, Category.PHONE));

        if (savedInstanceState == null) {
            openTransactionFragment();
        }
    }

    private void openTransactionFragment() { // show scene with transactions
        final Fragment detailsFragment = new TransactionsFragment(transactions);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.accountActivity, detailsFragment)
                .commit();
    }
}
