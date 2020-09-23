package com.grigoriy0.budgetfy.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountActivity;
import com.grigoriy0.budgetfy.AccountAdapter;
import com.grigoriy0.budgetfy.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AccountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showAccounts();
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private void showAccounts() {
        ViewPager2 viewPager2 = findViewById(R.id.accountsViewPager);
        List<Account> list = Arrays.asList(
                new Account("Visa", 400, 293, Account.Type.CREDIT_CARD),
                new Account("Mastercard", 190, 0.54f, Account.Type.CREDIT_CARD),
                new Account("Cache", 50, 23.51f, Account.Type.WALLET)
        );
        adapter = new AccountAdapter(list);
        viewPager2.setAdapter(adapter);
    }
}