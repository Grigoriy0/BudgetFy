package com.grigoriy0.budgetfy.accountdetails;

import android.graphics.Canvas;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
                date = (new SimpleDateFormat("dd.MM.yyyy")).parse(
                        ((TextView) transactionView.findViewById(R.id.dateTextView)).getText().toString());
            } catch (ParseException e) {
                date = Calendar.getInstance().getTime();
            }

            Transaction transaction = new Transaction(sum, category, comment, accountId);
            transaction.id = id;
            transaction.date = date;
            transaction.loss = category.isLoss();


            viewModel.update(transaction);
        }
    };
}
