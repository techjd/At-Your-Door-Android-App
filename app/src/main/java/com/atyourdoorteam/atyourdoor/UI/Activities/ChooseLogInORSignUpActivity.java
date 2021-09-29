package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.atyourdoorteam.atyourdoor.R;

public class ChooseLogInORSignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login_signup);


        TextView signUp = (TextView) findViewById(R.id.signUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLogInORSignUpActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


        Button buttons = (Button) findViewById(R.id.logIn);
        buttons.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ChooseLogInORSignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}