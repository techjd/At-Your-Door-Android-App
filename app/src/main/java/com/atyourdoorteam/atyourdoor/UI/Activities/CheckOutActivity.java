package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.Adapters.CheckOutItemsListAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Fragments.GroceyListFragment;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.entities.GroceryList;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class CheckOutActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private CheckOutItemsListAdapter checkOutItemsListAdapter;
    private RecyclerView checkoutItemsListRecyclerView;
    private Database database;
    private ArrayList<Cart> cartArrayList;
    private TextView totalPrice;
    private ProgressDialog progressDialog;
    private int total = 0;
    private String primaryAddress, secondaryAddress;
    private Button checkOut;
    private EditText address;
    private String edtString;
    private String shopId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("LET'S CHECKOUT");
        totalPrice = findViewById(R.id.totalPrice);

        primaryAddress = getIntent().getStringExtra("PRIMARYADDRESS");
//        secondaryAddress = getIntent().getStringExtra("SECONDARYADDRESS");
        Log.d("ADDRESS", primaryAddress + secondaryAddress);
        checkOut = findViewById(R.id.checkout);
        address = findViewById(R.id.address);
        address.setText(primaryAddress);

        progressDialog = new ProgressDialog(CheckOutActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Getting All Things Ready");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        database = Room.databaseBuilder(this, Database.class, "CartDB").build();

        checkoutItemsListRecyclerView = findViewById(R.id.ordersItemRecyclerView);
        checkoutItemsListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        checkOutItemsListAdapter = new CheckOutItemsListAdapter(this);
        checkoutItemsListRecyclerView.setAdapter(checkOutItemsListAdapter);
        loadData();
        progressDialog.dismiss();
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtString = address.getText().toString();
//                Toast.makeText(CheckOutActivity.this, edtString, Toast.LENGTH_LONG).show();
                Intent moveToPaymentScreen = new Intent(CheckOutActivity.this, ChooseModeOfPayment.class);
                moveToPaymentScreen.putExtra("TOTAL PRICE", total);
                moveToPaymentScreen.putExtra("SHOPID", shopId);
                moveToPaymentScreen.putExtra("ADDRESS", edtString);
                moveToPaymentScreen.putParcelableArrayListExtra("ARRAY", (ArrayList<? extends Parcelable>) cartArrayList);
                startActivity(moveToPaymentScreen);
            }
        });
    }


    private void loadData() {
        new GetAllItemsAsyncTask().execute();
    }

    private class GetAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {


            cartArrayList = (ArrayList<Cart>) database.getCartDao().getProducts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            checkOutItemsListAdapter.setLists(cartArrayList);
            for (int i = 0; i < cartArrayList.size(); i++) {
                int productPrice = cartArrayList.get(i).getProductPrice();
                int quantity = cartArrayList.get(i).getProductQuantity();
                shopId = cartArrayList.get(i).getShopId();
                total += productPrice * quantity;


            }
            Log.d("TOTALPRICE", String.valueOf(total));
            Log.d("SHOPID", shopId);
            totalPrice.setText("₹ " + String.valueOf(total));
            progressDialog.dismiss();
        }
    }
}