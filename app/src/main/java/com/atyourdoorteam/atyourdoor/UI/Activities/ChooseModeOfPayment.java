package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.room.Room;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Fragments.HomeFragment;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.models.Categories;
import com.atyourdoorteam.atyourdoor.models.Message;
import com.google.gson.Gson;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseModeOfPayment extends AppCompatActivity implements PaymentResultWithDataListener {
    private CardView payOnline, orderOnUdhaar, cod;
    private Integer totalPrice;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private String token;
    private String shopId;
    private SharedPreferences sharedPreferences;
    private ArrayList<Cart> cartArrayList;
    private List<Cart> cartItems;
    private Database database;
    private String address;
    private String orderId;
    private ProgressDialog progressDialog;
    private String payByOnline = "onlineMethod";
    private String payByUdhaar = "udhaarMethod";
    private String payByCod = "codMethod";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_mode_of_payment);
        Checkout.preload(getApplicationContext());

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("MODE OF PAYMENT");
        sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");

        totalPrice = getIntent().getIntExtra("TOTAL PRICE", 0);
        shopId = getIntent().getStringExtra("SHOPID");
        database = Room.databaseBuilder(ChooseModeOfPayment.this, Database.class, "CartDB").build();
        Log.d("SHOPID", shopId);
        Log.d("PRICE", String.valueOf(totalPrice));
        payOnline = findViewById(R.id.payOnline);
        orderOnUdhaar = findViewById(R.id.orderOnUdhaar);
        cod = findViewById(R.id.cashOnDelievery);

        cartItems = getIntent().getParcelableArrayListExtra("ARRAY");
        Log.d("TYPE", cartItems.getClass().getSimpleName());
        Log.d("SIZE OF CART", String.valueOf(cartItems.size()));
        Log.d("CHANGING", new Gson().toJson(cartItems));
        address = getIntent().getStringExtra("ADDRESS");
        Log.d("ADDRESS", address);
        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrder();
                startPayment();
            }
        });


        orderOnUdhaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderByUdhaar();
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeOrderByCod();
            }
        });
    }

    private void placeOrderByCod() {
        progressDialog = new ProgressDialog(ChooseModeOfPayment.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Processing Your Request");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        APIService apiService = RetrofitInstance.getService();

        Call<Message> orderByCod = apiService.checkOutByCod(token, shopId, payByCod, new Gson().toJson(cartItems), address, totalPrice);
        orderByCod.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChooseModeOfPayment.this, "Order Successfully Place By COD Method", Toast.LENGTH_LONG).show();
                    deleteAllRecords();
                    progressDialog.dismiss();
                    Intent intent = new Intent(ChooseModeOfPayment.this, OrderPlaced.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Intent intent = new Intent(ChooseModeOfPayment.this, OrdersFailed.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    private void deleteAllRecords() {
        new DeleteAllRecords().execute();
    }

    private class DeleteAllRecords extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            database.getCartDao().deleteAllRecords();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("DELETION MESSAGE", "ALL PRODUCTS DELETED SUCCESSFULLY");
        }
    }


    private void placeOrderByUdhaar() {
        progressDialog = new ProgressDialog(ChooseModeOfPayment.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Processing Your Request");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        APIService apiService = RetrofitInstance.getService();
        Call<Message> orderByUdhaar = apiService.checkOutByUdhaar(token, shopId, payByUdhaar, new Gson().toJson(cartItems), address, totalPrice);
        orderByUdhaar.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        Toast.makeText(ChooseModeOfPayment.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                        Log.d("BODY", response.body().getMsg());
                        progressDialog.dismiss();
                        Intent intent = new Intent(ChooseModeOfPayment.this, OrdersFailed.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else if (response.code() == 201) {
                        Toast.makeText(ChooseModeOfPayment.this, "Order Successfully Place By Udhaar Method", Toast.LENGTH_LONG).show();
                        deleteAllRecords();
                        progressDialog.dismiss();
                        Intent intent = new Intent(ChooseModeOfPayment.this, OrderPlaced.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Intent intent = new Intent(ChooseModeOfPayment.this, OrdersFailed.class);
                startActivity(intent);
            }
        });
    }

    private void placeOrder() {
        progressDialog = new ProgressDialog(ChooseModeOfPayment.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Processing Your Request");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        APIService apiService = RetrofitInstance.getService();

        Call<Message> messageCall = apiService.checkOut(token, shopId, payByOnline, new Gson().toJson(cartItems), address, totalPrice);
        messageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {
                    Log.d("ORDER ID", response.body().getMsg());
                    orderId = response.body().getMsg();
                    deleteAllRecords();
                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                progressDialog.dismiss();
                Intent intent = new Intent(ChooseModeOfPayment.this, OrdersFailed.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_qxDuKzTTaQoeLH");
        /**
         * Set your logo here
         */


        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "At Your Door");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");//from response of step 3.
            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", totalPrice * 100);//pass amount in currency subunits
            options.put("prefill.email", "atyourdoor01@gmail.com");
            options.put("prefill.contact", "7016301356");
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e("ERROR", "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try {
            Log.e("TAG", " payment successfull " + s.toString());
            Intent intent = new Intent(ChooseModeOfPayment.this, OrderPlaced.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            updateValueToTrue();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
//        Toast.makeText(this, paymentData.getPaymentId(), Toast.LENGTH_LONG).show();

//        try {
//            Toast.makeText(this, "Payment successfully done! " +s, Toast.LENGTH_SHORT).show();
//        } catch (Exception e) {
//
//        }
    }

    private void updateValueToTrue() {
        APIService apiService = RetrofitInstance.getService();

        Call<Message> messageCall = apiService.getResponse(token, orderId);
        Log.d("IS IT EXECUTING", "YESSS");
        messageCall.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(ChooseModeOfPayment.this, response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {

            }
        });
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Log.e("main", "error code " + String.valueOf(i) + " -- Payment failed " + s.toString());
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        try {
            Toast.makeText(this, "Payment error ! Try Again After Some Time", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("OnPaymentError", "Exception in onPaymentError", e);
        }
    }
}