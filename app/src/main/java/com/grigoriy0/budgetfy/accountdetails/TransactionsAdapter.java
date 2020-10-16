package com.grigoriy0.budgetfy.accountdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.grigoriy0.budgetfy.R;

import java.util.List;

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

    protected TransactionsAdapter() {
        super(new DiffCallback());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final LayoutInflater layoutInflater = (LayoutInflater)
                parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View view = layoutInflater.inflate(
                R.layout.transaction_item,
                parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(getCurrentList().get(position));
    }

    public void setTransactions(List<Transaction> transactions) {
        submitList(transactions);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView sumView;
        private final TextView categoryView;
        private final TextView dateView;
        private final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sumView = itemView.findViewById(R.id.sumView);
            categoryView = itemView.findViewById(R.id.categoryImage);
            dateView = itemView.findViewById(R.id.dateView);
            imageView = itemView.findViewById(R.id.transactionImageView);
        }

        public final void onBind(Transaction transaction) {
            String sum = String.valueOf(Float.valueOf(transaction.getSum()) / 10);
            sumView.setText(sum);
            categoryView.setText(transaction.getCategory().toString());
            dateView.setText(transaction.getDateString());
            if (transaction.isLoss())
                imageView.setImageResource(R.drawable.ic_down_90);
            else
                imageView.setImageResource(R.drawable.ic_up_90);
        }
    }
}
