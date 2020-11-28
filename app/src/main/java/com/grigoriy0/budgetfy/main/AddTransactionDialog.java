package com.grigoriy0.budgetfy.main;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.Category;
import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;

import java.util.Calendar;
import java.util.UUID;

public class AddTransactionDialog extends BottomSheetDialog implements View.OnClickListener {
    private final MainActivity activity;
    private final UUID accountId;
    final TextView commentView;
    final TextView sumView;
    final RadioGroup radioGroup;
    final View bottomSheetView;
    private final float accountCurrentValue;
    private final AccountUpdater updater;

    public AddTransactionDialog(@NonNull MainActivity activity, final UUID accountId, final AccountUpdater updater, final Float accountCurrentValue, boolean isLoss) {
        super(activity, R.style.BottomSheetDialogTheme);
        this.activity = activity;
        this.accountId = accountId;
        this.accountCurrentValue = accountCurrentValue;
        this.updater = updater;
        if (isLoss) {
            bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(R.layout.activity_add_loss,
                            (LinearLayout) activity.findViewById(R.id.lossContainer),
                            false
                    );
            setContentView(bottomSheetView);
            commentView = bottomSheetView.findViewById(R.id.commentEditLoss);
            sumView = bottomSheetView.findViewById(R.id.valueEditLoss);
            radioGroup = bottomSheetView.findViewById(R.id.radioGroupLoss);
            bottomSheetView.findViewById(R.id.applyLossButton).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.cancelLossButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            bottomSheetView = LayoutInflater.from(activity.getApplicationContext())
                    .inflate(R.layout.activity_add_increase,
                            (LinearLayout) activity.findViewById(R.id.increaseContainer),
                            false
                    );
            setContentView(bottomSheetView);
            commentView = bottomSheetView.findViewById(R.id.commentEditIncrease);
            sumView = bottomSheetView.findViewById(R.id.valueEditIncrease);
            radioGroup = bottomSheetView.findViewById(R.id.radioGroupIncrease);
            bottomSheetView.findViewById(R.id.applyIncreaseButton).setOnClickListener(this);
            bottomSheetView.findViewById(R.id.cancelIncreaseButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
        if (getWindow() != null)
            getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    @Override
    public void onClick(View v) {
        String comment = commentView.getText().toString();
        if (bottomSheetView.findViewById(radioGroup.getCheckedRadioButtonId()) == null) {
            return;
        }
        String category = ((RadioButton) bottomSheetView.findViewById(
                radioGroup.getCheckedRadioButtonId())).getText().toString();
        long sum;
        boolean isLoss = Category.fromString(category).isLoss();
        try {
            float value = Float.parseFloat(sumView.getText().toString()) * 100;
            if (value == 0) {
                Toast.makeText(activity.getApplicationContext(), "Enter non-zero value", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (value < 0) {
                if (isLoss)
                    value = -value;
                else {
                    Toast.makeText(activity.getApplicationContext(), "Type positive value", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
            }
            if (isLoss && (value / 100 > accountCurrentValue)) {
                Toast.makeText(activity.getApplicationContext(), "You do not have that much money", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            sum = (long) value;
        } catch (NumberFormatException ex) {
            Toast.makeText(activity.getApplicationContext(), "Type correct value", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        Transaction transactionToAdd = new Transaction(sum, Category.fromString(category), comment, accountId);
        transactionToAdd.id = UUID.randomUUID();
        transactionToAdd.loss = isLoss;
        transactionToAdd.date = Calendar.getInstance().getTime();

        TransactionRepository tr = new TransactionRepository(activity.getApplication(), accountId);
        tr.addTransaction(transactionToAdd);
        updater.addTransaction(transactionToAdd);
        String msg = (isLoss ? "Loss" : "Increase")
                + " added";
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
