package com.grigoriy0.budgetfy.main;


import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountViewModel;
import com.grigoriy0.budgetfy.accountdetails.Transaction;

import java.util.List;

public class AccountUpdater {
    private final Account account;
    private final AccountViewModel accountViewModel;
    private final ViewPagerAdapter adapter;

    AccountUpdater(Account account,
                   AccountViewModel accountViewModel,
                   ViewPagerAdapter adapter) {
        this.account = account;
        this.adapter = adapter;
        this.accountViewModel = accountViewModel;
    }

    public void applyTransactions(List<Transaction> transactionList) {
        account.currentValue = account.startValue;
        for (Transaction transaction : transactionList) {
            if (transaction.loss)
                account.currentValue -= (float) transaction.sum / 100;
            else
                account.currentValue += (float) transaction.sum / 100;
        }
        accountViewModel.update(account);
        adapter.notifyDataSetChanged();
    }

    public void addTransaction(Transaction transaction) {
        if (transaction.loss)
            account.currentValue -= (float) transaction.sum / 100;
        else
            account.currentValue += (float) transaction.sum / 100;
        accountViewModel.update(account);
        adapter.notifyDataSetChanged();
    }
}
