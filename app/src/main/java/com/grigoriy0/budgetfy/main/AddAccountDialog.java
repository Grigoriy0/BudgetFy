package com.grigoriy0.budgetfy.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;

import java.util.UUID;

public class AddAccountDialog extends Dialog implements View.OnClickListener {
    private EditText nameText;
    private EditText start;
    private RadioGroup radioGroup;
    public MainActivity activity;

    public AddAccountDialog(@NonNull MainActivity activity) {
        super(activity);
        setContentView(R.layout.add_account_dialog);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Button yes = findViewById(R.id.yesAddAccountButton);
        yes.setOnClickListener(this);
        Button no = findViewById(R.id.noAddAccountButton);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        nameText = findViewById(R.id.nameAddAccount);
        start = findViewById(R.id.startValueAddAccount);
        radioGroup = findViewById(R.id.radioGroupAddAccount);
    }

    @Override
    public void onClick(View v) {
        if (findViewById(radioGroup.getCheckedRadioButtonId()) == null) {
            return;
        }
        String name = nameText.getText().toString().trim();
        if (name.isEmpty() || start.getText().toString().isEmpty()) {
            Toast.makeText(activity, "Enter non-empty data", Toast.LENGTH_LONG)
                    .show();
        }
        float startValue;
        try {
            startValue = Float.parseFloat(start.getText().toString());
        } catch (NumberFormatException ignored) {
            Toast.makeText(activity, "Enter correct value", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (startValue < 0.0f) {
            Toast.makeText(activity, "Enter positive money value", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        String type = ((RadioButton) findViewById(
                radioGroup.getCheckedRadioButtonId())).getText().toString();
        if (type.equals("wallet")) {
            type = Account.Type.WALLET;
        } else type = Account.Type.CREDIT_CARD;
        Account account = new Account(UUID.randomUUID(), name, startValue, type);
        account.currentValue = startValue;
        activity.accountViewModel.insert(account);
        activity.repositories.add(new TransactionRepository(activity.getApplication(), account.id));
        Toast.makeText(activity, "Account " + account.name + " added", Toast.LENGTH_SHORT)
                .show();
        activity.menu.findItem(R.id.deleteAccountMenuItem).setEnabled(true);
        activity.menu.findItem(R.id.editAccountMenuItem).setEnabled(true);
        dismiss();
    }
}
