package com.grigoriy0.budgetfy.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountCreator;
import com.grigoriy0.budgetfy.AccountRepository;
import com.grigoriy0.budgetfy.AppDatabase;
import com.grigoriy0.budgetfy.accountdetails.AccountActivity;
import com.grigoriy0.budgetfy.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter adapter;
    private ViewPager2 accountsViewPager;
    private LiveData<List<Account>> accounts;
    private FloatingActionButton lossActionButton;
    private FloatingActionButton increaseActionButton;
    private AccountRepository accountRepository;

    private int accountIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        lossActionButton = findViewById(R.id.fab_loss_action);
        increaseActionButton = findViewById(R.id.fab_increase_action);
        lossActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTransactionDialog(v);
            }
        });
        increaseActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTransactionDialog(v);
            }
        });
        if (savedInstanceState == null) {
            AppDatabase db = AppDatabase.getInstance(getApplicationContext());
            accounts = db.getAccountDao().getAllAccounts();
            accountRepository = AccountRepository.getInstance(getApplication());

            accountIndex = -1;
            if (accounts.getValue() == null || accounts.getValue().size() != 0)
                openAccountsViewPager();
        }
    }

    public void showAccountDetails(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra("accountId", accountIndex);
        startActivity(intent);
    }

    private void openAccountsViewPager() {
        accountsViewPager = findViewById(R.id.accountsViewPager);
        adapter = new ViewPagerAdapter(accounts.getValue(), accountsViewPager);
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
        accountIndex = 0;
    }

    private void openAddTransactionDialog(View button) {
        BottomSheetDialog dialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme);
        View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.buttom_sheet_layout,
                        (LinearLayout) findViewById(R.id.bottomSheetContainer),
                        false
                );
        dialog.setContentView(bottomSheetView);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_credit_card_action:
                openAddAccountActivity("CREDIT_CARD");
                break;
            case R.id.add_wallet_action:
                openAddAccountActivity("WALLET");
                break;
            case R.id.remove_account_action:
                // TODO
                break;
            default:
                break;
        }
        return true;
    }

    private void openAddAccountActivity(String type) {
        Intent intent = new Intent(this, AccountCreator.class);
        intent.putExtra("type", type);
        startActivity(intent);
    }
}