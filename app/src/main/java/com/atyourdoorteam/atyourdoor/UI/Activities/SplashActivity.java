package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.atyourdoorteam.atyourdoor.R;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");

        Log.d("TOKEN", token);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (token.isEmpty()) {
                    Intent intent = new Intent(SplashActivity.this, ChooseLogInORSignUpActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Intent openMain = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(openMain);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}