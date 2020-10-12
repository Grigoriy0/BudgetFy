package com.grigoriy0.budgetfy.accountdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grigoriy0.budgetfy.R;

import java.util.List;

public class TransactionsFragment extends Fragment {
    private TransactionsAdapter adapter;
    private TransactionViewModel viewModel;
    private int accountId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_transaction, container, false);
        final RecyclerView transactionsRecyclerView = parentView.findViewById(R.id.transactionsView);
        viewModel = ViewModelProviders.of(this).get(TransactionViewModel.class);
        viewModel.init(accountId);
        viewModel.getAccountTransactions().observe(this, new Observer<List<Transaction>>() {
            @Override
            public void onChanged(List<Transaction> transactions) {
                adapter.setTransactions(transactions);
            }
        });
        adapter = new TransactionsAdapter();
        transactionsRecyclerView.setAdapter(adapter);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(parentView.getContext()));
        return parentView;
    }

    public TransactionsFragment(int accountId) {
        this.accountId = accountId;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
