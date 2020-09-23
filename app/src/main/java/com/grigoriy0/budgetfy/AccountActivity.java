package com.grigoriy0.budgetfy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.grigoriy0.budgetfy.accountdetails.TransactionsFragment;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (savedInstanceState == null) {
            openTransactionFragment();
        }
    }

    private void openTransactionFragment() { // show scene with transactions
        final Fragment detailsFragment = new TransactionsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.accountActivity, detailsFragment)
                .commit();
    }
}
