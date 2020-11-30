package com.grigoriy0.budgetfy.accountdetails;

import android.app.Dialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.grigoriy0.budgetfy.R;

import java.text.ParseException;
import java.time.DateTimeException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class EditTransactionDialog extends Dialog implements View.OnClickListener {
    private final Transaction transactionToEdit;
    private final TextView commentView;
    private final TextView categoryView;
    private final TextView sumView;
    private final TextView dateView;
    private final boolean isLoss;
    private final TransactionsFragment fragment;
    private final float oldValue;
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
        float value;
        try {
            String str = ((TextView) transactionView.findViewById(R.id.sumTextView)).getText().toString();
            value = Float.parseFloat(str.substring(1).replace(',', '.'));
        } catch (Exception e) {
            Toast.makeText(fragment.getContext(), "Error. Default value will be 0", Toast.LENGTH_LONG)
                    .show();
            value = 0;
        }
        oldValue = value;
        Category category = Category.fromString(((TextView) transactionView.findViewById(R.id.categoryTextView)).getText().toString());
        String comment = ((TextView) transactionView.findViewById(R.id.transactionCommentTextView)).getText().toString();
        Date date;
        try {
            date = (Transaction.DATE_FORMAT).parse(
                    ((TextView) transactionView.findViewById(R.id.dateTextView)).getText().toString());
        } catch (ParseException e) {
            date = Calendar.getInstance().getTime();
        }
        transactionToEdit = new Transaction(0, category, comment, fragment.accountId);
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
        sumView.setText(String.format(Locale.getDefault(), "%.2f", Math.abs(oldValue)));
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
        Date now = Calendar.getInstance().getTime();
        try {
            newTransaction.date = Transaction.DATE_FORMAT.parse(dateView.getText().toString());
            float value = Float.parseFloat(sumView.getText().toString().replace(',', '.'));
            if (newTransaction.date.getYear() > now.getYear()
                    || newTransaction.date.getMonth() > now.getMonth()
                    || newTransaction.date.getDay() > now.getDay())
                throw new ParseException("", 0);
            if (value == 0) {
                Toast.makeText(fragment.getContext(), "Enter non-zero value", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (value < 0) {
                value = -value;
            }

            if (isLoss && (accountCurrentValue + oldValue < value)) {
                Toast.makeText(fragment.getContext(), "You do not have much money", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            value *= 100f;
            newTransaction.sum = (long) value;
        } catch (ParseException ignored) {
            Toast.makeText(fragment.getContext(), "Invalid date value", Toast.LENGTH_SHORT)
                    .show();
            return;
        } catch (NumberFormatException ignored) {
        }
        fragment.viewModel.update(newTransaction);
        dismiss();
    }
}
