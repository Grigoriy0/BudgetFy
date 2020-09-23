package com.grigoriy0.budgetfy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onPressButtonPressed(View view) {
        if (view == findViewById(R.id.accountButton)) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
        }
    }

}