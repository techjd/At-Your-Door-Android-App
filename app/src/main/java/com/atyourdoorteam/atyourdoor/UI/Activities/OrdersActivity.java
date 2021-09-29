package com.atyourdoorteam.atyourdoor.UI.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.atyourdoorteam.atyourdoor.API.APIService;
import com.atyourdoorteam.atyourdoor.API.RetrofitInstance;
import com.atyourdoorteam.atyourdoor.Adapters.OrdersAdapter;
import com.atyourdoorteam.atyourdoor.R;
import com.atyourdoorteam.atyourdoor.models.FinalOrderedItems.FinalOrders;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView toolbar_title;
    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private ProgressDialog progressDialog;
    private ShimmerFrameLayout shimmerFrameLayout;
    private TextView noOrdersYet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        toolbar = findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("YOUR PREVIOUS ORDER");
        SharedPreferences sharedPreferences = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("TOKEN", "");
        shimmerFrameLayout = findViewById(R.id.shimmerOrders);
        noOrdersYet = findViewById(R.id.noOrdersYet);
        shimmerFrameLayout.startShimmer();
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        progressDialog = new ProgressDialog(OrdersActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setMessage("Loading Profile Information");
//        progressDialog.setCancelable(false);
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();
        APIService apiService = RetrofitInstance.getService();

        Call<List<FinalOrders>> finalOrdersCall = apiService.getAllOrders(token);

        finalOrdersCall.enqueue(new Callback<List<FinalOrders>>() {
            @Override
            public void onResponse(Call<List<FinalOrders>> call, Response<List<FinalOrders>> response) {
                List<FinalOrders> finalOrders = response.body();

                ArrayList<FinalOrders> finalOrdersArrayList = new ArrayList<>();

                for (int i = 0; i < finalOrders.size(); i++) {
                    finalOrdersArrayList.add(new FinalOrders(
                            finalOrders.get(i).getIsPaid(),
                            finalOrders.get(i).getId(),
                            finalOrders.get(i).getUser(),
                            finalOrders.get(i).getShopId(),
                            finalOrders.get(i).getOrderMode(),
                            finalOrders.get(i).getOrderedItems(),
                            finalOrders.get(i).getShippingAddress(),
                            finalOrders.get(i).getTotalPrice(),
                            finalOrders.get(i).getCreatedAt()
                    ));
                }

                if (finalOrdersArrayList.size() == 0) {
                    noOrdersYet.setVisibility(View.VISIBLE);
                    noOrdersYet.setText("You Don't Have any Orders , Why don't Your Order Something !!  \uD83D\uDE04 ");
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                } else {
                    ordersAdapter = new OrdersAdapter(finalOrdersArrayList);
                    ordersRecyclerView.setAdapter(ordersAdapter);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                }


//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<FinalOrders>> call, Throwable t) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
            }
        });

    }
}