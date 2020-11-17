package com.grigoriy0.budgetfy.main;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountViewModel;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;

import java.util.List;
import java.util.UUID;

public class AddAccountHelper {
    AddAccountHelper(final MainActivity mainActivity,
                     final View view) {
        final AccountViewModel viewModel = mainActivity.accountViewModel;
        final Context applicationContext = mainActivity.getApplicationContext();
        final List<TransactionRepository> repositories = mainActivity.repositories;
        final Application app = mainActivity.getApplication();
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        final EditText nameEditText = view.findViewById(R.id.nameAddAccount);
        final EditText startValue = view.findViewById(R.id.startValueAddAccount);
        final RadioGroup radioGroup = view.findViewById(R.id.radioGroupAddAccount);
        view.findViewById(R.id.yesAddAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view.findViewById(radioGroup.getCheckedRadioButtonId()) == null) {
                    return;
                }
                String name = nameEditText.getText().toString().trim();
                if (name.isEmpty() || startValue.getText().toString().isEmpty()) {
                    Toast.makeText(applicationContext, "Enter non-empty data", Toast.LENGTH_LONG)
                            .show();
                }
                float start;
                try {
                    start = Float.parseFloat(startValue.getText().toString());
                } catch (NumberFormatException ignored) {
                    Toast.makeText(applicationContext, "Enter correct value", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (start < 0.0f) {
                    Toast.makeText(applicationContext, "Enter positive money value", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                String type = ((RadioButton) view.findViewById(
                        radioGroup.getCheckedRadioButtonId())).getText().toString();
                if (type.equals("wallet")){
                    type = Account.Type.WALLET;
                }
                else type = Account.Type.CREDIT_CARD;
                Account account = new Account(UUID.randomUUID(), name, start, type);
                account.currentValue = start;
                viewModel.insert(account);
                repositories.add(new TransactionRepository(app, account.id));
                Toast.makeText(applicationContext, "Account " + account.name + " added", Toast.LENGTH_SHORT)
                        .show();
                mainActivity.menu.findItem(R.id.deleteAccountMenuItem).setEnabled(true);
                mainActivity.menu.findItem(R.id.editAccountMenuItem).setEnabled(true);
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.noAddAccountButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();

    }
}