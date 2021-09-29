package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.CategoriesAdapter;
import com.atyourdoorteam.atyourdoor.Adapters.DetailCategoriesAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.Categories;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllCategoriesActivity extends AppCompatActivity {
    private RecyclerView categoriesrecyclerView;
    private DetailCategoriesAdapter detailCategoriesAdapter;
    private Toolbar toolbar;
    private TextView toolbar_title;
    private ProgressDialog progressDialog;
    private ShimmerFrameLayout shimmerFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_categories);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("ALL CATEGORIES");

        shimmerFrameLayout = findViewById(R.id.shimmerLayoutCategories);
        shimmerFrameLayout.startShimmer();

        categoriesrecyclerView = findViewById(R.id.recyclerView);
        categoriesrecyclerView.setLayoutManager(new LinearLayoutManager(AllCategoriesActivity.this));
//        progressDialog = new ProgressDialog(AllCategoriesActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Categories");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                apiCall();
            }
        }, 700);


    }

    private void apiCall() {

        APIService apiService = RetrofitInstance.getService();

        Call<List<Categories>> categoriesList = apiService.getCategoriesList();
        categoriesList.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                if (response.isSuccessful()) {
                    List<Categories> categoriesList1 = response.body();

                    ArrayList<Categories> categoriesArrayList = new ArrayList<>();

                    for (int i = 0; i < categoriesList1.size(); i++) {

                        categoriesArrayList.add(new Categories(categoriesList1.get(i).getId(), categoriesList1.get(i).getCategoryName(),
                                categoriesList1.get(i).getCategoriesImage()));
                    }
                    detailCategoriesAdapter = new DetailCategoriesAdapter(categoriesArrayList, getApplicationContext());
                    categoriesrecyclerView.setAdapter(detailCategoriesAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
//                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Log.d("ERROR", t.getMessage());
//                progressDialog.dismiss();
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                Toast.makeText(AllCategoriesActivity.this, "Please Try Again ! Serve Side Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}