package com.grigoriy0.budgetfy.accountdetails;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grigoriy0.budgetfy.R;

import java.util.UUID;

public class AccountActivity extends AppCompatActivity {
    public static final String EXTRA_ACCOUNT =
            "com.grigoriy0.budgetfy.accountdetails.EXTRA_ACCOUNT";
    private UUID accountId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        accountId = UUID.fromString(getIntent().getStringExtra(EXTRA_ACCOUNT));
        showTransactions();
    }

    private void showTransactions() {
        final Fragment detailsFragment = new TransactionsFragment(accountId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.accountActivity, detailsFragment)
                .commit();
    }
}
