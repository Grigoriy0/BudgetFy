package com.grigoriy0.budgetfy.accountdetails;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grigoriy0.budgetfy.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class EditTransactionDialog extends Dialog implements View.OnClickListener {
    private Transaction transactionToEdit;
    private final TextView commentView;
    private final TextView categoryView;
    private final TextView sumView;
    private final TextView dateView;
    private final boolean isLoss;
    private final TransactionsFragment fragment;
    private final Float accountCurrentValue;

    public EditTransactionDialog(TransactionsFragment fragment,
                                 final View transactionView,
                                 final Float accountCurrentValue) {
        super(fragment.getContext());
        setContentView(R.layout.edit_transaction_dialog);
        this.fragment = fragment;
        this.accountCurrentValue = accountCurrentValue;
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
        transactionToEdit = new Transaction(sum, category, comment, fragment.accountId);
        transactionToEdit.id = id;
        transactionToEdit.date = date;
        transactionToEdit.loss = category.isLoss();

        commentView = findViewById(R.id.commentEditTransaction);
        dateView = findViewById(R.id.dateEditTransaction);
        ImageView imageView = findViewById(R.id.imageViewEditTransaction);
        categoryView = findViewById(R.id.categoryEditTransaction);
        sumView = findViewById(R.id.sumEditTransaction);
        commentView.setText(comment);
        dateView.setText(Transaction.DATE_FORMAT.format(date));
        imageView.setBackgroundResource(category.getIconResId());
        categoryView.setText(category.toString());
        sumView.setText(String.format("%.2f", Math.abs((float) transactionToEdit.sum) / 100));
        isLoss = category.isLoss();
        findViewById(R.id.yesEditTransaction).setOnClickListener(this);
        findViewById(R.id.noEditTransaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Transaction newTransaction = new Transaction(transactionToEdit);
        newTransaction.category = Category.fromString(categoryView.getText().toString());
        newTransaction.comment = commentView.getText().toString();
        try {
            newTransaction.date = Transaction.DATE_FORMAT.parse(dateView.getText().toString());
            float value = Float.parseFloat(sumView.getText().toString()) * 100;
            if (value == 0) {
                Toast.makeText(fragment.getContext(), "Enter non-zero value", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (value < 0) {
                value = -value;
            }
            if (isLoss && (value / 100 > +(float) transactionToEdit.sum / 100)) {
                Toast.makeText(fragment.getContext(), "You do not have that much money", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            newTransaction.sum = (long) value;
        } catch (ParseException ignored) {
            Toast.makeText(fragment.getContext(), "Cannot change data", Toast.LENGTH_SHORT)
                    .show();
        } catch (NumberFormatException ignored) {
        }
        fragment.viewModel.update(newTransaction);
    }
}
