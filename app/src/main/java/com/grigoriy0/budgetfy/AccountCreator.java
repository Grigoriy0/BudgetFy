package com.grigoriy0.budgetfy;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.List;

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
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
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
        // Add this account to account_table
        AppDatabase db = AppDatabase.getInstance(getApplicationContext());
        db.getAccountDao().insertAll(account);
        finish();
    }

    public void onCancelClick(View view) {
        finish();
    }
}
