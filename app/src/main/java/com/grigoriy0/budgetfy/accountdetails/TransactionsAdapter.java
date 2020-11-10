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
    private final TransactionsFragment fragment;

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

    protected TransactionsAdapter(TransactionsFragment fragment) {
        super(new DiffCallback());
        this.fragment = fragment;
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
        view.setOnClickListener(fragment.transactionItemListener);
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
        private final TextView idTextView;
        private final TextView commentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sumView = itemView.findViewById(R.id.sumTextView);
            categoryView = itemView.findViewById(R.id.categoryTextView);
            dateView = itemView.findViewById(R.id.dateTextView);
            imageView = itemView.findViewById(R.id.transactionImageView);
            idTextView = itemView.findViewById(R.id.transactionIdTextView);
            commentTextView = itemView.findViewById(R.id.transactionCommentTextView);
        }

        public final void onBind(Transaction transaction) {
            String sum = (transaction.loss ? "-" : "+") + (float) transaction.sum / 100;
            sumView.setText(sum);
            categoryView.setText(transaction.category.toString());
            dateView.setText(transaction.getDateString());
            if (transaction.loss)
                imageView.setImageResource(R.drawable.ic_down_90);
            else
                imageView.setImageResource(R.drawable.ic_up_90);
            idTextView.setText(String.valueOf(transaction.id));
            commentTextView.setText(transaction.comment);
        }
    }
}
