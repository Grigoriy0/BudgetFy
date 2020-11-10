package com.grigoriy0.budgetfy.main;


import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.AccountViewModel;
import com.grigoriy0.budgetfy.accountdetails.Transaction;

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

    public void addTransaction(Transaction transaction) {
        if (transaction.loss)
            account.currentValue -= (float) transaction.sum / 100;
        else
            account.currentValue += (float) transaction.sum / 100;
        accountViewModel.update(account);
        adapter.notifyDataSetChanged();
    }

    public AccountViewModel getAccountViewModel() {
        return accountViewModel;
    }

    public ViewPagerAdapter getAdapter() {
        return adapter;
    }
}
