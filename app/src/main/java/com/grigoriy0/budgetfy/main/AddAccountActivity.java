package com.grigoriy0.budgetfy.main;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.grigoriy0.budgetfy.R;

public class AddAccountActivity extends AppCompatActivity {
    public static final String EXTRA_NAME =
            "com.grigoriy0.budgetfy.EXTRA_NAME";
    public static final String EXTRA_START =
            "com.grigoriy0.budgetfy.EXTRA_START";
    public static final String EXTRA_TYPE =
            "com.grigoriy0.budgetfy.EXTRA_TYPE";
    private EditText startValue;
    private String type;
    private EditText accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        startValue = findViewById(R.id.startValueEditText);
        accountName = findViewById(R.id.accountNameEditText);

        type = getIntent().getStringExtra(EXTRA_TYPE);
        setTitle("Add " + type.toLowerCase());
        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_account_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_account:
                saveAccount(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveAccount(@Nullable View v) {
        String name = accountName.getText().toString();
        float start = 0;
        try{
            start = Float.parseFloat(startValue.getText().toString());
        } catch (NumberFormatException e){
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            return;
        }
        if (name.trim().isEmpty() || start < 0.0f) {
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_TYPE, type);
        data.putExtra(EXTRA_START, start);
        setResult(RESULT_OK, data);
        finish();
    }

    public void finish(View view) {
        finish();
    }
}