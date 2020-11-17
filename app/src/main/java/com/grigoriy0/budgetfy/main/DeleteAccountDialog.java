package com.grigoriy0.budgetfy.main;

import android.app.Dialog;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.R;

public class DeleteAccountDialog extends Dialog implements View.OnClickListener {
    private MainActivity activity;
    private Account accountToDelete;
    private final int accountIndex;

    public DeleteAccountDialog(@NonNull MainActivity mainActivity, Account account, int accountIndex) {
        super(mainActivity);
        this.activity = mainActivity;
        this.accountToDelete = account;
        this.accountIndex = accountIndex;

        setContentView(R.layout.delete_account_dialog);
        Button yes = findViewById(R.id.yesDeleteAccount);
        yes.setOnClickListener(this);
        Button no = findViewById(R.id.noDeleteAccount);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setTitle(String.format("Delete %s?", account.name));
    }

    @Override
    public void onClick(View v) {
        activity.accountViewModel.delete(accountToDelete);
        activity.repositories.remove(accountIndex);
    }
}
