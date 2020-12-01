package com.grigoriy0.budgetfy.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountViewModel;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.AccountActivity;
import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    protected AccountViewModel accountViewModel;
    private ViewPagerAdapter adapter;
    private LiveData<List<Account>> accounts;
    private int accountIndex;
    private PieChartManager pieChartManager;
    protected List<TransactionRepository> repositories;
    private TransactionRepository currentAccTransRepo;
    private AccountUpdater updater;
    protected Menu menu;
    private FloatingActionsMenu fabMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findViewById(R.id.fab_loss_action);
        findViewById(R.id.fab_increase_action);

        accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
        accountViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                adapter.setAccounts(accounts);
            }
        });
        accounts = accountViewModel.getAllAccounts();
        openAccountsViewPager();
        createPieChart();
        fabMenu = findViewById(R.id.floatingActionMenu);

    }

    public void showAccountDetails(View view) {
        if (accounts.getValue() == null)
            return;
        Intent intent = new Intent(this, AccountActivity.class);
        UUID id = accounts.getValue().get(accountIndex).id;
        float value = accounts.getValue().get(accountIndex).currentValue;
        String name = accounts.getValue().get(accountIndex).name;
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT, id.toString());
        intent.putExtra(AccountActivity.EXTRA_VALUE, value);
        intent.putExtra(AccountActivity.EXTRA_NAME, name);
        fabMenu.collapse();
        startActivityForResult(intent, 1);
    }

    private void openAccountsViewPager() {
        ViewPager2 accountsViewPage = findViewById(R.id.accountsViewPager);
        adapter = new ViewPagerAdapter(accounts.getValue());
        accountsViewPage.setAdapter(adapter);

        final CompositePageTransformer transformer = new CompositePageTransformer();
        transformer.addTransformer(new MarginPageTransformer(40));
        transformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        accountsViewPage.setPageTransformer(transformer);
        accountsViewPage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                accountIndex = position;
                if (repositories == null) {
                    repositories = new ArrayList<>();
                    for (Account account : accounts.getValue()) {
                        repositories.add(new TransactionRepository(getApplication(), account.id));
                    }
                }
                if (repositories.isEmpty())
                    return;
                currentAccTransRepo = repositories.get(position);
                currentAccTransRepo.getTransactions().observeForever(new Observer<List<Transaction>>() {
                    @Override
                    public void onChanged(List<Transaction> transactions) {
                        pieChartManager.update(currentAccTransRepo.getTransactions().getValue());
                    }
                });
                updater = new AccountUpdater(accounts.getValue().get(accountIndex),
                        accountViewModel, adapter);
            }
        });
        accountIndex = 0;
    }

    public void openAddTransactionDialog(View button) {
        boolean loss = button.getId() == R.id.fab_loss_action;
        if (accounts.getValue() == null || accounts.getValue().size() == 0) {
            Toast.makeText(MainActivity.this, "Add account first", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        AddTransactionDialog dialog = new AddTransactionDialog(this,
                accounts.getValue().get(accountIndex).id,
                updater,
                accounts.getValue().get(accountIndex).currentValue,
                loss);
        fabMenu.collapse();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addAccountMenuItem)
            openAddAccountDialog();
        else if (id == R.id.deleteAccountMenuItem)
            openDeleteAccountDialog();
        else if (id == R.id.editAccountMenuItem)
            openEditAccountDialog();
        else
            return false;
        return true;
    }

    public void openDeleteAccountDialog() {
        if (accounts.getValue() == null || accounts.getValue().size() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        DeleteAccountDialog dialog = new DeleteAccountDialog(MainActivity.this,
                accounts.getValue().get(accountIndex), accountIndex);
        fabMenu.collapse();
        dialog.show();
    }

    public void openEditAccountDialog() {
        if (accounts.getValue() == null ||
                accounts.getValue().size() == 0) {
            Toast.makeText(MainActivity.this, "Create an account first", Toast.LENGTH_SHORT).show();
            return;
        }
        EditAccountDialog dialog = new EditAccountDialog(MainActivity.this, accounts.getValue().get(accountIndex));
        fabMenu.collapse();
        dialog.show();
    }

    private void openAddAccountDialog() {
        if (repositories == null)
            repositories = new ArrayList<>();
        AddAccountDialog dialog = new AddAccountDialog(MainActivity.this);
        fabMenu.collapse();
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            updater.applyTransactions(currentAccTransRepo.getTransactions().getValue());
        }
    }

    private void createPieChart() {
        pieChartManager = new PieChartManager(true, findViewById(R.id.pieChart));
    }

    public void showPopup(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(listener);
        popupMenu.inflate(R.menu.popup_menu);
        if (accounts.getValue() == null || accounts.getValue().isEmpty()) {
            popupMenu.getMenu().findItem(R.id.deleteAccountAction).setEnabled(false);
            popupMenu.getMenu().findItem(R.id.editAccountAction).setEnabled(false);
            menu.findItem(R.id.deleteAccountMenuItem).setEnabled(false);
            menu.findItem(R.id.editAccountMenuItem).setEnabled(false);
        }
        fabMenu.collapse();
        popupMenu.show();
    }

    private final PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.addAccountAction)
                openAddAccountDialog();
            else if (id == R.id.editAccountAction)
                openEditAccountDialog();
            else if (id == R.id.deleteAccountAction)
                openDeleteAccountDialog();
            else
                return false;
            return true;
        }
    };
}