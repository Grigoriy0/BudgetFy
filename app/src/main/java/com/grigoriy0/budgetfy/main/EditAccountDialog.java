package com.grigoriy0.budgetfy.main;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.R;

public class EditAccountDialog extends Dialog implements View.OnClickListener {
    private EditText nameText;
    private EditText start;
    private RadioGroup radioGroup;
    public MainActivity activity;
    private Account accountToRename;

    public EditAccountDialog(@NonNull MainActivity mainActivity, Account account) {
        super(mainActivity);
        this.activity = mainActivity;
        this.accountToRename = account;
        setContentView(R.layout.edit_account_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(String.format("Rename %s ?", accountToRename.name));
        Button yes = findViewById(R.id.yesEditButton);
        yes.setOnClickListener(this);
        Button no = findViewById(R.id.noEditButton);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        nameText = findViewById(R.id.nameEditAccount);
        nameText.setText(accountToRename.name);
        start = findViewById(R.id.startValueAddAccount);
        radioGroup = findViewById(R.id.radioGroupAddAccount);
    }

    @Override
    public void onClick(View v) {
        String name = ((TextView) findViewById(R.id.nameEditAccount)).getText().toString();
        if (name.trim().isEmpty()) {
            Toast.makeText(activity, "Type non-empty string", Toast.LENGTH_SHORT).show();
            return;
        }
        accountToRename.name = name;
        accountToRename.type = ((RadioButton) findViewById(R.id.walletRadioEditAccount)).isChecked() ?
                Account.Type.WALLET : Account.Type.CREDIT_CARD;
        activity.accountViewModel.update(accountToRename);
        dismiss();
    }
}
