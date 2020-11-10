package com.grigoriy0.budgetfy.accountdetails;

import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.grigoriy0.budgetfy.R;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class RefactorTransactionHelper {
    private final Transaction transaction;
    private final AlertDialog dialog;
    private final TransactionViewModel viewModel;

    private final TextView commentView;
    private final TextView categoryView;
    private final TextView sumView;
    private final TextView dateView;


    public RefactorTransactionHelper(final TransactionsFragment fragment,
                                     View transactionView,
                                     final Float accountCurrentValue) {
        this.viewModel = fragment.viewModel;

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
        transaction = new Transaction(sum, category, comment, fragment.accountId);
        transaction.id = id;
        transaction.date = date;
        transaction.loss = category.isLoss();

        AlertDialog.Builder builder = new AlertDialog.Builder(fragment.getContext());
        View dialogView = LayoutInflater.from(fragment.getContext()).inflate(
                R.layout.transaction_details_dialog,
                (LinearLayout) fragment.getView().findViewById(R.id.transactionDetailsContainer)
        );
        builder.setView(dialogView);
        commentView = dialogView.findViewById(R.id.commentRefactorTransaction);
        dateView = dialogView.findViewById(R.id.dateRefactorTransaction);
        ImageView imageView = dialogView.findViewById(R.id.imageViewRefactorTransaction);
        categoryView = dialogView.findViewById(R.id.categoryRefactorTextView);
        sumView = dialogView.findViewById(R.id.sumRefactorTransaction);
        commentView.setText(comment);
        dateView.setText(Transaction.DATE_FORMAT.format(date));
        imageView.setBackgroundResource(category.getIconResId());
        categoryView.setText(category.toString());
        sumView.setText(String.format("%.2f", Math.abs((float) transaction.sum) / 100));
        final boolean isLoss = category.isLoss();

        dialog = builder.create();
        dialogView.findViewById(R.id.okButtonRefactorTransaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Transaction newTransaction = new Transaction(transaction);
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
                    if (isLoss && (value / 100 > accountCurrentValue)) {
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
                viewModel.update(newTransaction);
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.cancelButtonRefactorTransaction).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

}
