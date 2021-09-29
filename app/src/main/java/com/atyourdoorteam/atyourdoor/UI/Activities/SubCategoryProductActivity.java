package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.DisplayProductFromSubCategoryAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.SubCategoriesProducts.ProductsBySubCategories;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubCategoryProductActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView productsFromSubCategories;
    private DisplayProductFromSubCategoryAdapter displayProductFromSubCategoryAdapter;
    private TextView noProductsAvailable;
    private String subCategoryId;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_product);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("PRODUCTS");
        subCategoryId = getIntent().getStringExtra("SUBCATEGORYID");
        noProductsAvailable = findViewById(R.id.noProductsAvailableinSubCategory);
        shimmerFrameLayout = findViewById(R.id.shimmerLayourForShopCategories);
        shimmerFrameLayout.startShimmer();
        productsFromSubCategories = findViewById(R.id.productFromSubCategories);
        productsFromSubCategories.setLayoutManager(new LinearLayoutManager(this));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProducts();
            }
        }, 700);


    }

    private void getProducts() {
        APIService apiService = RetrofitInstance.getService();

        Call<List<ProductsBySubCategories>> product = apiService.getProductsBySubCategory(subCategoryId);
        product.enqueue(new Callback<List<ProductsBySubCategories>>() {
            @Override
            public void onResponse(Call<List<ProductsBySubCategories>> call, Response<List<ProductsBySubCategories>> response) {
                if (response.isSuccessful()) {
                    List<ProductsBySubCategories> productsBySubCategoriesList = response.body();

                    ArrayList<ProductsBySubCategories> productsBySubCategoriesArrayList = new ArrayList<>();

                    for (int i = 0; i < productsBySubCategoriesList.size(); i++) {
                        productsBySubCategoriesArrayList.add(new ProductsBySubCategories(
                                productsBySubCategoriesList.get(i).getId(),
                                productsBySubCategoriesList.get(i).getShopId(),
                                productsBySubCategoriesList.get(i).getMainCategoryId(),
                                productsBySubCategoriesList.get(i).getSubCategoryId(),
                                productsBySubCategoriesList.get(i).getProductImageURL(),
                                productsBySubCategoriesList.get(i).getProductName(),
                                productsBySubCategoriesList.get(i).getProductPrice()
                        ));
                    }

                    if (productsBySubCategoriesArrayList.size() == 0) {
                        noProductsAvailable.setVisibility(View.VISIBLE);
                        noProductsAvailable.setText("Ooh.. No..  No Products Available in this Sub Category \uD83D\uDE25. Maybe Try Other Category \uD83D\uDE04!! ");
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    } else {
                        displayProductFromSubCategoryAdapter = new DisplayProductFromSubCategoryAdapter(productsBySubCategoriesArrayList, SubCategoryProductActivity.this);
                        productsFromSubCategories.setAdapter(displayProductFromSubCategoryAdapter);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<ProductsBySubCategories>> call, Throwable t) {

            }
        });
    }
}