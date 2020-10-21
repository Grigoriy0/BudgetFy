package com.grigoriy0.budgetfy.accountdetails;

import android.app.AlertDialog;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.grigoriy0.budgetfy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TransactionsFragment extends Fragment {
    private TransactionsAdapter adapter;
    private TransactionViewModel viewModel;
    private RecyclerView recyclerView;
    private final UUID accountId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_transaction, container, false);
        recyclerView = parentView.findViewById(R.id.transactionsView);
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        viewModel.init(accountId);
        viewModel.getAccountTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);
            }
        });
        adapter = new TransactionsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(parentView.getContext()));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return parentView;
    }

    public TransactionsFragment(UUID accountId) {
        this.accountId = accountId;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            try {
                deletedTransaction = new Transaction(viewModel.getAccountTransactions().getValue().get(position));
            } catch (Exception ignored) {
            }
            viewModel.delete(deletedTransaction);
            String msg = String.format("%s(%d) deleted", deletedTransaction.comment, deletedTransaction.sum);
            Snackbar.make(recyclerView, msg, Snackbar.LENGTH_LONG)
                    .setAction("Undo", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            viewModel.insert(deletedTransaction);
                        }
                    }).show();
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addBackgroundColor(ContextCompat.getColor(getContext(), R.color.red))
                    .addActionIcon(R.drawable.ic_delete_24)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };

    Transaction deletedTransaction = null;
    View.OnClickListener transactionItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View transactionView) {
            UUID id = UUID.fromString(
                    ((TextView) transactionView.findViewById(R.id.transactionIdTextView)
                    ).getText().toString());
            long sum = (long) (Float.parseFloat(((TextView) transactionView.findViewById(R.id.sumTextView)).getText().toString()) * 100);
            Category category = Category.fromString(((TextView) transactionView.findViewById(R.id.categoryTextView)).getText().toString());
            String comment = ((TextView) transactionView.findViewById(R.id.transactionCommentTextView)).getText().toString();
            Date date;
            try {
                date = (Transaction.DATE_FORMAT).parse(
                        ((TextView) transactionView.findViewById(R.id.dateTextView)).getText().toString());
            } catch (ParseException e) {
                date = Calendar.getInstance().getTime();
            }

            final Transaction transaction = new Transaction(sum, category, comment, accountId);
            transaction.id = id;
            transaction.date = date;
            transaction.loss = category.isLoss();
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View view = LayoutInflater.from(getContext()).inflate(
                    R.layout.transaction_details_dialog,
                    (LinearLayout) getView().findViewById(R.id.transactionDetailsContainer));
            builder.setView(view);

            final TextView commentView = view.findViewById(R.id.commentRefactorTransaction);
            final TextView dateView = view.findViewById(R.id.dateRefactorTransaction);
            ImageView imageView = view.findViewById(R.id.imageViewRefactorTransaction);
            final TextView categoryTextView = view.findViewById(R.id.categoryRefactorTextView);
            final TextView sumTextView = view.findViewById(R.id.sumRefactorTransaction);

            commentView.setText(comment);
            dateView.setText(Transaction.DATE_FORMAT.format(date));
            imageView.setBackgroundResource(category.getIconResId());
            categoryTextView.setText(category.toString());
            sumTextView.setText(String.format("%.2f", ((float) transaction.sum) / 100));
            final AlertDialog dialog = builder.create();
            view.findViewById(R.id.okButtonRefactorTransaction).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Transaction newTransaction = new Transaction(transaction);
                    newTransaction.category = Category.fromString(categoryTextView.getText().toString());
                    newTransaction.comment = commentView.getText().toString();
                    try {
                        newTransaction.date = Transaction.DATE_FORMAT.parse(dateView.getText().toString());
                        newTransaction.sum = (long) (Float.parseFloat(sumTextView.getText().toString()) * 100);
                    } catch (ParseException ignored) {
                        Toast.makeText(getContext(), "Cannot change data", Toast.LENGTH_SHORT).show();
                    }
                    catch (NumberFormatException ignored) {}
                    viewModel.update(newTransaction);
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.cancelButtonRefactorTransaction).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };
}
