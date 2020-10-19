package com.grigoriy0.budgetfy.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountViewModel;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.AccountActivity;
import com.grigoriy0.budgetfy.accountdetails.Category;
import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private AccountViewModel accountViewModel;
    private ViewPagerAdapter adapter;
    private LiveData<List<Account>> accounts;
    private int accountIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        findViewById(R.id.fab_loss_action);
        findViewById(R.id.fab_increase_action);

        if (savedInstanceState == null) {
            accountViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
            accountViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
                @Override
                public void onChanged(List<Account> accounts) {
                    adapter.setAccounts(accounts);
                }
            });
            accounts = accountViewModel.getAllAccounts();
        }
        openAccountsViewPager();
    }

    public void showAccountDetails(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        UUID id = accounts.getValue().get(accountIndex).id;
        intent.putExtra(AccountActivity.EXTRA_ACCOUNT, id.toString());
        startActivity(intent);
    }

    private void openAccountsViewPager() {
        ViewPager2 accountsViewPage = findViewById(R.id.accountsViewPager);
        adapter = new ViewPagerAdapter(accounts.getValue());
        accountsViewPage.setAdapter(adapter);

        CompositePageTransformer transformer = new CompositePageTransformer();
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
            }
        });
        accountIndex = 0;
    }

    public void openAddLossTransactionDialog(View button) {
        final BottomSheetDialog dialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(
                        R.layout.activity_add_loss,
                        (LinearLayout) findViewById(R.id.lossContainer),
                        false
                );
        dialog.setContentView(bottomSheetView);
        final TextView commentView = bottomSheetView.findViewById(R.id.commentEditLoss);
        final TextView sumView = bottomSheetView.findViewById(R.id.valueEditLoss);
        final RadioGroup radioGroup = bottomSheetView.findViewById(R.id.radioGroupLoss);

        bottomSheetView.findViewById(R.id.applyLossButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentView.getText().toString();
                String category = ((RadioButton) bottomSheetView.findViewById(
                        radioGroup.getCheckedRadioButtonId())).getText().toString();
                long sum;
                try {
                    float value = Float.parseFloat(sumView.getText().toString());
                    value *= 10;
                    sum = (long) value;
                } catch (NumberFormatException ex) {
                    Toast.makeText(getApplicationContext(), "Type correct value", Toast.LENGTH_SHORT).show();
                    return;
                }
                Transaction transaction = new Transaction(sum, Category.fromString(category), comment, UUID.randomUUID());
                UUID id = accounts.getValue().get(accountIndex).id;

                TransactionRepository tr = new TransactionRepository(getApplication(), id);
                tr.addTransaction(transaction);
                Toast.makeText(MainActivity.this, "Loss added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.cancelLossButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

    public void openAddIncreaseTransactionDialog(View button) {
        final BottomSheetDialog dialog = new BottomSheetDialog(
                MainActivity.this, R.style.BottomSheetDialogTheme);
        final View bottomSheetView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.activity_add_increase,
                        (LinearLayout) findViewById(R.id.increaseContainer),
                        false
                );
        dialog.setContentView(bottomSheetView);
        final TextView commentView = bottomSheetView.findViewById(R.id.commentEditIncrease);
        final TextView sumView = bottomSheetView.findViewById(R.id.valueEditIncrease);
        final RadioGroup radioGroup = bottomSheetView.findViewById(R.id.radioGroupIncrease);

        bottomSheetView.findViewById(R.id.applyIncreaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentView.getText().toString();
                String category = ((RadioButton) bottomSheetView.findViewById(
                        radioGroup.getCheckedRadioButtonId())).getText().toString();
                long sum;
                try {
                    float value = Float.parseFloat(sumView.getText().toString());
                    value *= 10;
                    sum = (long) value;
                } catch (NumberFormatException ex) {
                    Toast.makeText(getApplicationContext(), "Type correct value", Toast.LENGTH_SHORT).show();
                    return;
                }
                Transaction transaction = new Transaction(sum, Category.fromString(category), comment, UUID.randomUUID());
                UUID id = accounts.getValue().get(accountIndex).id;
                TransactionRepository tr = new TransactionRepository(getApplication(), id);
                tr.addTransaction(transaction);
                Toast.makeText(MainActivity.this, "Increase added", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        bottomSheetView.findViewById(R.id.cancelIncreaseButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
                (ConstraintLayout) findViewById(R.id.layoutUpdateDialogContainer));
        builder.setView(view);
        String title = String.format("Rename %s ?", accountToRename.name);
        ((TextView) view.findViewById(R.id.titleRename)).setText(title);

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

    private void openAddAccountActivity(String type) {
        Intent addAccountIntent = new Intent(this, AddAccountActivity.class);
        addAccountIntent.putExtra(AddAccountActivity.EXTRA_TYPE, type);
        startActivityForResult(addAccountIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddAccountActivity.EXTRA_NAME);
            String type = data.getStringExtra(AddAccountActivity.EXTRA_TYPE);
            float startValue = data.getFloatExtra(AddAccountActivity.EXTRA_START, 0);
            Account account = new Account(UUID.randomUUID(), name, startValue, type);
            accountViewModel.insert(account);
            Toast.makeText(getApplicationContext(), "Account " + account.name + " added", Toast.LENGTH_SHORT).show();
        }
    }
}