package com.grigoriy0.budgetfy.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountActivity;
import com.grigoriy0.budgetfy.AccountFragment;
import com.grigoriy0.budgetfy.ViewPagerAdapter;
import com.grigoriy0.budgetfy.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private ViewPager2 accountsViewPager;
    private List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        accounts = Arrays.asList(
                new Account("Visa", 400, 293, Account.Type.CREDIT_CARD),
                new Account("Mastercard", 190, 0.54f, Account.Type.CREDIT_CARD),
                new Account("Cache", 50, 23.51f, Account.Type.WALLET));

        if (savedInstanceState == null)
            openAccountsViewPager();
    }

    public void showAccountDetails(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private void openAccountsViewPager() {
        accountsViewPager = findViewById(R.id.accountsViewPager);
        adapter = new ViewPagerAdapter(accounts);
        accountsViewPager.setAdapter(adapter);
    }
}