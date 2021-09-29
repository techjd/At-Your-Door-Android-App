package com.atyourdoorteam.atyourdoor.UI.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.CartAdapter;
import com.atyourdoorteam.atyourdoor.Adapters.ProductsAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.UI.Activities.AddressActivity;
import com.atyourdoorteam.atyourdoor.UI.Activities.CheckOutActivity;

import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.models.UserInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShoppingCartFragment extends Fragment {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private Database database;
    private ArrayList<Cart> carts;
    private CartAdapter cartAdapter;
    private RecyclerView cartRecyclerView;
    private ProductsAdapter.ProductsViewHolder productsViewHolder;
    private Button checkout;
    private int total = 0;
    private TextView totalPrice;
    private SharedPreferences sharedPreferences;
    private String token;
    private ProgressDialog progressDialog;
    private ArrayList<Cart> cartArrayListforDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shoppingcart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getContext().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("TOKEN", "");

        toolbar = view.findViewById(R.id.toolbar);
        toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("YOUR CART");
        totalPrice = view.findViewById(R.id.totalPrice);
        checkout = view.findViewById(R.id.checkout);

        cartRecyclerView = view.findViewById(R.id.cartRecyclerView);

        cartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        cartRecyclerView.addItemDecoration(dividerItemDecoration);

        database = Room.databaseBuilder(getContext(), Database.class, "CartDB").allowMainThreadQueries().build();
        carts = (ArrayList<Cart>) database.getCartDao().getProducts();
        cartAdapter = new CartAdapter(carts, getContext(), totalPrice);
        getTotalPrice();
        cartRecyclerView.setAdapter(cartAdapter);

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int totalPrice = database.getCartDao().getTotal();
                if (totalPrice == 0) {
                    Toast.makeText(getContext(), "Please Add Some Items To Cart", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog = new ProgressDialog(v.getContext());
                    progressDialog.setTitle("Processing");
                    progressDialog.setMessage("Processing Details");
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    APIService apiService = RetrofitInstance.getService();

                    Call<UserInfo> userDetails = apiService.getLoggedInUserInfo(token);

                    userDetails.enqueue(new Callback<UserInfo>() {
                        @Override
                        public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                            if (response.isSuccessful()) {
                                UserInfo userInfo = response.body();

                                if (userInfo.getPrimaryAddress().getAddress().equals("no")) {
                                    Toast.makeText(v.getContext(), "You Don't Have any Address Saved , Please Add Address", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(v.getContext(), AddressActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent openNextActivty = new Intent(v.getContext(), CheckOutActivity.class);
                                    openNextActivty.putExtra("PRIMARYADDRESS", userInfo.getPrimaryAddress().getAddress() + " " + userInfo.getPrimaryAddress().getCity() + " " + userInfo.getPrimaryAddress().getState() + " " + userInfo.getPrimaryAddress().getPostalCode());
                                    startActivity(openNextActivty);
                                }
                                progressDialog.dismiss();

                            }
                        }

                        @Override
                        public void onFailure(Call<UserInfo> call, Throwable t) {

                        }
                    });
                }


            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Cart cartItem = carts.get(viewHolder.getAdapterPosition());
                deleteItem(cartItem);
            }
        }).attachToRecyclerView(cartRecyclerView);


    }

    void getAllItems() {
        new GetAllCartItemsAsyncTask().execute();
    }

    private class GetAllCartItemsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            cartArrayListforDB = (ArrayList<Cart>) database.getCartDao().getProducts();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cartAdapter.setCarts(cartArrayListforDB);
        }
    }


    private void deleteItem(Cart cartItem) {
        new DeleteItemAsyncTask().execute(cartItem);
    }

    private class DeleteItemAsyncTask extends AsyncTask<Cart, Void, Void> {

        @Override
        protected Void doInBackground(Cart... carts) {
            database.getCartDao().delete(carts[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getAllItems();
            getTotalPrice();
//            getCartList();
        }
    }


    private void getTotalPrice() {
        new getTotalPriceAsyncTask().execute();
    }

    private class getTotalPriceAsyncTask extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... voids) {
            int total = database.getCartDao().getTotal();
            return total;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("THIS IS THE TOTAL", String.valueOf(integer));
            totalPrice.setText(String.valueOf(integer));
        }
    }


    public void updateTotalPrice() {
        new UpdateTotalPrice().execute();
    }

    private class UpdateTotalPrice extends AsyncTask<Void, Void, Integer> {


        @Override
        protected Integer doInBackground(Void... voids) {
            database = Room.databaseBuilder(getContext(), Database.class, "CartDB").build();
            ArrayList<Cart> cartArrayList = new ArrayList<>();
            cartArrayList = (ArrayList<Cart>) database.getCartDao().getProducts();
            for (int i = 0; i < cartArrayList.size(); i++) {
                int productPrice = cartArrayList.get(i).getProductPrice();
                int quantity = cartArrayList.get(i).getProductQuantity();

                total += productPrice * quantity;
            }
            return total;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Log.d("TOTALPRICE", String.valueOf(total));
            totalPrice.setText(String.valueOf(total));

        }
    }


}
