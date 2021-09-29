package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.DetailCategoriesAdapter;
import com.atyourdoorteam.atyourdoor.Adapters.ShopCategoriesAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.Categories;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopCategoriesActivity extends AppCompatActivity {
    private String shopName;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView shopDetailRecyclerView;
    private ShopCategoriesAdapter shopCategoriesAdapter;
    private String shopId;
    private ProgressDialog progressDialog;
    private TextView noCategoriesAvailable;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_categories);
        shopName = getIntent().getStringExtra("SHOPNAME");
        shopId = getIntent().getStringExtra("SHOPID");
        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(shopName);
        shimmerFrameLayout = findViewById(R.id.shimmerLayourForShopCategories);
        shimmerFrameLayout.startShimmer();
        noCategoriesAvailable = findViewById(R.id.noCategoriesAvailable);
        shopDetailRecyclerView = findViewById(R.id.shopCategoriesRecyclerView);
        shopDetailRecyclerView.setLayoutManager(new LinearLayoutManager(ShopCategoriesActivity.this));
//        progressDialog = new ProgressDialog(ShopCategoriesActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Categories");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getCategories();
            }
        }, 700);


    }

    private void getCategories() {
        APIService apiService = RetrofitInstance.getService();

        Call<List<Categories>> listCategories = apiService.getCategoriesOfSpecificShop(shopId);

        listCategories.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                if (response.isSuccessful()) {
                    List<Categories> categoriesList1 = response.body();

                    ArrayList<Categories> categoriesArrayList = new ArrayList<>();

                    for (int i = 0; i < categoriesList1.size(); i++) {

                        categoriesArrayList.add(new Categories(categoriesList1.get(i).getId(), categoriesList1.get(i).getCategoryName(),
                                categoriesList1.get(i).getCategoriesImage()));
                    }
                    Log.d("SIZE", String.valueOf(categoriesArrayList.size()));
                    if (categoriesArrayList.size() == 0) {
                        noCategoriesAvailable.setVisibility(View.VISIBLE);
                        noCategoriesAvailable.setText("Ooh.. No..  No Products Available in this Shop \uD83D\uDE25. Maybe Try Other Shop \uD83D\uDE04!! ");
//                        progressDialog.dismiss();
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    } else {
                        shopCategoriesAdapter = new ShopCategoriesAdapter(categoriesArrayList, getApplicationContext(), shopId);
                        shopDetailRecyclerView.setAdapter(shopCategoriesAdapter);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
//                        progressDialog.dismiss();
                    }

                }
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
                progressDialog.dismiss();
                Toast.makeText(ShopCategoriesActivity.this, "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}