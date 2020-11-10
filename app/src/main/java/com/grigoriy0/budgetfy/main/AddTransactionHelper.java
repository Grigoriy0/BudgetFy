package com.grigoriy0.budgetfy.main;

import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.grigoriy0.budgetfy.R;
import com.grigoriy0.budgetfy.accountdetails.Category;
import com.grigoriy0.budgetfy.accountdetails.Transaction;
import com.grigoriy0.budgetfy.accountdetails.TransactionRepository;

import java.util.Calendar;
import java.util.UUID;

public class AddTransactionHelper {
    private final MainActivity mainActivity;
    private final UUID accountId;
    final TextView commentView;
    final TextView sumView;
    final RadioGroup radioGroup;
    final View bottomSheetView;
    final BottomSheetDialog dialog;
    private final float accountCurrentValue;
    private final AccountUpdater updater;

    public AddTransactionHelper(final UUID accountId, final AccountUpdater updater, final Float accountCurrentValue, final MainActivity mainActivity, boolean isLoss) {
        this.mainActivity = mainActivity;
        this.accountId = accountId;
        this.accountCurrentValue = accountCurrentValue;
        this.updater = updater;
        dialog = new BottomSheetDialog(
                mainActivity, R.style.BottomSheetDialogTheme);
        if (isLoss) {
            bottomSheetView = LayoutInflater.from(mainActivity.getApplicationContext())
                    .inflate(R.layout.activity_add_loss,
                            (LinearLayout) mainActivity.findViewById(R.id.lossContainer),
                            false
                    );
            dialog.setContentView(bottomSheetView);
            commentView = bottomSheetView.findViewById(R.id.commentEditLoss);
            sumView = bottomSheetView.findViewById(R.id.valueEditLoss);
            radioGroup = bottomSheetView.findViewById(R.id.radioGroupLoss);
            bottomSheetView.findViewById(R.id.applyLossButton).setOnClickListener(addTransactionListener);
            bottomSheetView.findViewById(R.id.cancelLossButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            bottomSheetView = LayoutInflater.from(mainActivity.getApplicationContext())
                    .inflate(R.layout.activity_add_increase,
                            (LinearLayout) mainActivity.findViewById(R.id.increaseContainer),
                            false
                    );
            dialog.setContentView(bottomSheetView);
            commentView = bottomSheetView.findViewById(R.id.commentEditIncrease);
            sumView = bottomSheetView.findViewById(R.id.valueEditIncrease);
            radioGroup = bottomSheetView.findViewById(R.id.radioGroupIncrease);
            bottomSheetView.findViewById(R.id.applyIncreaseButton).setOnClickListener(addTransactionListener);
            bottomSheetView.findViewById(R.id.cancelIncreaseButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        if (dialog.getWindow() != null)
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
    }

    private View.OnClickListener addTransactionListener = new View.OnClickListener() {
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
                    Toast.makeText(mainActivity.getApplicationContext(), "Enter non-zero value", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (value < 0) {
                    if (isLoss)
                        value = -value;
                    else {
                        Toast.makeText(mainActivity.getApplicationContext(), "Type positive value", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
                if (isLoss && (value / 100 > accountCurrentValue)) {
                    Toast.makeText(mainActivity.getApplicationContext(), "You do not have that much money", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                sum = (long) value;
            } catch (NumberFormatException ex) {
                Toast.makeText(mainActivity.getApplicationContext(), "Type correct value", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            Transaction transactionToAdd = new Transaction(sum, Category.fromString(category), comment, accountId);
            transactionToAdd.id = UUID.randomUUID();
            transactionToAdd.loss = isLoss;
            transactionToAdd.date = Calendar.getInstance().getTime();

            TransactionRepository tr = new TransactionRepository(mainActivity.getApplication(), accountId);
            tr.addTransaction(transactionToAdd);
            updater.addTransaction(transactionToAdd);
            String msg = (isLoss ? "Loss" : "Increase")
                    + " added";
            Toast.makeText(mainActivity, msg, Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }
    };
}
