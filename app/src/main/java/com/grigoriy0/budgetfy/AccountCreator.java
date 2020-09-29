package com.grigoriy0.budgetfy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

public class AccountCreator extends Activity {
    private Account account;
    private EditText startValue;
    private EditText accountName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_account_layout);
        startValue = findViewById(R.id.startValueEditText);
        accountName = findViewById(R.id.accountNameEditText);
        account = new Account(0, "", 0, 0, "");

        account.setType(getIntent().getStringExtra("type"));
    }

    @SuppressLint("ShowToast")
    public void onSaveClick(View view) {
        float value;
        String name;
        try {
            value = Float.parseFloat(startValue.getText().toString());
            name = accountName.getText().toString();
            if (value <= 0 || name.isEmpty())
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            return;
        }
        account.setStartValue(value);
        account.setCurrentValue(value);
        account.setName(name);
        AccountRepository ar = AccountRepository.getInstance(getApplication());
        ar.addAccount(account); // here error
        finish();
    }

    public void onCancelClick(View view) {
        finish();
    }
}
