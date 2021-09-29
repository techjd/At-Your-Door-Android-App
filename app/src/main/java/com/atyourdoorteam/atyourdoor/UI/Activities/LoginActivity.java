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
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.Token;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private String email, password;
    private Button login;
    private TextView forgotPassword, newAccount;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private SharedPreferences shopIdPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextEmail = findViewById(R.id.edtLoginEmail);
        editTextPassword = findViewById(R.id.edtloginPassword);
        login = findViewById(R.id.loginUser);
        sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        shopIdPreferences = getSharedPreferences("SHOPID", Context.MODE_PRIVATE);
        // Forgot Password (Text view)
        TextView tvForgot = (TextView) findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        // Don't have an account ( Sign up button)
        Button signupbtn = (Button) findViewById(R.id.registration_activity);
        signupbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editTextEmail.getText().toString();
                password = editTextPassword.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                SharedPreferences.Editor shopIdEditor = shopIdPreferences.edit();
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setTitle("Loading");
                progressDialog.setMessage("Logging You In");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    APIService apiService = RetrofitInstance.getService();

                    Call<Token> loginCall = apiService.loginUser(email, password);

                    loginCall.enqueue(new Callback<Token>() {
                        @Override
                        public void onResponse(Call<Token> call, Response<Token> response) {
                            if (response.isSuccessful()) {
                                Log.d("TOKEN", response.body().getToken());
                                Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                editor.putString("TOKEN", response.body().getToken());
                                editor.commit();
                                shopIdEditor.putString("SHOPID", "EMPTY");
                                shopIdEditor.commit();

                                Intent navigate = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(navigate);
                            } else {
                                Log.d("ERROR", response.message());

                                progressDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "MayBe Your Are Entering Wrong Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Token> call, Throwable t) {
                            Log.d("ERROR", t.getMessage());
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });


    }
}