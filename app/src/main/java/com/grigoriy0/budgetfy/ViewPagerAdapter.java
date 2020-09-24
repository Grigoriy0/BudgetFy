package com.grigoriy0.budgetfy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.AccountViewHolder> {

    private List<Account> accounts;

    public ViewPagerAdapter(List<Account> accounts) {
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.fragment_transaction_item,
                parent,
                false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        holder.setData(accounts.get(position));
    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    static class AccountViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView accountName;
        private TextView value;

        AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            accountName = itemView.findViewById(R.id.accountName);
            value = itemView.findViewById(R.id.currentValue);
        }

        public void setData(Account account) {
            accountName.setText(account.getName());
            value.setText(account.getCurrentValue().toString());
            // TODO set selecting image
//            if (account.getType() == Account.Type.CREDIT_CARD)
//                imageView =
        }
    }

}
