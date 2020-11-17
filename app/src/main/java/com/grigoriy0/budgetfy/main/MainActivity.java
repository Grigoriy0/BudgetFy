package com.grigoriy0.budgetfy.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

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
    }

    public void showAccountDetails(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        UUID id = accounts.getValue().get(accountIndex).id;
        float value = accounts.getValue().get(accountIndex).currentValue;
        String name = accounts.getValue().get(accountIndex).name;
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT, id.toString());
        intent.putExtra(AccountActivity.EXTRA_VALUE, value);
        intent.putExtra(AccountActivity.EXTRA_NAME, name);
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
        new AddTransactionHelper(accounts.getValue().get(accountIndex).id,
                updater,
                accounts.getValue().get(accountIndex).currentValue,
                this, loss);
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
            case R.id.add_account_action:
                openAddAccountDialog();
                break;
            case R.id.remove_account_action:
                openRemoveAccountDialog(null);
                break;
            case R.id.rename_account_action:
                openRenameAccountDialog(null);
            default:
                break;
        }
        return true;
    }

    public void openRemoveAccountDialog(View ignored) {
        if (accounts.getValue() == null || accounts.getValue().size() == 0) {
            Toast.makeText(MainActivity.this, "Nothing to delete", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.delete_account_dialog,
                (ConstraintLayout) findViewById(R.id.layoutDeleteDialogContainer));
        builder.setView(view);
        final Account accountToDelete = accounts.getValue().get(accountIndex);
        String title = String.format("Delete %s ?", accountToDelete.name);
        ((TextView) view.findViewById(R.id.titleDelete)).setText(title);
        final AlertDialog dialog = builder.create();
        view.findViewById(R.id.yesDeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountViewModel.delete(accountToDelete);
                repositories.remove(accountIndex);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.noDeleteButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

    public void openRenameAccountDialog(View ignored) {
        if (accounts.getValue() == null ||
                accounts.getValue().size() == 0) {
            Toast.makeText(MainActivity.this, "Create an account first", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        final Account accountToRename = accounts.getValue().get(accountIndex);
        final View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.update_account_dialog,
                (LinearLayout) findViewById(R.id.layoutUpdateDialogContainer));
        builder.setView(view);
        String title = String.format("Rename %s ?", accountToRename.name);
        ((TextView) view.findViewById(R.id.titleRename)).setText(title);
        if (accountToRename.type.equals(Account.Type.CREDIT_CARD))
            ((RadioButton) view.findViewById(R.id.creditCardRadioRefactorAccount)).setChecked(true);
        else
            ((RadioButton) view.findViewById(R.id.walletRadioRefactorAccount)).setChecked(true);

        final AlertDialog dialog = builder.create();
        ((TextView) view.findViewById(R.id.inputField)).setText(accountToRename.name);
        view.findViewById(R.id.yesRenameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((TextView) view.findViewById(R.id.inputField)).getText().toString();
                if (name.trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Type non-empty string", Toast.LENGTH_SHORT).show();
                    return;
                }
                accountToRename.name = name;
                accountToRename.type = ((RadioButton) view.findViewById(R.id.walletRadioRefactorAccount)).isChecked() ?
                        Account.Type.WALLET : Account.Type.CREDIT_CARD;
                accountViewModel.update(accountToRename);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.noRenameButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

    private void openAddAccountDialog(){
        final View view = LayoutInflater.from(MainActivity.this).inflate(
                R.layout.activity_add_account,
                (LinearLayout) findViewById(R.id.addAccountContainer));
        new AddAccountHelper(MainActivity.this, view);
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
}