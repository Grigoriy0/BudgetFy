package com.grigoriy0.budgetfy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;

public class TransactionsFragment extends Fragment {
    private TransactionsAdapter adapter;

    public TransactionsFragment(){}

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
        adapter.submitList(Arrays.asList(new Transaction(-12.67f, Category.CAFE),
                new Transaction(-5.50f, Category.BARBER),
                new Transaction(-0.65f, Category.TRANSPORT),
                new Transaction(-5.50f, Category.UNIVERSITY),
                new Transaction(-0.65f, Category.FOOD),
                new Transaction(-5.50f, Category.RENT),
                new Transaction(-0.65f, Category.TRANSPORT),
                new Transaction(-5.50f, Category.MISCELLANEOUS),
                new Transaction(-0.65f, Category.PHONE)));
        return parentView;
    }
}
