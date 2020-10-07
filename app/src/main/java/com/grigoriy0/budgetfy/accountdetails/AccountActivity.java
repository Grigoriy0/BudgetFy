package com.grigoriy0.budgetfy.accountdetails;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grigoriy0.budgetfy.R;

public class AccountActivity extends AppCompatActivity {
    public static final String EXTRA_ACCOUNT =
            "com.grigoriy0.budgetfy.accountdetails.EXTRA_ACCOUNT";
    private int accountId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        accountId = getIntent().getIntExtra(EXTRA_ACCOUNT, -1);
        if (accountId == -1) {
            Toast.makeText(getApplicationContext(), "ERROR Extra not transferred", Toast.LENGTH_LONG).show();
            return;
        }
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
