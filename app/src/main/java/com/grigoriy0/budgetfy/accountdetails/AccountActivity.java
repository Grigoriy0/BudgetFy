package com.grigoriy0.budgetfy.accountdetails;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grigoriy0.budgetfy.AppDatabase;
import com.grigoriy0.budgetfy.R;

import java.util.List;

public class AccountActivity extends AppCompatActivity {
    public static final String EXTRA_ACCOUNT =
            "com.grigoriy0.budgetfy.accountdetails.EXTRA_ACCOUNT";

    private List<Transaction> transactions;
    private int account;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        if (savedInstanceState != null) {
            account = savedInstanceState.getInt("EXTRA_ACCOUNT");
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            transactions = db.getTransactionDao().getByAccountId(account);
//            setTitle(); // TODO set account name
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
