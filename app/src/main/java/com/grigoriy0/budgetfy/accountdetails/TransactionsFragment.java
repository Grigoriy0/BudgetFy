package com.grigoriy0.budgetfy.accountdetails;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.grigoriy0.budgetfy.R;

import java.util.List;

public class TransactionsFragment extends Fragment {
    private TransactionsAdapter adapter;
    private List<Transaction> transactions;

    public TransactionsFragment(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View parentView = inflater.inflate(R.layout.fragment_transaction, container, false);
        final RecyclerView transactionsRecyclerView = parentView.findViewById(R.id.transactionsView);
        adapter = new TransactionsAdapter();
        transactionsRecyclerView.setAdapter(adapter);
        transactionsRecyclerView.setLayoutManager(new LinearLayoutManager(parentView.getContext()));
        adapter.submitList(transactions);
        return parentView;
    }
}
