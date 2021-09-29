package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;
import com.atyourdoorteam.atyourdoor.models.Address.PrimaryAddress;
import com.atyourdoorteam.atyourdoor.models.Message;
import com.atyourdoorteam.atyourdoor.models.UserInfo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private TextView primaryAddress;
    private TextView secondaryAddress;
    private Button editprimaryAddress, editsecondaryAddress;
    private ProgressDialog progressDialog;
    private String address, pincode, city, state, token;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("YOUR ADDRESS BOOK");
        primaryAddress = findViewById(R.id.primaryAddress);
//        secondaryAddress = findViewById(R.id.secondaryAddress);
        editprimaryAddress = findViewById(R.id.editPrimaryAddress);
//        editsecondaryAddress = findViewById(R.id.editSecondaryAddress);
        loadData();
        editprimaryAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setCancelable(false);
                final View view1 = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_add_address, null);
                builder.setView(view1);

                Button add;
                Button cancel;
                EditText edtAddress, edtPinCode, edtCity, edtState;
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                edtAddress = view1.findViewById(R.id.edtAddress);
                edtPinCode = view1.findViewById(R.id.edtPinCode);
                edtCity = view1.findViewById(R.id.edtCity);
                edtState = view1.findViewById(R.id.edtState);
                add = view1.findViewById(R.id.addAddress);
                cancel = view1.findViewById(R.id.cancel);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressDialog = new ProgressDialog(AddressActivity.this);
                        progressDialog.setTitle("Updating");
                        progressDialog.setMessage("Updating Primary Address");
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();

                        address = edtAddress.getText().toString();
                        pincode = edtPinCode.getText().toString();
                        city = edtCity.getText().toString();
                        state = edtState.getText().toString();

                        APIService apiService = RetrofitInstance.getService();

                        Call<Message> addPrimaryAddress = apiService.addPrimaryAddress(token, address, pincode, city, state);

                        addPrimaryAddress.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                if (response.isSuccessful()) {
                                    Message message = response.body();

                                    Toast.makeText(AddressActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();

                                    progressDialog.dismiss();

                                    alertDialog.dismiss();
                                    loadData();

                                }
                            }

                            @Override
                            public void onFailure(Call<Message> call, Throwable t) {
                                progressDialog.dismiss();
                            }
                        });
                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });


            }
        });

//        editsecondaryAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
//
//                builder.setCancelable(false);
//                final View view1 = LayoutInflater.from(v.getContext()).inflate(R.layout.layout_add_address, null);
//                builder.setView(view1);
//
//                Button add;
//                Button cancel;
//                EditText edtAddress, edtPinCode, edtCity, edtState;
//                AlertDialog alertDialog = builder.create();
//                alertDialog.show();
//                edtAddress = view1.findViewById(R.id.edtAddress);
//                edtPinCode = view1.findViewById(R.id.edtPinCode);
//                edtCity = view1.findViewById(R.id.edtCity);
//                edtState = view1.findViewById(R.id.edtState);
//                add = view1.findViewById(R.id.addAddress);
//                cancel = view1.findViewById(R.id.cancel);
//                add.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        progressDialog = new ProgressDialog(AddressActivity.this);
//                        progressDialog.setTitle("Updating");
//                        progressDialog.setMessage("Updating Primary Address");
//                        progressDialog.setCancelable(false);
//                        progressDialog.setCanceledOnTouchOutside(false);
//                        progressDialog.show();
//
//                        address = edtAddress.getText().toString();
//                        pincode = edtPinCode.getText().toString();
//                        city = edtCity.getText().toString();
//                        state = edtState.getText().toString();
//
//                        APIService apiService = RetrofitInstance.getService();
//
//                        Call<Message> addPrimaryAddress = apiService.addSecondaryAddress(token, address, pincode, city, state);
//
//                        addPrimaryAddress.enqueue(new Callback<Message>() {
//                            @Override
//                            public void onResponse(Call<Message> call, Response<Message> response) {
//                                if (response.isSuccessful()) {
//                                    Message message = response.body();
//
//                                    Toast.makeText(AddressActivity.this, message.getMsg(), Toast.LENGTH_SHORT).show();
//
//                                    progressDialog.dismiss();
//
//                                    alertDialog.dismiss();
//                                    loadData();
//
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<Message> call, Throwable t) {
//                                progressDialog.dismiss();
//                            }
//                        });
//                    }
//                });
//
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog.show();
//            }
//        });


    }

    private void loadData() {
        progressDialog = new ProgressDialog(AddressActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Loading Address");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        APIService apiService = RetrofitInstance.getService();

        Call<UserInfo> getUserInfo = apiService.getLoggedInUserInfo(token);

        getUserInfo.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if (response.isSuccessful()) {
                    UserInfo userInfo = response.body();
                    if (userInfo.getPrimaryAddress().getAddress().equals("no")) {
                        primaryAddress.setText("No Address");
                    }

//                    if (userInfo.getSecondaryAddress().getAddress().equals("no")) {
//                        secondaryAddress.setText("No Address");
//                    }

                    if (userInfo.getPrimaryAddress().getAddress() != "no") {
                        String priAddress = userInfo.getPrimaryAddress().getAddress() + " " + userInfo.getPrimaryAddress().getCity() + " " + userInfo.getPrimaryAddress().getState() + " " + userInfo.getPrimaryAddress().getPostalCode();
                        primaryAddress.setText(priAddress);
                    }

//                    if (userInfo.getSecondaryAddress().getAddress() != "no") {
//                        String secAddress = userInfo.getSecondaryAddress().getAddress() + " " + userInfo.getSecondaryAddress().getCity() + " " + userInfo.getSecondaryAddress().getState() + " " + userInfo.getSecondaryAddress().getPostalCode();
//                        secondaryAddress.setText(secAddress);
//                    }


                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}