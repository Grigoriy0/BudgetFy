package com.grigoriy0.budgetfy.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.grigoriy0.budgetfy.Account;
import com.grigoriy0.budgetfy.R;

import java.util.LinkedList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.PagerVH> {
    private List<Account> accounts;
    private ViewPager2 pager2;

    ViewPagerAdapter(List<Account> accounts, ViewPager2 viewPager2) {
        if (accounts == null){
            accounts = new LinkedList<>();
        }
        this.accounts = accounts;
        this.pager2 = viewPager2;
    }

    @NonNull
    @Override
    public PagerVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PagerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.account_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PagerVH holder, int position) {
        holder.onBind(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        if (accounts == null)
            return 0;
        return accounts.size();
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
        notifyDataSetChanged();
    }

    static class PagerVH extends RecyclerView.ViewHolder {
        private TextView accountName;

        PagerVH(View view) {
            super(view);
            accountName = view.findViewById(R.id.accountName);
        }

        public final void onBind(Account account) {
            accountName.setText(account.getName());
        }
    }

}
