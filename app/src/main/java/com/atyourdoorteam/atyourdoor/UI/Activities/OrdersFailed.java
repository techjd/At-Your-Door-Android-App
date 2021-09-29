package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.atyourdoorteam.atyourdoor.R;

public class OrdersFailed extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_failed);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("ORDER FAILED");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent openMain = new Intent(OrdersFailed.this, MainActivity.class);
                startActivity(openMain);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}