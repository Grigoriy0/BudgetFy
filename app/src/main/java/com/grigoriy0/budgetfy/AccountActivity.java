package com.grigoriy0.budgetfy;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        if (savedInstanceState == null) {
            openAccountFragment();
        }
    }

    private void openAccountFragment() {
        final Fragment homeFragment = new TransactionsFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.accountActivity, homeFragment)
                .commit();
    }
}
