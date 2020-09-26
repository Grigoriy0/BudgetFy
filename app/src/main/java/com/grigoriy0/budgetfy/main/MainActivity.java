package com.grigoriy0.budgetfy.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountActivity;
import com.grigoriy0.budgetfy.R;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private ViewPager2 accountsViewPager;
    private List<Account> accounts;
    private FloatingActionButton lossActionButton;
    private FloatingActionButton increaseActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        accounts = Arrays.asList(
                new Account("Visa", 400, 293, Account.Type.CREDIT_CARD),
                new Account("Mastercard", 190, 0.54f, Account.Type.CREDIT_CARD),
                new Account("Cache", 50, 23.51f, Account.Type.WALLET));

        lossActionButton = findViewById(R.id.fab_loss_action);
        increaseActionButton = findViewById(R.id.fab_increase_action);
        lossActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        increaseActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if (savedInstanceState == null)
            openAccountsViewPager();
    }

    public void showAccountDetails(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private void openAccountsViewPager() {
        accountsViewPager = findViewById(R.id.accountsViewPager);
        adapter = new ViewPagerAdapter(accounts, accountsViewPager);
        accountsViewPager.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        accountsViewPager.setPageTransformer(transformer);
    }

    public void clickAdd(View view) {
        // TODO
    }
}