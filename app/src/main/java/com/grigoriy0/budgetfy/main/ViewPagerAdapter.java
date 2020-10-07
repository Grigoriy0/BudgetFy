package com.grigoriy0.budgetfy.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    ViewPagerAdapter(List<Account> accounts) {
        if (accounts == null){
            accounts = new LinkedList<>();
        }
        this.accounts = accounts;
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
        private TextView currentValue;
        private ImageView imageView;

        PagerVH(View view) {
            super(view);
            accountName = view.findViewById(R.id.accountName);
            currentValue = view.findViewById(R.id.currentValue);
            imageView = view.findViewById(R.id.categoryImage);
        }

        public final void onBind(Account account) {
            accountName.setText(account.getName());
            currentValue.setText(account.getCurrentValue().toString() + " BYN");
            if (account.getType().equals(Account.Type.WALLET))
                imageView.setImageResource(R.drawable.ic_wallet_90);
            else
                imageView.setImageResource(R.drawable.ic_credit_card_90);
        }
    }
}
