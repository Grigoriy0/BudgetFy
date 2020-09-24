package com.grigoriy0.budgetfy.accountdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.grigoriy0.budgetfy.R;

public class TransactionsAdapter extends ListAdapter<Transaction, TransactionsAdapter.ViewHolder> {
    private static final class DiffCallback extends DiffUtil.ItemCallback<Transaction> {

        @Override
        public boolean areItemsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Transaction oldItem, @NonNull Transaction newItem) {
            return oldItem.equals(newItem);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView sumView;
        private final TextView categoryView;
        private final TextView dateView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sumView = itemView.findViewById(R.id.sumView);
            categoryView = itemView.findViewById(R.id.categoryImage);
            dateView = itemView.findViewById(R.id.dateView);
        }

        public final void onBind(Transaction transaction) {
            sumView.setText(String.valueOf(transaction.getSum()));
            categoryView.setText(transaction.getCategory().toString());
            dateView.setText(transaction.getDateString());
        }
    }

    protected TransactionsAdapter() {
        super(new DiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = (LayoutInflater)
                parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = layoutInflater.inflate(
                R.layout.fragment_transaction_item,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getCurrentList().get(position));
    }
}
