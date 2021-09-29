package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {
    private EditText edtName, edtEmail, edtPassword, edtConfirmpassword, edtNumber;
    private String name, email, password, confirmpassword, number;
    private Button register;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private RetrofitInstance retrofitInstance;
    private SharedPreferences shopIdPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);
        edtConfirmpassword = findViewById(R.id.confirmPassword);
        edtNumber = findViewById(R.id.phoneNumber);
        register = findViewById(R.id.register);
        sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        shopIdPreferences = getSharedPreferences("SHOPID", Context.MODE_PRIVATE);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edtName.getText().toString();
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                confirmpassword = edtConfirmpassword.getText().toString();
                number = edtNumber.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                SharedPreferences.Editor shopIdEditor = shopIdPreferences.edit();
                progressDialog = new ProgressDialog(RegistrationActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Registering " + name);
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Name", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(RegistrationActivity.this, "Please Write Password Again To Verify", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (TextUtils.isEmpty(number)) {
                    Toast.makeText(RegistrationActivity.this, "Please Enter Phone Number", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (!password.equals(confirmpassword)) {
                    Toast.makeText(RegistrationActivity.this, "Passwords Do Not Match", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    APIService apiService = RetrofitInstance.getService();
                    Call<Token> tokenCall = apiService.registerUser(email, name, password, number);
                    tokenCall.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(RegistrationActivity.this, "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                editor.putString("TOKEN", response.body().getToken());
                                editor.commit();
                                shopIdEditor.putString("SHOPID", "EMPTY");
                                shopIdEditor.commit();

                                Intent navigate = new Intent(RegistrationActivity.this, MainActivity.class);
                                startActivity(navigate);
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Log.d("ERROR", t.getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(RegistrationActivity.this, "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });
    }


}