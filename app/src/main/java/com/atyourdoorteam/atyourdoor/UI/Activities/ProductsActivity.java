package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.ProductsAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.db.entities.Cart;
import com.atyourdoorteam.atyourdoor.db.Database;
import com.atyourdoorteam.atyourdoor.models.Products;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {
    private ProductsAdapter productsAdapter;
    private RecyclerView productsRecyclerView;

    private String shopId;
    private String subCategoryId;
    private ProgressDialog progressDialog;
    private Database database;
    private ArrayList<Cart> cartList;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ProductsAdapter.ProductsViewHolder productsViewHolder;
    private String shopshopID;
    private SharedPreferences sharedPreferences;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        shopId = getIntent().getStringExtra("SHOPID");
        subCategoryId = getIntent().getStringExtra("SUBCATEGORYID");
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("PRODUCTS");
        shimmerFrameLayout = findViewById(R.id.shimmerLayoutForProducts);
        shimmerFrameLayout.startShimmer();
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(ProductsActivity.this));
        database = Room.databaseBuilder(this, Database.class, "CartDB").allowMainThreadQueries().build();

        cartList = (ArrayList<Cart>) database.getCartDao().getProducts();
//        sharedPreferences = getSharedPreferences("SHOPID", Context.MODE_PRIVATE);
//        shopshopID = sharedPreferences.getString("SHOPID", "");
//        Log.d("WHAT IS INSIDE", shopshopID);
//        progressDialog = new ProgressDialog(ProductsActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Categories");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProducts();
            }
        }, 700);

    }

    private void getProducts() {
        APIService apiService = RetrofitInstance.getService();

        Call<List<Products>> productsListCall = apiService.getProductsListBySubCategoryOfSpecificShop(shopId, subCategoryId);

        productsListCall.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful()) {
                    List<Products> productsList = response.body();

                    ArrayList<Products> productsArrayList = new ArrayList<>();

                    for (int i = 0; i < productsList.size(); i++) {
                        productsArrayList.add(new Products(
                                productsList.get(i).getId(),
                                productsList.get(i).getShopId(),
                                productsList.get(i).getMainCategoryId(),
                                productsList.get(i).getSubCategoryId(),
                                productsList.get(i).getProductImageURL(),
                                productsList.get(i).getProductName(),
                                productsList.get(i).getProductPrice()
                        ));

//                        for(int k = 0; k < cartList.size(); k++) {
//
//                            if (cartList.get(k).getId().equals(productsList.get(i).getId())) {
//                                Log.d("MATCH", " IS MAYCHED");
//
//                            productsViewHolder.addToCard.setVisibility(View.INVISIBLE);
//
//                            }
//                        }


                    }

                    productsAdapter = new ProductsAdapter(productsArrayList, ProductsActivity.this);
                    productsRecyclerView.setAdapter(productsAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
//                    progressDialog.dismiss();

                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(ProductsActivity.this, "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}